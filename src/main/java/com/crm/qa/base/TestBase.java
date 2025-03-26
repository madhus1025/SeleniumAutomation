package com.crm.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.BeforeSuite;

import com.crm.qa.util.DriverManager;
import com.crm.qa.util.TestUtil;
import com.crm.qa.util.WebEventListener;

public class TestBase {

    public static WebDriver driver;
    public static Properties prop;
    public static EventFiringWebDriver e_driver;
    public static WebEventListener eventListener;

    // Constructor to load properties
    public TestBase() {
        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/src/main/java/com/crm"
                    + "/qa/config/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Singleton method to initialize WebDriver
    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    // Initialize WebDriver
    private static void initializeDriver() {
        String browserName = prop.getProperty("browser");

        if (browserName.equals("chrome")) {
            // Wait for chromedriver to be ready
            String chromeDriverPath = DriverManager.getChromeDriverPath();
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            driver = new ChromeDriver();
        } else if (browserName.equals("FF")) {
            System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");
            driver = new FirefoxDriver();
        }

        // Register WebDriver with EventFiringWebDriver
        e_driver = new EventFiringWebDriver(driver);
        eventListener = new WebEventListener();
        e_driver.register(eventListener);
        driver = e_driver;

        // Browser configurations
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(TestUtil.IMPLICIT_WAIT, TimeUnit.SECONDS);

        // Navigate to the URL
        driver.get(prop.getProperty("url"));
    }

    // Quit WebDriver
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null; // Reset the driver instance
        }
    }

    // Ensure chromedriver is ready before any tests start
    @BeforeSuite
    public void setupChromeDriver() {
        System.out.println("Checking and setting up chromedriver...");
        DriverManager.getChromeDriverPath(); // Ensures chromedriver is downloaded and ready
    }
}
