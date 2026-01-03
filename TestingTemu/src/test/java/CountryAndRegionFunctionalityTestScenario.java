import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CountryAndRegionFunctionalityTestScenario {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Variables to store prices for comparison
    private static double priceInBAM = 0.0;
    private static double priceInEUR = 0.0;

    // The expected conversion rate per user request: 1 Euro = 1.99 KM
    private static final double EXCHANGE_RATE = 1.99;

    @BeforeAll
    public static void setUp() {
        // Driver Path Setup
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        // User Agent
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    // --- HELPER: PARSE PRICE STRING ---
    // Handles formats like "KM 19.50" or "€ 10,20"
    private double parsePrice(String priceText) {
        try {
            // Remove everything that isn't a number, dot, or comma
            String cleanPrice = priceText.replaceAll("[^0-9.,]", "");
            // Replace comma with dot (for European formats)
            cleanPrice = cleanPrice.replace(",", ".");
            return Double.parseDouble(cleanPrice);
        } catch (Exception e) {
            System.out.println("Error parsing price '" + priceText + "': " + e.getMessage());
            return 0.0;
        }
    }

    private void randomDelay() {
        try {
            int delay = 2000 + random.nextInt(2000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {}
    }

    // ---------------------------------------------------------
    // STEP 1 - 8: Change Language to Bosnian
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-8: Change Language & Verify")
    public void test01_ChangeLanguage() {
        System.out.println("=== TEST 1: Change Language ===");
        try {
            // 1. Open Temu
            driver.get("https://www.temu.com/");
            randomDelay();

            // 2. Go to Orders and Account (CSS Selector)
            // Replacing spaces with dots for compound class
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            System.out.println("Clicked Account Icon.");
            Thread.sleep(2000);

            // 3. Go to Country/Region & Language (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/ul/li[9]/div")).click();
            System.out.println("Clicked 'Country/Region & Language'.");
            Thread.sleep(2000);

            // 4. Take sample word 'Language' (XPath provided)
            WebElement labelEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div/div/div[2]/div[1]"));
            String originalLabel = labelEl.getText();
            System.out.println("Original Label: " + originalLabel);

            // 5. Click dropdown language options (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div/div/div[2]/div[2]")).click();
            System.out.println("Clicked Language Dropdown.");
            Thread.sleep(1000);

            // 6. Click Bosanski option (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div/div/div[2]/div[3]/div[4]")).click();
            System.out.println("Selected 'Bosanski'.");

            // 7. Wait 3 seconds for change
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 8. Check if label is now 'Jezik' (XPath provided)
            // Re-finding element to avoid StaleElementReferenceException after page refresh
            WebElement updatedLabelEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div/div/div[2]/div[1]"));
            String updatedLabel = updatedLabelEl.getText();
            System.out.println("Updated Label: " + updatedLabel);

            Assertions.assertEquals("Jezik", updatedLabel, "Language label did not change to 'Jezik'!");

        } catch (Exception e) {
            Assertions.fail("Language Change Failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 9 - 10: Get Price in BAM (History)
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 9-10: Get Price in BAM")
    public void test02_GetPriceInBAM() {
        System.out.println("=== TEST 2: Capture BAM Price ===");
        try {
            // 9. Click History part (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div/div[1]/div/ul/li[7]/div")).click();
            System.out.println("Clicked History.");
            Thread.sleep(2000);

            // 10. Take price from first item (XPath provided)
            WebElement priceEl = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div[1]/a[1]/div/div[4]/div[1]/div[1]/div/span[2]"));
            String priceText = priceEl.getText();
            System.out.println("Price in BAM text: " + priceText);

            priceInBAM = parsePrice(priceText);
            System.out.println("Parsed BAM Price: " + priceInBAM);

            Assertions.assertTrue(priceInBAM > 0, "Failed to capture a valid price in BAM.");

        } catch (Exception e) {
            Assertions.fail("Failed to capture BAM price: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 11 - 15: Change Currency to Euro & Compare
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 11-15: Change Currency & Verify Rate")
    public void test03_ChangeCurrencyAndCompare() {
        System.out.println("=== TEST 3: Change Currency to Euro ===");
        try {
            // 11. Click Country/Region & Language (XPath provided)
            // Note: Accessing from side menu inside History page
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/ul/li[9]/div")).click();
            System.out.println("Clicked Settings (Region/Language).");
            Thread.sleep(2000);

            // 12. Click currency dropdown (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div/div/div[3]/div[2]/div/div")).click();
            System.out.println("Clicked Currency Dropdown.");
            Thread.sleep(1000);

            // 13. Click to change to Euro (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div/div/div[3]/div[2]/div[2]/div[2]")).click();
            System.out.println("Selected Euro.");

            // 14. Wait 3 seconds
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 15 (First). Click History part (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div/div[1]/div/ul/li[7]/div")).click();
            System.out.println("Clicked History again.");
            Thread.sleep(2000);

            // 15 (Second). Take price and check verification
            WebElement priceEl = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div[1]/a[1]/div/div[4]/div[1]/div[1]/div/span[2]"));
            String priceText = priceEl.getText();
            System.out.println("Price in EUR text: " + priceText);

            priceInEUR = parsePrice(priceText);
            System.out.println("Parsed EUR Price: " + priceInEUR);

            // MATH CHECK: 1 Euro = 1.99 KM
            // Therefore: EUR = BAM / 1.99
            double expectedEUR = priceInBAM / EXCHANGE_RATE;

            System.out.println("Calculation Check:");
            System.out.println("BAM (" + priceInBAM + ") / " + EXCHANGE_RATE + " = " + expectedEUR);
            System.out.println("Actual EUR = " + priceInEUR);

            // Allow a small delta (e.g., 0.1) for rounding differences
            Assertions.assertEquals(expectedEUR, priceInEUR, 0.5, "Currency conversion is not within expected range (1 EUR = 1.99 KM).");

        } catch (Exception e) {
            Assertions.fail("Currency Conversion Test Failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Region & Language Test Completed.");
        // driver.quit();
    }
}
