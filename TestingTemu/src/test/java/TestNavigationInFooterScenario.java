import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Set;

public class TestNavigationInFooterScenario {
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
    public void testFooterLinks() {

        webDriver.get(baseUrl);

        // Locate the footer
        WebElement footer = webDriver.findElement(By.tagName("footer"));

        // Get all links inside the footer
        List<WebElement> footerLinks = footer.findElements(By.tagName("a"));

        Assertions.assertFalse(
                footerLinks.isEmpty(),
                "No footer links were found."
        );

        String originalWindow = webDriver.getWindowHandle();

        for (WebElement link : footerLinks) {

            // Skip invisible or empty links
            if (!link.isDisplayed()) continue;

            String linkText = link.getText();
            String href = link.getAttribute("href");

            Assertions.assertNotNull(href, "Link has no href attribute");

            // Click the link
            link.click();

            // Handle new tab or same window
            Set<String> windows = webDriver.getWindowHandles();

            for (String window : windows) {
                if (!window.equals(originalWindow)) {
                    webDriver.switchTo().window(window);
                    break;
                }
            }

            String currentUrl = webDriver.getCurrentUrl();

            Assertions.assertNotNull(
                    currentUrl,
                    "Navigation failed for footer link: " + linkText
            );

            // Close external tab if opened
            if (windows.size() > 1) {
                webDriver.close();
                webDriver.switchTo().window(originalWindow);
            }
        }
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
