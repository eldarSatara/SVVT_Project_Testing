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

public class TestNavigationInFooterBetterVersion {

    private boolean isSocialMediaLink(String url) {
        return url.contains("instagram.com")
                || url.contains("facebook.com")
                || url.contains("x.com")
                || url.contains("twitter.com")
                || url.contains("tiktok.com")
                || url.contains("youtube.com")
                || url.contains("pinterest.com");
    }

    private boolean isAppStoreLink(String url) {
        return url.contains("apps.apple.com")
                || url.contains("play.google.com");
    }

    private boolean isInternalTemuLink(String url) {
        return url.startsWith(baseUrl) || url.startsWith("/");
    }

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
    public void testFooterInternalNavigationLinks() {
        webDriver.get(baseUrl);

        WebElement footer = webDriver.findElement(By.tagName("footer"));
        List<WebElement> links = footer.findElements(By.tagName("a"));

        for (WebElement link : links) {
            String href = link.getAttribute("href");

            if (href == null || href.isEmpty()) {
                continue;
            }

            // Ignore external and app store links
            if (isSocialMediaLink(href) || isAppStoreLink(href)) {
                continue;
            }

            if (isInternalTemuLink(href)) {

                String targetUrl = href.startsWith("/")
                        ? baseUrl + href.substring(1)
                        : href;

                webDriver.navigate().to(targetUrl);

                String currentUrl = webDriver.getCurrentUrl();

                assertTrue(
                        currentUrl.startsWith("https://www.temu.com"),
                        "Internal footer link did not load correctly: " + href
                );
            }
        }
    }

    @Test
    public void testFooterSocialMediaLinks() {
        webDriver.get(baseUrl);

        WebElement footer = webDriver.findElement(By.tagName("footer"));
        List<WebElement> links = footer.findElements(By.tagName("a"));

        for (WebElement link : links) {
            String href = link.getAttribute("href");

            if (href == null || href.isEmpty()) {
                continue;
            }

            if (isSocialMediaLink(href)) {

                assertTrue(
                        href.startsWith("https://"),
                        "Social media link is not HTTPS: " + href
                );

                assertTrue(
                        href.contains("temu"),
                        "Social media link does not reference Temu: " + href
                );
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
