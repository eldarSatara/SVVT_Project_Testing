
import java.time.Duration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestLoginTemu1 {
    private static WebDriver driver;
    private static JavascriptExecutor js;

    @BeforeAll
    public static void setUp() throws Exception {
        // Driver Path (Adjust if it is still in D:)
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testLoginWithCustomXPath() throws Exception {
        // Open Website
        System.out.println("Open Temu...");
        driver.get("https://www.temu.com/");
        Thread.sleep(4000);

        // 2. Click Xpath for cookies
        try {
            System.out.println("Try to click Xpath : //div[3]/div[2]/span");
            WebElement customElement = driver.findElement(By.xpath("//div[3]/div[2]/span"));

            // Force click
            js.executeScript("arguments[0].click();", customElement);
            System.out.println("SUCCESSFUL: The element was clicked.");

            // If that element actually opens the login menu, try clicking "Sign in" inside it
            Thread.sleep(1000);
            try {
                driver.findElement(By.xpath("//div[@id='mainHeader']//div[contains(text(), 'Sign in')]")).click();
                System.out.println("'Sign in' button in dropdown also clicked.");
            } catch (Exception e) {
                // Might go directly to login page, ignore this error
            }

        } catch (Exception e) {
            System.out.println("FAILED to click your chosen element: " + e.getMessage());
        }

        Thread.sleep(2000);

        // 3. FILL EMAIL
        try {
            // Using aria-label selector (Most Stable)
            WebElement emailField = driver.findElement(By.cssSelector("input[aria-label='Email or phone number']"));
            emailField.clear();
            emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
            System.out.println("Email filled.");

            // Click Continue
            try {
                driver.findElement(By.id("submit-button")).click();
            } catch (Exception e) {
                driver.findElement(By.xpath("//button[contains(text(), 'Continue')]")).click();
            }

        } catch (Exception e) {
            System.out.println("Failed to fill email (Login might not be open yet): " + e.getMessage());
        }

        try {
            // find input email / phone
            WebElement emailField = driver.findElement(
                    By.cssSelector("input[aria-label='Email or phone number']")
            );

            String currentValue = emailField.getAttribute("value");

            if (currentValue == null || currentValue.trim().isEmpty()) {
                emailField.clear();
                emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
                System.out.println("Email field was empty → filled.");
            } else {
                System.out.println("Email field already filled → skipped.");
            }

            // Klik Continue
            if (driver.findElements(By.id("submit-button")).size() > 0) {
                driver.findElement(By.id("submit-button")).click();
            } else {
                driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
            }

        } catch (Exception e) {
            System.out.println("Email input not found or page not ready: " + e.getMessage());
        }

        try {
            // find input email / phone
            WebElement emailField = driver.findElement(
                    By.cssSelector("input[aria-label='Email or phone number']")
            );

            String currentValue = emailField.getAttribute("value");

            if (currentValue == null || currentValue.trim().isEmpty()) {
                emailField.clear();
                emailField.sendKeys("rayi.aqli.gemilang@stu.ibu.edu.ba");
                System.out.println("Email field was empty → filled.");
            } else {
                System.out.println("Email field already filled → skipped.");
            }

            // Klik Continue
            if (driver.findElements(By.id("submit-button")).size() > 0) {
                driver.findElement(By.id("submit-button")).click();
            } else {
                driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
            }

        } catch (Exception e) {
            System.out.println("Email input not found or page not ready: " + e.getMessage());
        }



        Thread.sleep(45000);

        System.out.println("Continuing! Filling Password...");

        // 4. FILL PASSWORD
        try {
            WebElement pwdField = driver.findElement(By.xpath("//input[@type='password']"));
            pwdField.clear();
            pwdField.sendKeys("Admin123");
            System.out.println("Password filled.");

            // Click Final Login
            try {
                driver.findElement(By.id("submit-button")).click();
            } catch (Exception e) {
                driver.findElement(By.xpath("//button[contains(text(), 'Sign in') or contains(text(), 'Login')]")).click();
            }
            System.out.println("SUCCESS! Final Login button clicked.");

        } catch (Exception e) {
            System.out.println("Failed to fill password. Ensure password element has appeared.");
        }

        Thread.sleep(50000);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        // driver.quit();
    }
}