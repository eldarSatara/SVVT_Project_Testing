

import java.time.Duration;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.*;
        import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions; // Important for Hover

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowedStoreFunctionalitiesScenario {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Variables to store data across steps
    private static String targetStoreName = "";
    private static int initialCartCount = 0;

    @BeforeAll
    public static void setUp() {
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

    // --- HELPER: SWITCH TO NEWEST TAB ---
    private void switchToNewestTab() {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
        }
        System.out.println("Switched to tab: " + driver.getTitle());
    }

    // --- HELPER: GET CART COUNT (SAFE PARSING) ---
    private int getCartCount(String xpath) {
        try {
            // Check if element exists first (avoid waiting 10s if empty)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
            WebElement countEl = driver.findElement(By.xpath(xpath));
            String text = countEl.getText().trim();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Restore wait

            if (text.isEmpty()) return 0;
            return Integer.parseInt(text);
        } catch (Exception e) {
            // Element not found usually means cart is empty (0 items)
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Restore wait
            return 0;
        }
    }

    private void randomDelay() {
        try {
            Thread.sleep(2000 + random.nextInt(2000));
        } catch (InterruptedException e) {}
    }

    // ---------------------------------------------------------
    // STEP 1 - 6: Visit, Navigate Tabs, Follow Store
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-6: Visit Item -> Visit Store -> Follow")
    public void test01_FollowNewStore() {
        System.out.println("=== TEST 1: Follow Store Sequence ===");
        try {
            // 1. Visit Temu
            driver.get("https://www.temu.com/");
            randomDelay();

            // 2. Click 1 item (Opens Tab 2)
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[2]/div[2]/div/div[3]/div[1]/div[1]/div[1]/div/div/div/div[1]")).click();
            System.out.println("Clicked Item.");

            // 3. Switch to New Tab (Item Page)
            switchToNewestTab();
            Thread.sleep(2000);

            // 4. Click the store (Opens Tab 3)
            driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div/div[3]/div/div[1]/a")).click();
            System.out.println("Clicked Store Link.");

            // Switch to Newest Tab again (Store Page)
            switchToNewestTab();
            Thread.sleep(3000);

            // 5. Follow new store (XPath provided)
            try {
                WebElement followBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[2]/div[2]/div/span"));
                followBtn.click();
                System.out.println("Clicked 'Follow' button.");
            } catch (Exception e) {
                System.out.println("Could not click Follow (Maybe already followed?). Continuing...");
            }
            randomDelay();

            // 6. Take store name (XPath provided)
            WebElement storeNameEl = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[1]/div/div[2]/div[1]/div/h1"));
            targetStoreName = storeNameEl.getText().trim();
            System.out.println("Target Store Name: " + targetStoreName);

            // Close the store tabs and return to main (Optional, but good for cleanup)
            // For now, we will just navigate to Home on the current tab to proceed to Step 7
            driver.get("https://www.temu.com/");

        } catch (Exception e) {
            Assertions.fail("Failed in Follow Store sequence: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 7 - 13: Verify Account & Navigation
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 7-13: Verify Store in Account List")
    public void test02_VerifyFollowedList() {
        System.out.println("=== TEST 2: Verify List ===");
        try {
            // 7. Go to orders and Account (CSS Selector)
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            System.out.println("Clicked Account.");

            // 8. Wait 3 seconds
            Thread.sleep(3000);

            // 9. Check if div contains the store name
            WebElement container = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[2]"));
            String containerText = container.getText();
            System.out.println("Container Text Check: " + containerText.contains(targetStoreName));
            Assertions.assertTrue(containerText.contains(targetStoreName), "Store name not found in the followed container!");

            // 10. Get store name from list item
            WebElement listStoreNameEl = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[2]/ul/li[1]/a/div/bdi"));
            String listStoreName = listStoreNameEl.getText().trim();
            System.out.println("List Store Name: " + listStoreName);

            // Compare names
            Assertions.assertEquals(targetStoreName, listStoreName, "Store name in list does not match actual store name.");

            // 11. Test visit store from followed list
            driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[2]/ul/li[1]/a")).click();
            System.out.println("Clicked store from list.");
            Thread.sleep(3000);

            // 12. Verify store name on page matches
            WebElement currentHeaderName = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[1]/div/div/div[1]/div/div[2]/div[1]/div/h1"));
            Assertions.assertEquals(targetStoreName, currentHeaderName.getText().trim(), "Store name verification failed after navigation.");

            // 13. Back to previous page
            driver.navigate().back();
            System.out.println("Navigated Back.");
            Thread.sleep(2000);

        } catch (Exception e) {
            Assertions.fail("Verification failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 14 - 17: See More & Add Item to Cart Verification
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 14-17: Add Item & Check Cart Count")
    public void test03_AddToCartVerification() {
        System.out.println("=== TEST 3: Cart Calculation ===");
        try {
            // 14. Click see more (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div/div[2]/div[2]/div[1]/a")).click();
            System.out.println("Clicked 'See More'.");
            Thread.sleep(2000);

            // 15. Get total cart items (XPath provided)
            String cartCountXpath = "/html/body/div[2]/div/div/div[1]/div/div/div[2]/div/div[5]/div[4]/div/div/div/div/div/span";
            initialCartCount = getCartCount(cartCountXpath);
            System.out.println("Initial Cart Count: " + initialCartCount);

            // 16. Click 'Add 1 item' (XPath provided)
            // Note: This button is usually inside the store product list
            WebElement addBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div[1]/div[2]/div[2]/ul/li[1]/div/div/div/div[3]/div[2]/div/div/svg"));
            addBtn.click();
            System.out.println("Clicked 'Add to Cart' button.");

            // Wait for cart update animation
            Thread.sleep(3000);

            // 17. Check if count matches expectation (Initial + 1)
            int newCartCount = getCartCount(cartCountXpath);
            System.out.println("New Cart Count: " + newCartCount);

            int expectedCount = initialCartCount + 1;
            Assertions.assertEquals(expectedCount, newCartCount, "Cart count did not increase correctly!");

        } catch (Exception e) {
            Assertions.fail("Cart verification failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 18 - 20: Unfollow Store
    // ---------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Step 18-20: Unfollow & Verify Removal")
    public void test04_UnfollowStore() {
        System.out.println("=== TEST 4: Unfollow ===");
        try {
            // 18. Hover over 'Following' button (XPath provided)
            WebElement followingBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div[1]/div[1]/div[4]/div[1]/svg"));

            Actions actions = new Actions(driver);
            actions.moveToElement(followingBtn).perform();
            System.out.println("Hovered over Following button.");
            Thread.sleep(1000);

            // 19. Click Unfollow option (XPath provided)
            WebElement unfollowOption = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div[1]/div[1]/div[4]/div[2]/div/div"));
            unfollowOption.click();
            System.out.println("Clicked 'Unfollow'.");

            // Wait for list to refresh
            Thread.sleep(3000);

            // 20. Check if name changed or store removed
            // Strategy: Check if the text at the specific location still equals the old store name
            try {
                WebElement storeNameLocation = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]"));
                String currentText = storeNameLocation.getText();
                System.out.println("Text after unfollow: " + currentText);

                // Assert that it is NOT the same as the target store name anymore
                // (It might be the next store in the list, or empty)
                Assertions.assertNotEquals(targetStoreName, currentText, "Store name is still present! Unfollow might have failed.");
            } catch (Exception e) {
                // If element is not found, it might mean the list is now empty (which is also a success if it was the only store)
                System.out.println("Store element not found (List might be empty). Unfollow successful.");
            }

        } catch (Exception e) {
            Assertions.fail("Unfollow failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Follow Store Test Completed.");
        // driver.quit();
    }
}