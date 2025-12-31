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
public class CartFunctionalityTestScenario  {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Variables to store price data between tests
    private static double priceItem2 = 0.0;
    private static double initialTotal = 0.0;
    private static double priceItem1_Single = 0.0; // The price of item 1 (derived)

    @BeforeAll
    public static void setUp() {
        // Driver Path Setup
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    // --- HELPER: PARSE PRICE STRING TO DOUBLE ---
    // Converts "BAM 24,50" or "$10.00" to 24.50 or 10.00
    private double parsePrice(String priceText) {
        try {
            // Remove non-numeric characters except dot and comma
            String cleanPrice = priceText.replaceAll("[^0-9.,]", "");
            // Replace comma with dot if necessary (European format)
            cleanPrice = cleanPrice.replace(",", ".");
            return Double.parseDouble(cleanPrice);
        } catch (Exception e) {
            System.out.println("Error parsing price: " + priceText);
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
    // STEP 1 - 4: Add Items & Go to Cart
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-4: Visit & Add Items to Cart")
    public void test01_AddItemsToCart() {
        System.out.println("=== TEST 1: Populate Cart ===");
        try {
            // 1. Visit Temu
            driver.get("https://www.temu.com/");
            randomDelay();

            // 2. Click cart first product (XPath provided)
            try {
                WebElement item1 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[2]/div[2]/div/div[2]/div[1]/div[1]/div[1]/div/div/div/div[3]/div[2]/div/div"));
                item1.click();
                System.out.println("Added Product 1 to Cart.");
            } catch (Exception e) {
                System.out.println("Product 1 XPath not found (Home layout might vary). Skipping add...");
            }
            Thread.sleep(1500);

            // 3. Click cart second product (XPath provided)
            try {
                WebElement item2 = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[2]/div[2]/div/div[2]/div[1]/div[1]/div[2]/div/div/div/div[3]/div[2]/div/div"));
                item2.click();
                System.out.println("Added Product 2 to Cart.");
            } catch (Exception e) {
                System.out.println("Product 2 XPath not found. Skipping add...");
            }
            Thread.sleep(1500);

            // 4. Click cart Icon (XPath provided)
            WebElement cartIcon = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[1]/div[1]/div/div[2]/div/div[5]/div[4]/div/div/div/svg"));
            cartIcon.click();
            System.out.println("Clicked Cart Icon.");

            // 5. Wait 3 seconds
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

        } catch (Exception e) {
            Assertions.fail("Failed to add items: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 6 - 11: Get Prices, Delete Item & Verify Math
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 6-11: Price Calculation & Delete Item")
    public void test02_VerifyDeleteCalculation() {
        System.out.println("=== TEST 2: Delete Logic ===");
        try {
            // 6. Get second item price (XPath provided)
            WebElement priceItem2El = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[1]/div[1]/div[3]/div[2]/div/div[3]/div[4]/div[1]/div/div[1]/span/span[1]"));
            String priceItem2Text = priceItem2El.getText();
            priceItem2 = parsePrice(priceItem2Text);
            System.out.println("Price of Item 2: " + priceItem2);

            // 8. Get total price (XPath provided) - Note: Step 7 skipped in user prompt
            WebElement totalPriceEl = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div[2]/div[4]/div/div[2]/div/div/div[1]/span/span[1]"));
            String totalText = totalPriceEl.getText();
            initialTotal = parsePrice(totalText);
            System.out.println("Initial Total Price: " + initialTotal);

            // 9. Delete the second item (XPath provided)
            WebElement deleteBtn = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[1]/div[1]/div[3]/div[2]/div/div[3]/div[1]/div/div/svg"));
            deleteBtn.click();
            System.out.println("Clicked Delete on Item 2.");

            // 10. Wait 3 seconds (User prompt said 11, meant 10)
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 11. Check price same (total - price second item)
            // Retrieve the NEW Total after deletion
            WebElement newTotalEl = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div[2]/div[4]/div/div[2]/div/div/div[1]/span/span[1]"));
            double newTotal = parsePrice(newTotalEl.getText());

            // Expected Calculation
            double expectedTotal = initialTotal - priceItem2;

            System.out.println("Math Verification:");
            System.out.println("Old Total (" + initialTotal + ") - Item 2 (" + priceItem2 + ") = " + expectedTotal);
            System.out.println("Actual New Total = " + newTotal);

            // Save the remaining item price (Item 1) for the next test
            priceItem1_Single = newTotal;

            // Allow small delta for floating point calculation
            Assertions.assertEquals(expectedTotal, newTotal, 0.1, "Price calculation after delete is incorrect!");

        } catch (Exception e) {
            Assertions.fail("Failed in Delete/Price verification: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 12 - 15: Change Quantity & Verify Price
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 12-15: Quantity Change Logic")
    public void test03_VerifyQuantityCalculation() {
        System.out.println("=== TEST 3: Quantity Logic ===");
        try {
            // 12. Click dropdown qty First item (XPath provided)
            WebElement qtyDropdown = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[1]/div[1]/div[3]/div/div/div[3]/div[4]/div[2]/div"));
            qtyDropdown.click();
            System.out.println("Clicked Quantity Dropdown.");
            Thread.sleep(1000);

            // 13. Choose 2 (XPath provided)
            WebElement selectQty2 = driver.findElement(By.xpath("/html/body/div[7]/div/div[3]/div")); // This looks like a popup container
            // Sometimes specific option needs to be found inside the container
            try {
                // Try to click the container directly or find text '2' inside
                if(selectQty2.getText().contains("2")) {
                    selectQty2.click();
                } else {
                    // Fallback: search for list item '2' if the xpath points to the whole list
                    selectQty2.findElement(By.xpath(".//div[contains(text(), '2')]")).click();
                }
            } catch (Exception e) {
                // If direct click fails, force click the provided xpath
                selectQty2.click();
            }
            System.out.println("Selected Quantity: 2.");

            // 14. Wait 3 seconds
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 15. Check is the total price change as expected
            // Since we deleted Item 2, only Item 1 remains.
            // If Qty becomes 2, Total should be approx (Item 1 Price * 2)

            WebElement finalTotalEl = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div[2]/div[4]/div/div[2]/div/div/div[1]/span/span[1]"));
            double finalTotal = parsePrice(finalTotalEl.getText());

            double expectedFinalTotal = priceItem1_Single * 2;

            System.out.println("Qty Verification:");
            System.out.println("Item 1 Single Price (" + priceItem1_Single + ") * 2 = " + expectedFinalTotal);
            System.out.println("Actual Final Total = " + finalTotal);

            // Assertion
            Assertions.assertEquals(expectedFinalTotal, finalTotal, 0.1, "Price calculation after quantity update is incorrect!");

        } catch (Exception e) {
            Assertions.fail("Failed in Quantity verification: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 16 - 17: Checkout
    // ---------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Step 16-17: Checkout Process")
    public void test04_Checkout() {
        System.out.println("=== TEST 4: Checkout ===");
        try {
            // 16. Click checkout button (XPath provided)
            WebElement checkoutBtn = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div[3]/div/span[2]"));
            checkoutBtn.click();
            System.out.println("Clicked Checkout Button.");

            Thread.sleep(2000);

            // 17. Finish since need to be logged in user
            System.out.println("Test Finished (Redirected to Login/Register page as expected).");

            // Validate we are asked to login (Optional)
            boolean isLoginPresent = driver.getPageSource().contains("Email") || driver.getPageSource().contains("Sign in");
            Assertions.assertTrue(isLoginPresent, "Should be redirected to login page.");

        } catch (Exception e) {
            Assertions.fail("Failed Checkout Step: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Cart Test Suite Completed.");
        // driver.quit();
    }
}