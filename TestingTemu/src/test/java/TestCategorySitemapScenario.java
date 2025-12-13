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

public class TestCategorySitemapScenario {
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
    public void testSitemapPageLoads() {
        webDriver.get("https://www.temu.com/sitemap.html");

        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(
                currentUrl.startsWith("https://"),
                "Sitemap page is not served over HTTPS."
        );
    }

    @Test
    public void testSitemapContainsLinks() {
        webDriver.get("https://www.temu.com/sitemap.html");

        // Find all links on sitemap page
        List<WebElement> links = webDriver.findElements(By.tagName("a"));

        Assertions.assertFalse(
                links.isEmpty(),
                "No links were found on the sitemap page."
        );
    }

    @Test
    public void testSitemapLinksNavigation() {

        webDriver.get("https://www.temu.com/sitemap.html");

        List<WebElement> links = webDriver.findElements(By.tagName("a"));
        String originalWindow = webDriver.getWindowHandle();

        int testedLinks = 0;
        int maxLinksToTest = 5; // limit scope deliberately

        for (WebElement link : links) {

            if (testedLinks >= maxLinksToTest) break;
            if (!link.isDisplayed()) continue;

            String href = link.getAttribute("href");
            if (href == null || href.isEmpty()) continue;

            link.click();

            // Handle possible new tab
            for (String window : webDriver.getWindowHandles()) {
                if (!window.equals(originalWindow)) {
                    webDriver.switchTo().window(window);
                    break;
                }
            }

            String currentUrl = webDriver.getCurrentUrl();
            Assertions.assertNotNull(
                    currentUrl,
                    "Navigation failed for sitemap link: " + href
            );

            // Close tab if needed
            if (webDriver.getWindowHandles().size() > 1) {
                webDriver.close();
                webDriver.switchTo().window(originalWindow);
            }

            testedLinks++;
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
