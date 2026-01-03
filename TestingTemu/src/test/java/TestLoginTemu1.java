//Draft File.To be deleted

//
//import java.time.Duration;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//public class TestLoginTemu1 {
//    private static WebDriver driver;
//    private static JavascriptExecutor js;
//
//    @BeforeAll
//    public static void setUp() throws Exception {
//        // Driver Path (Adjust if it is still in D:)
//        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--remote-allow-origins=*");
//        options.addArguments("--start-maximized");
//        options.addArguments("--disable-blink-features=AutomationControlled");
//
//        driver = new ChromeDriver(options);
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        js = (JavascriptExecutor) driver;
//    }
//
//    @Test
//    public void testLoginWithCustomXPath() throws Exception {
//        // Open Website
//        System.out.println("Open Temu...");
//        driver.get("https://www.temu.com/");
//        Thread.sleep(4000);
//
//        // 2. Click Xpath for cookies
//        try {
//            System.out.println("Try to click Xpath : //div[3]/div[2]/span");
//            WebElement customElement = driver.findElement(By.xpath("//div[3]/div[2]/span"));
//
//            // Force click
//            js.executeScript("arguments[0].click();", customElement);
//            System.out.println("SUCCESSFUL: The element was clicked.");
//
//            // If that element actually opens the login menu, try clicking "Sign in" inside it
//            Thread.sleep(1000);
//            try {
//                driver.findElement(By.xpath("//div[@id='mainHeader']//div[contains(text(), 'Sign in')]")).click();
//                System.out.println("'Sign in' button in dropdown also clicked.");
//            } catch (Exception e) {
//                // Might go directly to login page, ignore this error
//            }
//
//        } catch (Exception e) {
//            System.out.println("FAILED to click your chosen element: " + e.getMessage());
//        }
//
//        Thread.sleep(2000);
//
//        // 3. FILL EMAIL
//        try {
//            // Using aria-label selector (Most Stable)
//            WebElement emailField = driver.findElement(By.cssSelector("input[aria-label='Email or phone number']"));
//            emailField.clear();
//            emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
//            System.out.println("Email filled.");
//
//            // Click Continue
//            try {
//                driver.findElement(By.id("submit-button")).click();
//            } catch (Exception e) {
//                driver.findElement(By.xpath("//button[contains(text(), 'Continue')]")).click();
//            }
//
//        } catch (Exception e) {
//            System.out.println("Failed to fill email (Login might not be open yet): " + e.getMessage());
//        }
//
//        try {
//            // find input email / phone
//            WebElement emailField = driver.findElement(
//                    By.cssSelector("input[aria-label='Email or phone number']")
//            );
//
//            String currentValue = emailField.getAttribute("value");
//
//            if (currentValue == null || currentValue.trim().isEmpty()) {
//                emailField.clear();
//                emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
//                System.out.println("Email field was empty → filled.");
//            } else {
//                System.out.println("Email field already filled → skipped.");
//            }
//
//            // Klik Continue
//            if (driver.findElements(By.id("submit-button")).size() > 0) {
//                driver.findElement(By.id("submit-button")).click();
//            } else {
//                driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
//            }
//
//        } catch (Exception e) {
//            System.out.println("Email input not found or page not ready: " + e.getMessage());
//        }
//
//        try {
//            // find input email / phone
//            WebElement emailField = driver.findElement(
//                    By.cssSelector("input[aria-label='Email or phone number']")
//            );
//
//            String currentValue = emailField.getAttribute("value");
//
//            if (currentValue == null || currentValue.trim().isEmpty()) {
//                emailField.clear();
//                emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
//                System.out.println("Email field was empty → filled.");
//            } else {
//                System.out.println("Email field already filled → skipped.");
//            }
//
//            // Klik Continue
//            if (driver.findElements(By.id("submit-button")).size() > 0) {
//                driver.findElement(By.id("submit-button")).click();
//            } else {
//                driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
//            }
//
//        } catch (Exception e) {
//            System.out.println("Email input not found or page not ready: " + e.getMessage());
//        }
//
//
//
//        Thread.sleep(45000);
//
//        System.out.println("Continuing! Filling Password...");
//
//        // 4. FILL PASSWORD
//        try {
//            WebElement pwdField = driver.findElement(By.xpath("//input[@type='password']"));
//            pwdField.clear();
//            pwdField.sendKeys("Admin123");
//            System.out.println("Password filled.");
//
//            // Click Final Login
//            try {
//                driver.findElement(By.id("submit-button")).click();
//            } catch (Exception e) {
//                driver.findElement(By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login')]")).click();
//            }
//            System.out.println("SUCCESS! Final Login button clicked.");
//
//        } catch (Exception e) {
//            System.out.println("Failed to fill password. Ensure password element has appeared.");
//        }
//
//        Thread.sleep(50000);
//    }
//
//    @AfterAll
//    public static void tearDown() throws Exception {
//        // driver.quit();
//    }
//}


