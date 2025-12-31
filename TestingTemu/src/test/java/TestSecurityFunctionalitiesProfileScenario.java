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
public class TestSecurityFunctionalitiesProfileScenario {

    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Data storage for validation
    private static String savedPhoneNumber = "61234567";
    private static String savedEmail = "";
    private static String oldPassword = "Admin123";

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

    private void randomDelay() {
        try {
            int delay = 2000 + random.nextInt(2000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {}
    }

    // ---------------------------------------------------------
    // STEP 1 - 8: Add Phone Number Validation
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-8: Security - Add Phone Number")
    public void test01_AddPhoneNumber() {
        System.out.println("=== TEST 1: Add Phone Number ===");
        try {
            // 1. Open Temu
            driver.get("https://www.temu.com/");
            randomDelay();

            // 2. Go to orders and Account (CSS Selector)
            // Note: Replaced spaces with dots for CSS class selector
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            System.out.println("Clicked Account Icon.");
            randomDelay();

            // 3. Click Security Menu (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[1]/ul/li[11]/div")).click();
            System.out.println("Clicked 'Security' section.");
            Thread.sleep(2000);

            // 4. Click 'Add phone number' (XPath provided)
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div[2]")).click();
                System.out.println("Clicked 'Add phone number'.");
            } catch (Exception e) {
                System.out.println("Note: 'Add phone number' button might vary if one already exists.");
            }
            Thread.sleep(2000);

            // 5. Input phone number (Save in variable 61234567)
            WebElement phoneInput = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div/div/div[2]/input"));
            phoneInput.clear();
            phoneInput.sendKeys(savedPhoneNumber);
            System.out.println("Inputted Phone: " + savedPhoneNumber);
            randomDelay();

            // 6. Click submit (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div/div/div[2]/input")).click();
            System.out.println("Clicked Submit (Phone).");
            Thread.sleep(3000);

            // 7. Check formatting after +387
            // XPath: /html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/div[3]/span[2]
            WebElement phoneDisplayEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/div[3]/span[2]"));
            String displayedText = phoneDisplayEl.getText(); // e.g. "+387 61 234 567"
            System.out.println("Displayed Verification Phone: " + displayedText);

            // Logic: Remove "+387" and spaces, then compare with savedPhoneNumber
            String cleanNumber = displayedText.replace("+387", "").replace(" ", "").trim();

            Assertions.assertEquals(savedPhoneNumber, cleanNumber, "Phone number numbers do not match input!");
            System.out.println("Phone Verification Passed.");

            // 8. Click cancel to reject adding number (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/div[4]/div[1]/span")).click();
            System.out.println("Clicked Cancel.");
            randomDelay();

        } catch (Exception e) {
            Assertions.fail("Phone Number Test Failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 9 - 13: Email Verification
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 9-13: Security - Verify Email")
    public void test02_VerifyEmail() {
        System.out.println("=== TEST 2: Verify Email ===");
        try {
            // 9. Get Email Information (XPath provided)
            WebElement emailEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[3]/div[1]/div/div[2]/span"));
            savedEmail = emailEl.getText();
            System.out.println("Saved Email from Dashboard: " + savedEmail);

            // 10. Click Edit (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[3]/div[2]/span")).click();
            System.out.println("Clicked Edit Email.");

            // 11. Wait 3 seconds
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 12. Check if email matches (XPath provided)
            WebElement modalEmailEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/div[2]/span[2]"));
            String modalEmail = modalEmailEl.getText();
            System.out.println("Email in Modal: " + modalEmail);

            // Verification
            Assertions.assertEquals(savedEmail, modalEmail, "Email in modal does not match email on dashboard!");
            System.out.println("Email Verification Passed.");

            // 13. Click to cancel (XPath provided - SVG)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[1]/svg")).click();
            System.out.println("Clicked Cancel/Close.");
            randomDelay();

        } catch (Exception e) {
            Assertions.fail("Email Verification Failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 14 - 19: Password Logic
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 14-19: Security - Password Validation")
    public void test03_PasswordValidation() {
        System.out.println("=== TEST 3: Password Validation ===");
        try {
            // 14. Click Edit Password (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[2]/div[2]/div/div[4]/div[2]")).click();
            System.out.println("Clicked Edit Password.");
            Thread.sleep(2000);

            // 15. Input old password = Admin123 (XPath provided)
            WebElement oldPassInput = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div[1]/div[1]/div[2]/input"));
            oldPassInput.sendKeys(oldPassword);
            System.out.println("Inputted Old Password.");

            // 16. Input old password again (New Password field) to check validity (XPath provided)
            WebElement newPassInput = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div[2]/div[2]/input"));
            newPassInput.sendKeys(oldPassword); // Using same password to trigger error
            System.out.println("Inputted New Password (Same as old).");
            randomDelay();

            // 17. Click Submit (XPath provided)
            // Note: The provided XPath looks like the input field, but we follow the request.
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div[1]/div[1]/div[2]/input")).click();
            System.out.println("Clicked Submit (Password).");

            // Wait for validation message
            Thread.sleep(2000);

            // 18. Check for notification "Please don’t use a password you have used before"
            // XPath: /html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div[2]/div[5]/div/text()

            WebElement errorDiv = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[2]/form/div[2]/div[5]/div"));
            String errorText = errorDiv.getText();
            System.out.println("Error Message Found: " + errorText);

            String expectedError = "Please don’t use a password you have used before.";
            Assertions.assertTrue(errorText.contains(expectedError), "Expected error message not found!");

            // 19. Close (XPath provided - SVG)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div[3]/div[2]/div[1]/svg")).click();
            System.out.println("Clicked Close.");

        } catch (Exception e) {
            Assertions.fail("Password Test Failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Security Test Completed.");
        // driver.quit();
    }
}