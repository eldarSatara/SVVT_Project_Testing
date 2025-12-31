
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckHistoryScenario {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

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

    // Helper: Random Delay (3-6 seconds)
    private void randomDelay() {
        try {
            int min = 3000; int max = 6000;
            int delay = random.nextInt(max - min + 1) + min;
            System.out.println("   >> (Bot wait... " + delay + "ms)");
            Thread.sleep(delay);
        } catch (InterruptedException e) { e.printStackTrace(); }
    }

    // ---------------------------------------------------------
    // STEP 1 - 3: Navigate to History
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-3: Visit Temu & Go to Browsing History")
    public void test01_NavigateToHistory() {
        System.out.println("=== TEST 1: Navigate to History ===");

        // 1. Visit Temu
        driver.get("https://www.temu.com/");
        System.out.println("Visited Temu Home.");
        randomDelay();

        try {
            // 2. Click Orders & Account (Class: _1MI18fma _2eKJ81QH _2PffkKmv)
            // Note: Since class has spaces, we replace spaces with dots for CSS Selector
            WebElement accountBtn = driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv"));
            accountBtn.click();
            System.out.println("Clicked 'Orders & Account'.");
            randomDelay();

            // 3. Go to Browsing History (Class: item-3mvFT)
            WebElement historyMenu = driver.findElement(By.className("item-3mvFT"));
            historyMenu.click();
            System.out.println("Clicked 'Browsing History' section.");

        } catch (Exception e) {
            Assertions.fail("Failed navigation steps: " + e.getMessage());
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // STEP 4 - 5: New Tab Interaction
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 4-5: Open Item in New Tab & Close")
    public void test02_OpenNewTabAndClose() {
        System.out.println("=== TEST 2: Tab Handling ===");

        String originalWindow = driver.getWindowHandle(); // Store ID tab utama

        try {
            // 4. Click item to open new tab (XPath provided)
            WebElement itemLink = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div[1]/a[1]"));
            itemLink.click();
            System.out.println("Clicked item (Opening new tab).");

            Thread.sleep(3000); // Wait for tab to open

            // Switch to new tab
            Set<String> allWindows = driver.getWindowHandles();
            for (String windowHandle : allWindows) {
                if (!windowHandle.equals(originalWindow)) {
                    driver.switchTo().window(windowHandle);
                    System.out.println("Switched to New Tab.");
                    break;
                }
            }

            // (Optional) Verify item loaded
            randomDelay();

            // 5. Close new tab and back to main tab
            driver.close(); // Close current tab (new tab)
            System.out.println("Closed New Tab.");

            driver.switchTo().window(originalWindow); // Back to main
            System.out.println("Switched back to Main Tab.");

        } catch (Exception e) {
            Assertions.fail("Failed tab handling: " + e.getMessage());
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // STEP 6 - 7: Add to Cart & Wait
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 6-7: Add History Item to Cart")
    public void test03_AddToCart() {
        System.out.println("=== TEST 3: Add to Cart ===");
        try {
            // 6. Click to add item in history to cart (XPath provided)
            WebElement addToCartBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div/div[2]/div/div[1]/div[1]/a[1]/div/div[4]/div[2]"));
            addToCartBtn.click();
            System.out.println("Clicked 'Add to Cart' icon.");

            // 7. Wait 5 seconds (Explicit request)
            System.out.println("Waiting 5 seconds as requested...");
            Thread.sleep(5000);

        } catch (Exception e) {
            Assertions.fail("Failed to add to cart: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 8 - 11: Delete Single Item
    // ---------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Step 8-11: Manage & Delete First Item")
    public void test04_DeleteSingleItem() {
        System.out.println("=== TEST 4: Delete Single Item ===");
        try {
            // 8. Click manage (Class: text-1hWE8 manageButton-jRWa9 visible-OA1pl)
            // Using CSS Selector for multiple classes
            WebElement manageBtn = driver.findElement(By.cssSelector(".text-1hWE8.manageButton-jRWa9.visible-OA1pl"));
            manageBtn.click();
            System.out.println("Clicked 'Manage'.");
            randomDelay();

            // 9. Select first Item (XPath provided)
            WebElement firstItemCheckbox = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div[1]/div[2]/div/div[1]/div[1]/a[1]/div/div[7]/div/span"));
            firstItemCheckbox.click();
            System.out.println("Selected First Item.");
            randomDelay();

            // 10. Action to delete (Class: inner-3cggh)
            WebElement deleteBtn = driver.findElement(By.className("inner-3cggh"));
            deleteBtn.click();
            System.out.println("Clicked 'Delete'.");
            Thread.sleep(1000);

            // 11. Action to make sure delete (XPath provided - usually 'Confirm' button)
            WebElement confirmDelete = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div[2]/div[2]/div/div[4]/div[2]/div/span[2]"));
            confirmDelete.click();
            System.out.println("Confirmed Delete (Single).");

        } catch (Exception e) {
            Assertions.fail("Failed single delete: " + e.getMessage());
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // STEP 12 - 15: Bulk Delete (Select All)
    // ---------------------------------------------------------
    @Test
    @Order(5)
    @DisplayName("Step 12-15: Manage & Delete All")
    public void test05_DeleteAllItems() {
        System.out.println("=== TEST 5: Bulk Delete (Select All) ===");
        try {
            // 12. Click manage (Same as step 8)
            WebElement manageBtn = driver.findElement(By.cssSelector(".text-1hWE8.manageButton-jRWa9.visible-OA1pl"));
            manageBtn.click();
            System.out.println("Clicked 'Manage' again.");
            randomDelay();

            // 13. Click select all (XPath provided)
            WebElement selectAllBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div[2]/div/div/div/div/div[1]/div/span[1]"));
            selectAllBtn.click();
            System.out.println("Clicked 'Select All'.");
            randomDelay();

            // 14. Action to delete (Same as step 10)
            WebElement deleteBtn = driver.findElement(By.className("inner-3cggh"));
            deleteBtn.click();
            System.out.println("Clicked 'Delete'.");
            Thread.sleep(1000);

            // 15. Action to make sure delete (XPath provided - Check index carefully)
            // Note: Step 11 used span[2], Step 15 uses span[1] as per user request
            WebElement confirmDeleteAll = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[2]/div[1]/div[2]/div[2]/div[2]/div/div[4]/div[2]/div/span[1]"));
            confirmDeleteAll.click();
            System.out.println("Confirmed Delete (All).");

        } catch (Exception e) {
            Assertions.fail("Failed bulk delete: " + e.getMessage());
        }
        randomDelay();
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("History Test Completed.");
        // driver.quit();
    }
}