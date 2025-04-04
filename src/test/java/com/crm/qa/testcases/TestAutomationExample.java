package com.crm.qa.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.crm.qa.base.SeleniumUtils;
import com.crm.qa.base.TestBase;
import com.crm.qa.pages.HomePage;
import com.crm.qa.pages.LoginPage;
import com.crm.qa.util.TestUtil;

import java.util.List;
import java.util.Properties;

public class TestAutomationExample extends TestBase{
	
	LoginPage loginPage;
	HomePage homePage;
	TestUtil testUtil;

	private WebDriver driver;
	private SeleniumUtils seleniumUtils;

	@BeforeMethod
	public void setUp() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
		driver = TestBase.getDriver();
		testUtil = new TestUtil();
		driver.manage().window().maximize();
		loginPage = new LoginPage();
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		TestUtil.runTimeInfo("error", "login successful");

		
		seleniumUtils = new SeleniumUtils(10);
	}

	@Test
	public void testLogin() {
		seleniumUtils.enterText(By.id("username"), "testUser");
		seleniumUtils.enterText(By.id("password"), "testPass123");
		seleniumUtils.clickElement(By.id("loginButton"));

		// Verify title after login
		System.out.println("Page Title: " + seleniumUtils.getPageTitle());
	}

	@Test
	public void testFindElements() {
		List<WebElement> links = seleniumUtils.findElements(By.tagName("a"));
		System.out.println("Total links on page: " + links.size());
		for (WebElement link : links) {
			System.out.println("Link Text: " + link.getText());
		}
	}

	@Test
	public void testDropdownSelection() {
		seleniumUtils.selectByVisibleText(By.id("countryDropdown"), "United States");
		List<String> options = seleniumUtils.getDropdownOptions(By.id("countryDropdown"));
		System.out.println("Available Countries: " + options);
	}

	@Test
	public void testTableData() {
		List<List<String>> tableData = seleniumUtils.getTableData(By.id("dataTable"));
		System.out.println("Table Data: " + tableData);
	}

	@Test
	public void testWindowSwitching() {
		seleniumUtils.switchToWindowByIndex(1);
		System.out.println("Switched to window: " + seleniumUtils.getPageTitle());
		seleniumUtils.switchToDefaultWindow();
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
