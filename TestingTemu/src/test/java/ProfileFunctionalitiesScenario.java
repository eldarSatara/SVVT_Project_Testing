

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.*;
        import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys; // Needed for clearing inputs
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileFunctionalitiesScenario {

private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Store names for validation
    private static String oldNameOnScreen;
    private static String newGeneratedName;

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

    private void randomDelay() {
        try {
            int delay = 2000 + random.nextInt(2000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {}
    }

    // Generate Random Name (e.g., "Adnan 45")
    private String generateRandomName() {
        String[] names = {"Adnan", "Elvir", "Tarik", "Lejla", "Amra", "Mirza", "Daris", "Iman"};
        return names[random.nextInt(names.length)] + " " + (random.nextInt(100) + 1);
    }

    // ---------------------------------------------------------
    // STEP 1: Navigate to Profile
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1: Navigate to Profile")
    public void test01_NavigateToProfile() {
        System.out.println("=== TEST 1: Navigate to Profile ===");
        try {
            driver.get("https://www.temu.com/");
            randomDelay();

            // Click Account Icon
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            randomDelay();

            // Click Profile Menu
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[1]/ul/li[3]")).click();
            System.out.println("Clicked 'Profile' Menu.");
            Thread.sleep(2000);

        } catch (Exception e) {
            Assertions.fail("Navigation failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 2: Edit Name, Photo & VALIDATE
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 2: Change Name/Photo & Validate")
    public void test02_EditProfileAndValidate() {
        System.out.println("=== TEST 2: Edit & Validate ===");
        try {
            // 1. CAPTURE OLD NAME (Before Edit)
            // Using the XPath you provided: /html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div[2]/div[1]/div/span
            WebElement nameEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div[2]/div[1]/div/span"));
            oldNameOnScreen = nameEl.getText();
            System.out.println("Old Name on Screen: " + oldNameOnScreen);

            // 2. Generate New Name
            newGeneratedName = generateRandomName();
            System.out.println("New Name to Input: " + newGeneratedName);

            // 3. Input New Name
            WebElement nameInput = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div[2]/div/input"));
            nameInput.sendKeys(Keys.CONTROL + "a");
            nameInput.sendKeys(Keys.DELETE);
            Thread.sleep(500);
            nameInput.sendKeys(newGeneratedName);
            randomDelay();

            // 4. Change Photo (Manual Wait)
            WebElement photoIcon = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[1]/div[2]/label/svg"));
            photoIcon.click();
            System.out.println(">>> WAITING 30 SECONDS: UPLOAD PHOTO MANUALLY <<<");
            Thread.sleep(30000); 

            // 5. Click Save Photo (If modal exists)
            try {
                driver.findElement(By.xpath("/html/body/div[4]/div/div/div[5]/span")).click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Save Photo button skipped.");
            }

            // 6. Click Save All Changes
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[5]/div/span")).click();
            System.out.println("Clicked 'Save All'.");
            
            // WAIT for server update & page refresh
            System.out.println("Waiting 5 seconds for update...");
            Thread.sleep(5000); 

            // 7. CAPTURE NEW NAME (After Edit)
            // Re-find element to avoid StaleElementReferenceException
            WebElement updatedNameEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div[2]/div[1]/div/span"));
            String currentNameOnScreen = updatedNameEl.getText();
            System.out.println("Current Name on Screen: " + currentNameOnScreen);

            // === ASSERTIONS (VALIDATION) ===

            // Validation 1: NOT EQUAL (Name must change)
            Assertions.assertNotEquals(oldNameOnScreen, currentNameOnScreen, 
                "FAILED: The name on screen did not change! (Old: " + oldNameOnScreen + ", Current: " + currentNameOnScreen + ")");
            
            // Validation 2: EQUAL (Name must match input)
            Assertions.assertEquals(newGeneratedName, currentNameOnScreen, 
                "FAILED: The displayed name does not match the input! (Expected: " + newGeneratedName + ", Actual: " + currentNameOnScreen + ")");

            System.out.println("Assertions Passed: Name updated successfully.");

        } catch (Exception e) {
            Assertions.fail("Profile Edit Failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Profile Test Completed.");
        // driver.quit();
    }
}
