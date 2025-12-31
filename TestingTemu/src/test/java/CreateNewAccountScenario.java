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
public class CreateNewAccountScenario {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Constant for the password
    private static final String REG_PASSWORD = "Admin123";

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

    // ---------------------------------------------------------
    // STEP 1: Open Website (Same as Login)
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1: Open Temu Homepage")
    public void test01_OpenHomepage() {
        System.out.println("=== STEP 1: Opening Website ===");
        driver.get("https://www.temu.com/");
        Assertions.assertNotNull(driver.getTitle());
        randomDelay();
    }

    // ---------------------------------------------------------
    // STEP 2: Handle Header (Same as Login)
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 2: Handle Header & Menu")
    public void test02_HandleHeaderInteraction() {
        System.out.println("=== STEP 2: Header Interaction ===");
        try {
            WebElement headerElement = driver.findElement(By.xpath("//div[3]/div[2]/span"));
            js.executeScript("arguments[0].click();", headerElement);
            Thread.sleep(1000);
            try {
                driver.findElement(By.xpath("//div[@id='mainHeader']//div[contains(text(), 'Sign in')]")).click();
            } catch (Exception e) {}
        } catch (Exception e) {
            System.out.println("Info: Header interaction skipped.");
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // STEP 3: Initial Email Input (Same as Login)
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 3: Initial Email Input")
    public void test03_EnterEmail() {
        System.out.println("=== STEP 3: Input Email ===");
        try {
            WebElement emailField = driver.findElement(By.cssSelector("input[aria-label='Email or phone number']"));
            emailField.clear();
            Thread.sleep(800);
            // Note: Ensure this email is NOT registered if you want to test Registration flow.
            // If it is already registered, Temu will treat this as Login.
            emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
            System.out.println("Email typed.");
            randomDelay();

            WebElement submitBtn;
            try {
                submitBtn = driver.findElement(By.id("submit-button"));
            } catch (Exception e) {
                submitBtn = driver.findElement(By.xpath("//button[contains(text(), 'Continue')]"));
            }
            submitBtn.click();
            System.out.println("Continue button clicked.");

        } catch (Exception e) {
            Assertions.fail("Failed to enter Email: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 4: Input Password (Specific Registration XPath)
    // ---------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Step 4: Input Registration Password")
    public void test04_InputRegisterPassword() {
        System.out.println("=== STEP 4: Input Password ===");
        try {
            // Wait a bit for the password modal to appear
            Thread.sleep(2000);

            // XPath provided by user for Password Input
            WebElement passField = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div/div[4]/div[1]/form/div/div[2]/div[2]/input"));

            passField.clear();
            passField.sendKeys(REG_PASSWORD);
            System.out.println("Password 'Admin123' typed.");

        } catch (Exception e) {
            Assertions.fail("Failed to find Password Input (XPath might be dynamic or modal didn't appear): " + e.getMessage());
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // STEP 5: Click Register Button
    // ---------------------------------------------------------
    @Test
    @Order(5)
    @DisplayName("Step 5: Click Register Button")
    public void test05_ClickRegister() {
        System.out.println("=== STEP 5: Submit Registration ===");
        try {
            // XPath provided by user for Submit Button
            WebElement registerBtn = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div/div[4]/div[1]/form/div/div[3]/button"));

            registerBtn.click();
            System.out.println("Register button clicked.");

        } catch (Exception e) {
            Assertions.fail("Failed to click Register button: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 6: Manual Verification Wait
    // ---------------------------------------------------------
    @Test
    @Order(6)
    @DisplayName("Step 6: Wait for Manual Code Entry")
    public void test06_ManualVerificationWait() {
        System.out.println("=================================================");
        System.out.println("=== STEP 6: PAUSING FOR MANUAL VERIFICATION ===");
        System.out.println(">>> WAITING 45 SECONDS <<<");
        System.out.println(">>> PLEASE CHECK EMAIL & ENTER CODE MANUALLY <<<");
        System.out.println("=================================================");

        try {
            // Wait 45 seconds as requested
            Thread.sleep(45000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=== Registration Test Sequence Finished ===");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Test Completed.");
        // driver.quit();
    }
}