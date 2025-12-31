import java.time.Duration;
import java.util.Random;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginLogOutTestScenario {


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
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    private void randomDelay() {
        try {
            int min = 3000;
            int max = 6000;
            int delay = random.nextInt(max - min + 1) + min;
            System.out.println("   >> (Bot thinking... " + delay + "ms)");
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // STEP 1: Open Website
    @Test
    @Order(1)
    @DisplayName("Step 1: Open Temu Homepage")
    public void test01_OpenHomepage() {
        System.out.println("=== STEP 1: Opening Website ===");
        driver.get("https://www.temu.com/");
        Assertions.assertNotNull(driver.getTitle());
        randomDelay();
    }

    // STEP 2: Handle Header
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

    // STEP 3: Input Email Awal
    @Test
    @Order(3)
    @DisplayName("Step 3: Initial Email Input")
    public void test03_EnterEmail() {
        System.out.println("=== STEP 3: Input Email ===");
        try {
            WebElement emailField = driver.findElement(By.cssSelector("input[aria-label='Email or phone number']"));
            emailField.clear();
            Thread.sleep(800);
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

    // STEP 4: MANUAL CAPTCHA WAIT
    @Test
    @Order(4)
    @DisplayName("Step 4: Manual Wait for CAPTCHA")
    public void test04_ManualCaptchaWait() {
        System.out.println("=== STEP 4: PAUSING FOR MANUAL CAPTCHA (45s) ===");
        try {
            Thread.sleep(45000);
        } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("=== Resume Automation... ===");
    }

    // ---------------------------------------------------------
    // STEP 5: CEK APAKAH DIMINTA EMAIL LAGI? (BARU)
    // ---------------------------------------------------------
    @Test
    @Order(5)
    @DisplayName("Step 5: Re-confirm Email (If Required)")
    public void test05_ReconfirmEmailIfRequired() {
        System.out.println("=== STEP 5: Checking for Email Re-confirmation ===");

        try {
            // Coba cari elemen input dengan XPath spesifik dari user
            WebElement reInputEmail = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div/div/form/div/div[1]/div[2]/div/div[2]/input"));

            // Jika kode sampai sini, berarti elemen DITEMUKAN (Butuh isi ulang)
            System.out.println("(!) System asked for email again. Filling it now...");

            reInputEmail.clear();
            Thread.sleep(500);
            reInputEmail.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");

            randomDelay();

            // Klik tombol confirm dengan class spesifik dari user
            driver.findElement(By.className("loginBtn-2XOuy")).click();
            System.out.println("Re-confirm email button clicked.");

        } catch (Exception e) {
            // Jika elemen tidak ketemu (Error), berarti TIDAK butuh isi ulang
            System.out.println("Element not found. Email re-confirmation NOT required. Skipping to Password...");
        }

        randomDelay();
    }

    // STEP 6: Input Password
    @Test
    @Order(6)
    @DisplayName("Step 6: Input Password & Login")
    public void test06_EnterPassword() {
        System.out.println("=== STEP 6: Input Password ===");
        try {
            // Kita ubah implicit wait jadi lebih lama lagi untuk password
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            WebElement pwdField = driver.findElement(By.xpath("//input[@type='password']"));
            pwdField.clear();
            Thread.sleep(500);
            pwdField.sendKeys("Admin123");
            System.out.println("Password typed.");
            randomDelay();

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
        try { Thread.sleep(5000); } catch (InterruptedException e) {}
    }

    // STEP 7: Verification (DULU STEP 6)
    @Test
    @Order(7)
    @DisplayName("Step 7: Verify Login Success")
    public void test07_VerifyLogin() {
        System.out.println("=== STEP 7: Verification ===");
        String currentUrl = driver.getCurrentUrl();
        boolean isLoginSuccess = !currentUrl.contains("login_error");
        Assertions.assertTrue(isLoginSuccess, "Login failed.");
        randomDelay();
    }

    // STEP 8: LOGOUT (DULU STEP 7)
    @Test
    @Order(8)
    @DisplayName("Step 8: Logout Operation")
    public void test08_Logout() {
        System.out.println("=== STEP 8: Perform Logout ===");

        try {
            WebElement profileMenu = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[1]/div/div/div[2]/div/div[4]/div[1]/div/div[1]/div/div[2]/div[2]"));

            Actions actions = new Actions(driver);
            actions.moveToElement(profileMenu).perform();
            System.out.println("Hovering over profile menu...");

            Thread.sleep(2000);

            try {
                System.out.println("Attempting click via Class Name (_3cYTr7zU)...");
                WebElement logoutBtn = driver.findElement(By.className("_3cYTr7zU"));
                logoutBtn.click();
            } catch (Exception e) {
                System.out.println("Class Name failed, using absolute XPath...");
                driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div/div[1]/div/div/div[2]/div/div[4]/div[1]/div/div[2]/div/div/div/div[1]/div[4]/div[2]/div")).click();
            }

            System.out.println("Logout button clicked successfully.");

        } catch (Exception e) {
            Assertions.fail("Logout failed! Element not found: " + e.getMessage());
        }

        randomDelay();
    }
    @AfterAll
    public static void tearDown() {
        System.out.println("Test Sequence Completed.");
         driver.quit();
    }
}