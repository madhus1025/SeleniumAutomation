package com.crm.qa.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DriverManager {

    private static final String DRIVER_URL = "https://storage.googleapis.com/chrome-for-testing-public/134.0.6998.165/mac-arm64/chromedriver-mac-arm64.zip";
    private static final String DRIVER_DIR = System.getProperty("user.dir") + "/drivers";
    private static final String DRIVER_PATH = DRIVER_DIR + "/chromedriver";

    // Ensure chromedriver exists, download and unzip if necessary
    public static String getChromeDriverPath() {
        File chromeDriverFile = new File(DRIVER_PATH);
        if (!chromeDriverFile.exists()) {
            System.out.println("Chromedriver not found. Downloading..."+ DRIVER_PATH);
            try {
                // Create the drivers directory if it doesn't exist
                Files.createDirectories(Paths.get(DRIVER_DIR));

                // Download chromedriver zip
                String zipFilePath = DRIVER_PATH + ".zip";
                downloadFile(DRIVER_URL, zipFilePath);
                System.out.println("File downloaded to: " + zipFilePath);

                // Unzip the downloaded file
                unzipFile(zipFilePath, DRIVER_DIR);
                Files.delete(Paths.get(zipFilePath)); // Delete the zip file after extraction

                // Make the chromedriver executable
                chromeDriverFile.setExecutable(true);
                System.out.println("Chromedriver downloaded and ready to use.");
            } catch (IOException e) {
                System.out.println("The error is  " + e.getMessage());
                throw new RuntimeException("Failed to download or extract chromedriver: " + e.getMessage());
            }
        }

        return DRIVER_PATH;
    }

    private static void downloadFile(String fileURL, String savePath) {
        try (InputStream in = new URL(fileURL).openStream();
             FileOutputStream out = new FileOutputStream(savePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytes = 0; // Variable to track the total size of the file
    
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytes += bytesRead; // Increment the total size
            }
    
            System.out.println("File downloaded successfully to: " + savePath);
            System.out.println("File size: " + totalBytes + " bytes");
        } catch (IOException e) {
            System.out.println("Failed to download file from URL: " + fileURL);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Unzip a file to a destination directory
    private static void unzipFile(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
    
        try (ZipInputStream zipIn = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ZipEntry entry = zipIn.getNextEntry();
            System.out.println("Extracting entry: " + zipFilePath+"   "+entry);
            while (entry != null) {
                System.out.println("Inside loop entry: " + zipFilePath+"   "+entry.getName());
                String filePath = destDir + File.separator + "chromedriver";
                if (!entry.isDirectory() && entry.getName().equals("chromedriver-mac-arm64/chromedriver")) {
                    // Extract only the chromedriver binary
                    System.out.println("Extracting the right file: " + filePath);
                    try (FileOutputStream fos = new FileOutputStream(filePath)) {
                        System.out.println("Inside: " + filePath);
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = zipIn.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                        System.out.println("Extracted: " + filePath);
                    } catch (IOException e) {
                        System.out.println("Failed to write file: " + filePath);
                        System.out.println("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    System.out.println("Extracted: " + filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            System.out.println("Failed to unzip file: " + zipFilePath);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}