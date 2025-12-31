

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

    // Helper: Random Delay
    private void randomDelay() {
        try {
            int delay = 2000 + random.nextInt(2000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {}
    }

    // Helper: Generate Random Name
    private String generateRandomName() {
        String[] names = {"Adnan", "Elvir", "Tarik", "Lejla", "Amra", "Mirza", "Daris", "Iman"};
        return names[random.nextInt(names.length)] + " " + (random.nextInt(100) + 1);
    }

    // ---------------------------------------------------------
    // STEP 1 - 3: Navigate to Profile Settings
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-3: Visit Temu & Go to Profile")
    public void test01_NavigateToProfile() {
        System.out.println("=== TEST 1: Navigate to Profile ===");
        try {
            // 1. Visit Temu
            driver.get("https://www.temu.com/");
            randomDelay();

            // 2. Go to orders and Account (Class: _1MI18fma _2eKJ81QH _2PffkKmv)
            // Using CSS Selector (replace spaces with dots)
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            System.out.println("Clicked Account Icon.");
            randomDelay();

            // 3. Click Profile (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[1]/ul/li[3]")).click();
            System.out.println("Clicked 'Profile' Menu.");

            Thread.sleep(2000);

        } catch (Exception e) {
            Assertions.fail("Navigation failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 4 - 9: Edit Profile (Name & Photo)
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 4-9: Change Name & Photo")
    public void test02_EditProfileDetails() {
        System.out.println("=== TEST 2: Edit Profile Info ===");
        try {
            // 4. Change Name (XPath provided)
            WebElement nameInput = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[2]/div[2]/div/input"));

            String newName = generateRandomName();

            // Clear existing text using keyboard shortcuts
            nameInput.sendKeys(Keys.CONTROL + "a");
            nameInput.sendKeys(Keys.DELETE);
            Thread.sleep(500);

            // Type new name
            nameInput.sendKeys(newName);
            System.out.println("Name changed to: " + newName);
            randomDelay();

            // 6. Change photo (Click icon - XPath provided)
            WebElement photoIcon = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[1]/div[2]/label/svg"));
            photoIcon.click();
            System.out.println("Clicked Photo Upload Icon.");

            // 7. Wait 30 seconds to change manually
            System.out.println(">>> WAITING 30 SECONDS: PLEASE UPLOAD PHOTO MANUALLY NOW <<<");
            Thread.sleep(30000);
            System.out.println(">>> Resuming automation... <<<");

            // 8. Click save to save new photo (XPath provided - likely inside a modal)
            try {
                WebElement savePhotoBtn = driver.findElement(By.xpath("/html/body/div[4]/div/div/div[5]/span"));
                savePhotoBtn.click();
                System.out.println("Clicked 'Save' for Photo.");
                Thread.sleep(2000); // Wait for upload processing
            } catch (Exception e) {
                System.out.println("Save Photo button not found (Maybe you cancelled or saved manually?). Continuing...");
            }

            // 9. Click to save all changes (XPath provided)
            WebElement saveAllBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div[5]/div/span"));
            saveAllBtn.click();
            System.out.println("Clicked 'Save All Changes'.");

        } catch (Exception e) {
            Assertions.fail("Failed to edit profile: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Profile Test Completed.");
        // driver.quit();
    }
}