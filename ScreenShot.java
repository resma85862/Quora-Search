package MiniProject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;


public class ScreenShot {
	// Define the directory where screenshots will be saved
	private static final String SCREENSHOT_DIR = "screenshots";
	// Create the screenshots folder if it doesn't exist
	static {
		File dir = new File(SCREENSHOT_DIR);
		if (!dir.exists()) {   
			dir.mkdir();
			}
		}



/**

* Captures a screenshot and saves it to a file with a dynamic timestamp.

* * @param driver The WebDriver instance.

* @param filename The base name for the screenshot file.

* @return The absolute path to the saved screenshot file.

*/

public static String takeScreenshot(WebDriver driver, String filename) {
	// Create a unique filename with a timestamp to avoid overwriting
	String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	String fullFilename = filename + "_" + timestamp + ".png";
	File file = new File(SCREENSHOT_DIR + "/" + fullFilename);
	try {
		TakesScreenshot ts = (TakesScreenshot) driver;//enable ss function
		File src = ts.getScreenshotAs(OutputType.FILE);
		FileHandler.copy(src, file);
		System.out.println("Saved screenshot: " + file.getAbsolutePath());
		return file.getAbsolutePath(); //path of the saved file.
		} catch (IOException e) {
			System.out.println("Failed to save screenshot: " + fullFilename);
			e.printStackTrace();
			return null; // Return null if the screenshot fails
			}
	}
}