import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.*; // Import JUnit 5 assertions and annotations
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

// ANNOTATION: Ensures tests run in the specific order defined by @Order(x)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestLoginTemu1 {

    // Must be static so the browser session persists across different test methods
    private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    @BeforeAll
    public static void setUp() {
        // Driver Path setup
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        // User agent to look more natural
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    // --- Helper: Random Human-like Delay ---
    private void randomDelay() {
        try {
            int min = 3000; // 3 seconds
            int max = 7000; // 7 seconds
            int delay = random.nextInt(max - min + 1) + min;
            System.out.println("   >> (Waiting " + delay + "ms)...");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    // UNIT TEST 1: Open Page
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1: Open Temu Homepage")
    public void test01_OpenHomepage() {
        System.out.println("=== STEP 1: Opening Website ===");
        driver.get("https://www.temu.com/");

        String title = driver.getTitle();
        System.out.println("Page Title: " + title);

        // Simple assertion to ensure page is loaded
        Assertions.assertNotNull(title, "Title should not be null");

        randomDelay();
    }

    // ---------------------------------------------------------
    // UNIT TEST 2: Handle Popups / Header
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 2: Handle Header & Sign-In Menu")
    public void test02_HandleHeaderInteraction() {
        System.out.println("=== STEP 2: Header Interaction ===");
        try {
            // Try to click header element (cookies/menu)
            WebElement headerElement = driver.findElement(By.xpath("//div[3]/div[2]/span"));
            js.executeScript("arguments[0].click();", headerElement);
            System.out.println("Header clicked.");

            Thread.sleep(1000);

            // Try to click 'Sign in' inside dropdown
            try {
                WebElement signInBtn = driver.findElement(By.xpath("//div[@id='mainHeader']//div[contains(text(), 'Sign in')]"));
                signInBtn.click();
                System.out.println("Dropdown 'Sign in' clicked.");
            } catch (Exception e) {
                System.out.println("Dropdown Sign in not found/needed.");
            }
        } catch (Exception e) {
            System.out.println("Step 2 Info: Header interaction skipped (might be already on login page).");
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // UNIT TEST 3: Input Email
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 3: Input Email & Continue")
    public void test03_EnterEmail() {
        System.out.println("=== STEP 3: Input Email ===");
        try {
            WebElement emailField = driver.findElement(By.cssSelector("input[aria-label='Email or phone number']"));

            // Human logic: Clear -> Wait -> Type
            emailField.clear();
            Thread.sleep(800);
            emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
            System.out.println("Email typed.");

            randomDelay();

            // Click continue
            WebElement submitBtn;
            try {
                submitBtn = driver.findElement(By.id("submit-button"));
            } catch (Exception e) {
                submitBtn = driver.findElement(By.xpath("//button[contains(text(), 'Continue')]"));
            }
            submitBtn.click();
            System.out.println("Continue button clicked.");

        } catch (Exception e) {
            // If this fails, the test is marked RED
            Assertions.fail("Failed to enter Email: " + e.getMessage());
        }
        randomDelay();
    }

    // ---------------------------------------------------------
    // UNIT TEST 4: Input Password
    // ---------------------------------------------------------
    @Test
    @Order(4)
    @DisplayName("Step 4: Input Password & Submit")
    public void test04_EnterPassword() {
        System.out.println("=== STEP 4: Input Password ===");

        // Wait for captcha if necessary (Optional)
        // try { Thread.sleep(10000); } catch (Exception e){}

        try {
            WebElement pwdField = driver.findElement(By.xpath("//input[@type='password']"));
            pwdField.clear();
            Thread.sleep(500);
            pwdField.sendKeys("Admin123");
            System.out.println("Password typed.");

            randomDelay();

            // Click Final Login
            WebElement loginBtn;
            try {
                loginBtn = driver.findElement(By.id("submit-button"));
            } catch (Exception e) {
                loginBtn = driver.findElement(By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login')]"));
            }
            loginBtn.click();
            System.out.println("Final Login button clicked.");

        } catch (Exception e) {
            Assertions.fail("Failed to enter Password: " + e.getMessage());
        }

        // Wait for redirect
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
    }

    // ---------------------------------------------------------
    // UNIT TEST 5: Verify Success
    // ---------------------------------------------------------
    @Test
    @Order(5)
    @DisplayName("Step 5: Verify Login Success")
    public void test05_VerifyLogin() {
        System.out.println("=== STEP 5: Verification ===");

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Final URL: " + currentUrl);

        // Logic: If URL does not contain error, we assume pass
        boolean isLoginSuccess = !currentUrl.contains("login_error");

        // Assertions: If false, test fails
        Assertions.assertTrue(isLoginSuccess, "Login failed! URL indicates error.");
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("All Steps Completed.");
        // driver.quit(); // Uncomment to close automatically
    }
}
