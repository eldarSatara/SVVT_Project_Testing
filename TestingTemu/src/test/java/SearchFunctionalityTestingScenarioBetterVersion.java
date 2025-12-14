import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchFunctionalityTestingScenarioBetterVersion {

    private void performSearch(String term) {
        webDriver.get(baseUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        WebElement searchInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("searchInput"))
        );

        searchInput.clear();
        searchInput.sendKeys(term);
        searchInput.sendKeys(Keys.ENTER);
    }

    private List<String> getFirstProductTitles(int count) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.EKDT7a3v")
        ));

        List<WebElement> products =
                webDriver.findElements(By.cssSelector("div.EKDT7a3v"));

        List<String> titles = new ArrayList<>();

        for (int i = 0; i < Math.min(count, products.size()); i++) {
            WebElement titleElement =
                    products.get(i).findElement(By.cssSelector("h2"));

            titles.add(titleElement.getText().toLowerCase());
        }

        return titles;
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
    public void testSearchWithBroadTerm() {
        performSearch("usb");

        List<String> titles = getFirstProductTitles(5);

        assertFalse(titles.isEmpty(), "Search returned no results");

        for (String title : titles) {
            assertTrue(
                    title.contains("usb"),
                    "Result does not match search term: " + title
            );
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "usb", "usb c", "usb flash drive" })
    public void testSearchWithMultipleTerms(String term) {
        performSearch(term);

        List<String> titles = getFirstProductTitles(5);

        assertFalse(titles.isEmpty(), "Search returned no results");

        for (String title : titles) {
            assertTrue(
                    title.contains(term.split(" ")[0]),
                    "Result does not align with search term: " + title
            );
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
