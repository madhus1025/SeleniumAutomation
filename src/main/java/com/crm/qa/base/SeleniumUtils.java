package com.crm.qa.base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeleniumUtils {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private String defaultWindowHandle;

    // Constructor
    public SeleniumUtils(int timeoutInSeconds) {
        this.driver = TestBase.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        this.actions = new Actions(driver);
        this.defaultWindowHandle = driver.getWindowHandle();
        setImplicitWait(timeoutInSeconds);
    }

    // ================== WAIT METHODS ==================

    public void setImplicitWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    public WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // ================== ACTION METHODS ==================

    public void hoverOverElement(By locator) {
        WebElement element = waitForElementToBeVisible(locator, 10);
        actions.moveToElement(element).perform();
    }

    public void enterText(By locator, String text) {
        WebElement element = waitForElementToBeVisible(locator, 10);
        element.clear();
        element.sendKeys(text);
    }

    public void rightClick(By locator) {
        WebElement element = waitForElementToBeVisible(locator, 10);
        actions.contextClick(element).perform();
    }

    public void clickElement(By locator) {
        WebElement element = waitForElementToBeClickable(locator, 10);
        element.click();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    // ================== FIND ELEMENTS ==================

    public WebElement findElement(By locator) {
        return waitForElementToBeVisible(locator, 10);
    }

    public List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    // ================== WINDOW HANDLING ==================

    public Set<String> getAllWindowHandles() {
        return driver.getWindowHandles();
    }

    public void switchToWindowByTitle(String windowTitle) {
        for (String window : driver.getWindowHandles()) {
            driver.switchTo().window(window);
            if (driver.getTitle().equals(windowTitle)) {
                break;
            }
        }
    }

    public void switchToWindowByIndex(int index) {
        List<String> windows = new ArrayList<>(driver.getWindowHandles());
        if (index < windows.size()) {
            driver.switchTo().window(windows.get(index));
        }
    }

    public void closeCurrentWindowAndSwitchBack() {
        String currentHandle = driver.getWindowHandle();
        driver.close();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(currentHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

    public void switchToDefaultWindow() {
        driver.switchTo().window(defaultWindowHandle);
    }

    // ================== TABLE DATA EXTRACTION ==================

    public List<List<String>> getTableData(By tableLocator) {
        WebElement table = waitForElementToBeVisible(tableLocator, 10);
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        List<List<String>> tableData = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            List<String> rowData = new ArrayList<>();
            for (WebElement cell : cells) {
                rowData.add(cell.getText());
            }
            if (!rowData.isEmpty()) {
                tableData.add(rowData);
            }
        }
        return tableData;
    }

    // ================== DROPDOWN SELECTION ==================

    public void selectByVisibleText(By locator, String visibleText) {
        WebElement dropdown = waitForElementToBeVisible(locator, 10);
        new Select(dropdown).selectByVisibleText(visibleText);
    }

    public void selectByValue(By locator, String value) {
        WebElement dropdown = waitForElementToBeVisible(locator, 10);
        new Select(dropdown).selectByValue(value);
    }

    public void selectByIndex(By locator, int index) {
        WebElement dropdown = waitForElementToBeVisible(locator, 10);
        new Select(dropdown).selectByIndex(index);
    }

    public List<String> getDropdownOptions(By locator) {
        WebElement dropdown = waitForElementToBeVisible(locator, 10);
        List<WebElement> options = new Select(dropdown).getOptions();
        List<String> optionTexts = new ArrayList<>();
        for (WebElement option : options) {
            optionTexts.add(option.getText());
        }
        return optionTexts;
    }
}
