import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCategorySitemapScenarioBetterVersion {
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
    public void testSitemapCategoryLinks() {
        webDriver.get(baseUrl + "sitemap.html");

        // 1. Verify sitemap page loaded
        WebElement heading = webDriver.findElement(By.tagName("h1"));
        assertEquals("Sitemap", heading.getText(),
                "Sitemap page did not load correctly");

        // 2. Locate main content area
        WebElement mainContent = webDriver.findElement(By.cssSelector("[role='main']"));

        // 3. Get all links inside sitemap
        List<WebElement> links = mainContent.findElements(By.tagName("a"));
        assertFalse(links.isEmpty(), "No links found in sitemap");

        int testedLinks = 0;

        for (WebElement link : links) {
            if (testedLinks >= 15) break; // scope control

            String href = link.getAttribute("href");

            // 4. Filter only internal category links
            if (href == null || !href.startsWith("https://www.temu.com/")) {
                continue;
            }

            // Ignore app download links if any appear
            if (href.contains("apps.apple.com") || href.contains("play.google.com")) {
                continue;
            }

            // 5. Navigate safely
            webDriver.navigate().to(href);

            // 6. Assertions
            assertTrue(webDriver.getCurrentUrl().startsWith("https://"),
                    "Navigation did not use HTTPS: " + href);

            String title = webDriver.getTitle();
            assertNotNull(title, "Page title is null");
            assertFalse(title.isBlank(), "Page title is empty");

            testedLinks++;
        }

        assertTrue(testedLinks > 0, "No sitemap links were tested");
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
