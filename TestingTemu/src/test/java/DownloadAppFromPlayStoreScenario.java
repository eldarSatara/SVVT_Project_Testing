import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DownloadAppFromPlayStoreScenario {

    private static WebDriver webDriver;
    private static String baseUrl;

    // How to set up the connection to the webpage and find the Chrome driver
    @BeforeAll
    public static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Eldar\\Desktop\\IBU\\SVVT\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"); // specify the path to chromedriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.temu.com/"; //Specify the webpage
    }

    @Test
    public void testFooterAppDownloadButtons() {
        webDriver.get(baseUrl);

        WebElement footer = webDriver.findElement(By.tagName("footer"));
        List<WebElement> links = footer.findElements(By.tagName("a"));

        boolean playStoreFound = false;
        boolean appStoreFound = false;

        for (WebElement link : links) {
            String href = link.getAttribute("href");
            if (href == null) continue;

            // Google Play
            if (href.contains("play.google.com")) {
                playStoreFound = true;

                assertTrue(link.isDisplayed(), "Google Play button not visible");
                assertTrue(link.isEnabled(), "Google Play button not enabled");
                assertTrue(href.startsWith("https"),
                        "Google Play link is not HTTPS");
            }

            // Apple App Store
            if (href.contains("apps.apple.com")) {
                appStoreFound = true;

                assertTrue(link.isDisplayed(), "App Store button not visible");
                assertTrue(link.isEnabled(), "App Store button not enabled");
                assertTrue(href.startsWith("https"),
                        "App Store link is not HTTPS");
            }
        }

        assertTrue(playStoreFound, "Google Play download button not found");
        assertTrue(appStoreFound, "Apple App Store download button not found");
    }

    //How to close the connection after every test scenario
    @AfterAll
    public static void tearDown() {
        // Close the browser
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
