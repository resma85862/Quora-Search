package MiniProject;

import java.io.IOException;
import java.util.Scanner;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.MediaEntityBuilder;

public class QuoraSearch {
    
    public static ExtentReports extent;
    public static ExtentTest test;
    public static WebDriver driver;

    @BeforeSuite
    public void setupReport() {
    	//Creates a reporter that will generate an HTML report
        ExtentSparkReporter spark = new ExtentSparkReporter("QuoraTestReport.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Quora Automation Report");
        spark.config().setReportName("Quora Search Test");
        //Creates the main ExtentReports object.
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Reshma Farheen");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version",System.getProperty("java.version"));
    }

    // Helper method to log a message and a screenshot
    private void logWithScreenshot(Status status, String message, String screenshotName) {
        String screenshotPath = ScreenShot.takeScreenshot(driver, screenshotName);
        if (screenshotPath != null) {
            try {
                test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to attach screenshot: " + e.getMessage());
            }
        } else {
            test.log(status, message + " (Screenshot failed)");
        }
    }
//core test method
    @Test
    public void searchQuoraProfile() {
        test = extent.createTest("Quora Search and Profile Update Test");
        ExcelSet excelSet = new ExcelSet();

        Scanner sc = new Scanner(System.in);
        String browser = null;
        
        try {
            System.out.println("Starting Quora Automation Test...");
            System.out.println("-------------------------------------");

            System.out.print("Enter the Browser (Chrome/Edge): ");
            browser = sc.nextLine();

            // Launch browser
            System.out.println("Attempting to launch " + browser + " browser...");
            if (browser.equalsIgnoreCase("Chrome")) {
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\2425532\\Downloads\\chrome driver new\\chromedriver-win64\\chromedriver.exe");
                driver = new ChromeDriver();
                test.log(Status.PASS, "✅ Chrome browser launched successfully.");
                excelSet.writeData("Browser Launch", "Pass", "Launched " + browser + " successfully");
            } else if (browser.equalsIgnoreCase("Edge")) {
                System.setProperty("webdriver.edge.driver", "C:\\Users\\2425532\\Downloads\\edge driver v86\\msedgedriver.exe");
                driver = new EdgeDriver();
                test.log(Status.PASS, "✅ Edge browser launched successfully.");
                excelSet.writeData("Browser Launch", "Pass", "Launched " + browser + " successfully");
            } else {
                String errorMessage = "❌ Invalid browser input: " + browser;
                System.out.println(errorMessage);
                test.log(Status.FAIL, errorMessage);
                excelSet.writeData("Browser Launch", "Fail", errorMessage);
                return;
            }
            driver.manage().window().maximize();
            test.log(Status.PASS, "Browser window maximized.");

            // Navigate to Quora profile and take a screenshot
            System.out.println("Navigating to Quora profile page...");
            driver.get("https://www.quora.com/profile/Quora");
            logWithScreenshot(Status.PASS, "✅ Navigated to Quora profile page.", "photo_maximize");
            excelSet.writeData("Open Quora Profile", "Pass", "Page loaded and maximized");

            // Search input
            try {
                System.out.println("Attempting to find search input field and enter 'Testing'...");
                WebElement search = driver.findElement(By.xpath("//*[@id=\"mainContent\"]/div[2]/div/div[4]/div[2]/div/div/input"));
                search.sendKeys("Testing");
                test.log(Status.FAIL, "❌ Suggestion for Search term 'Testing' is not Displayed.");
                excelSet.writeData("Search Input", "Fail", "Search term entered");
                logWithScreenshot(Status.INFO, "Search term 'Testing' entered.", "photo_search");
            } catch (Exception e) {
                test.log(Status.FAIL, "❌ Error entering search term: " + e.getMessage());
                excelSet.writeData("Search Input", "Fail", "Error entering search: " + e.getMessage());
                logWithScreenshot(Status.FAIL, "Error while searching.", "photo_search_error");
            }
            
            // Click search and navigate to profile edit
            try {
                System.out.println("Attempting to click search and navigate to profile edit...");
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/div[2]/div/div[2]/div/button")).click();
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/div/div/div[2]/div/span/span[4]/div")).click();
                excelSet.writeData("Navigate to Edit Profile", "Pass", "Navigation successful");
                logWithScreenshot(Status.PASS, "✅ Successfully navigated to profile edit page.", "navigate_to_edit_profile");
            } catch (Exception e) {
                test.log(Status.FAIL, "❌ Navigation failed: " + e.getMessage());
                excelSet.writeData("Navigate to Edit Profile", "Fail", "Navigation failed: " + e.getMessage());
                logWithScreenshot(Status.FAIL, "Navigation failed due to error.", "navigate_error");
            }

            // Enter username
            try {
                System.out.println("Attempting to enter username...");
                driver.findElement(By.xpath("//*[@id=\"profile-name\"]")).sendKeys("Reshma");
                excelSet.writeData("Enter Username", "Pass", "Username entered");
                logWithScreenshot(Status.PASS, "✅ Username 'Reshma' entered successfully.", "photo_username");
            } catch (Exception e) {
                excelSet.writeData("Enter Username", "Fail", "Error entering username: " + e.getMessage());
                test.log(Status.FAIL, "❌ Error entering username: " + e.getMessage());
                logWithScreenshot(Status.FAIL, "Failed to enter username.", "username_error");
            }

            // Enter email
            try {
                System.out.println("Attempting to enter email...");
                driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("abc@abc");
                excelSet.writeData("Enter Email", "Fail", "Email entered (invalid format)");
                logWithScreenshot(Status.FAIL, "❌ Email 'abc@abc' entered successfully (invalid format).", "photo_email");
            } catch (Exception e) {
                excelSet.writeData("Enter Email", "Fail", "Error entering email: " + e.getMessage());
                test.log(Status.FAIL, "❌ Error entering email: " + e.getMessage());
                logWithScreenshot(Status.FAIL, "Failed to enter email.", "email_error");
            }

            // Final screenshot
            try {
                System.out.println("Waiting for 3 seconds before final screenshot...");
                Thread.sleep(3000);
                excelSet.writeData("Final Screenshot", "Pass", "Captured before quitting");
                logWithScreenshot(Status.PASS, "✅ Final screenshot captured successfully.", "photo_final");
            } catch (Exception e) {
                excelSet.writeData("Final Screenshot", "Fail", "Error capturing final screenshot: " + e.getMessage());
                test.log(Status.FAIL, "❌ Error capturing final screenshot: " + e.getMessage());
                logWithScreenshot(Status.FAIL, "Final screenshot failed to capture.", "final_screenshot_error");
            }

            excelSet.saveFile("DefectReport.xlsx");
            
        } catch (TimeoutException e) {
            test.log(Status.FAIL, "❌ Test failed due to timeout: " + e.getMessage());
            excelSet.writeData("Overall Test Execution", "Fail", "Timeout occurred: " + e.getMessage());
            logWithScreenshot(Status.FAIL, "Test failed due to timeout.", "timeout_error");
        } catch (Exception e) {
            test.log(Status.FAIL, "❌ An unexpected error occurred: " + e.getMessage());
            excelSet.writeData("Overall Test Execution", "Fail", "Unexpected error: " + e.getMessage());
            logWithScreenshot(Status.FAIL, "An unexpected error occurred.", "unexpected_error");
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
    }
    
    @AfterMethod
    public void tearDownMethod() {
        if (driver != null) {
            driver.quit();
            System.out.println("✅ Browser closed and reports saved.");
        }
    }
//saving the final report
    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}