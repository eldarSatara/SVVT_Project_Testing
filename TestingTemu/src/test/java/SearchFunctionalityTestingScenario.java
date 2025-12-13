import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class SearchFunctionalityTestingScenario {

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
    public void testSearchFieldFunctionality() {
        webDriver.get(baseUrl);

        WebElement searchInput = webDriver.findElement(By.id("searchInput"));
        searchInput.sendKeys("usb");
        searchInput.sendKeys(Keys.RETURN);

        // Wait for results to appear (simple implicit wait or explicit wait can be added)
        List<WebElement> results = webDriver.findElements(By.cssSelector(".product-card-title"));

        Assertions.assertFalse(results.isEmpty(), "No search results were found.");

        String firstResultTitle = results.get(0).getText();
        Assertions.assertNotNull(firstResultTitle, "First result title is null.");
    }

    @Test
    public void testSearchTermImpact() {
        webDriver.get(baseUrl);

        // First search
        WebElement searchInput = webDriver.findElement(By.id("searchInput"));
        searchInput.sendKeys("usb");
        searchInput.sendKeys(Keys.RETURN);

        List<WebElement> results1 = webDriver.findElements(By.cssSelector(".product-card-title"));
        String firstResult1 = results1.get(0).getText();

        // Second search
        webDriver.get(baseUrl);
        searchInput = webDriver.findElement(By.id("searchInput"));
        searchInput.sendKeys("usb flash drive");
        searchInput.sendKeys(Keys.RETURN);

        List<WebElement> results2 = webDriver.findElements(By.cssSelector(".product-card-title"));
        String firstResult2 = results2.get(0).getText();

        Assertions.assertNotEquals(firstResult1, firstResult2, "First results did not change between search terms.");
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
