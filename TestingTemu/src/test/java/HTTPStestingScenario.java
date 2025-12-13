import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HTTPStestingScenario {
    private static WebDriver webDriver;
    private static String baseUrl;

    //Set up the Selenium WebDriver:
    @BeforeAll
    public static void setUp() {
        System.setProperty(
                "webdriver.chrome.driver",
                "C:\\Users\\Eldar\\Desktop\\IBU\\SVVT\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe"
        );

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        webDriver = new ChromeDriver(options);
        baseUrl = "https://www.temu.com/";
    }

    //1: Verify HTTPS access:
    @Test
    public void testHttpsAccess() {
        webDriver.get(baseUrl);

        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(
                currentUrl.startsWith("https://"),
                "The website is not using HTTPS. Current URL: " + currentUrl
        );
    }

    //2: Verify HTTP redirects to HTTPS:
    @Test
    public void testHttpRedirectsToHttps() {
        webDriver.get("http://www.temu.com");

        String currentUrl = webDriver.getCurrentUrl();
        Assertions.assertTrue(
                currentUrl.startsWith("https://"),
                "HTTP did not redirect to HTTPS. Current URL: " + currentUrl
        );
    }

    //Closing the browser:
    @AfterAll
    public static void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
