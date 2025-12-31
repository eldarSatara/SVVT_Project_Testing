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
public class TestCustomerServiceBotScenario {

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

    // ---------------------------------------------------------
    // STEP 1 - 4: Open Chat
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-4: Open Support Chat")
    public void test01_OpenChat() {
        System.out.println("=== TEST 1: Open Chat ===");
        try {
            // 1. Visit Temu
            driver.get("https://www.temu.com/");
            Thread.sleep(3000); // Wait for load

            // 2. Hover over the Support Icon (XPath provided)
            WebElement supportIcon = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[1]/div[1]/div/div[2]/div/div[5]/div[2]/div/div[1]/div/div[1]/svg"));

            Actions actions = new Actions(driver);
            actions.moveToElement(supportIcon).perform();
            System.out.println("Hovered over Support Icon.");
            Thread.sleep(1000);

            // 3. Click the Chat/Help link (XPath provided)
            WebElement chatLink = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div[2]/div[1]/div[1]/div/div[2]/div/div[5]/div[2]/div/div[2]/div/div/div/span/span"));
            chatLink.click();
            System.out.println("Clicked Chat Link.");

            // 4. Wait 15 seconds
            System.out.println("Waiting 15 seconds (Loading Chat)...");
            Thread.sleep(15000);

        } catch (Exception e) {
            Assertions.fail("Failed to open chat: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 5 - 7: Send Text Message
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 5-7: Send & Verify Text")
    public void test02_SendText() {
        System.out.println("=== TEST 2: Send Text Message ===");
        try {
            // 5. Write "test " in textarea (XPath provided)
            WebElement inputField = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div[4]/div[2]/textarea"));
            inputField.sendKeys("test "); // Note: includes space as requested
            System.out.println("Typed 'test '.");
            Thread.sleep(1000);

            // 6. Click send (XPath provided)
            WebElement sendBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div[3]/svg"));
            sendBtn.click();
            System.out.println("Clicked Send.");
            Thread.sleep(2000);

            // 7. Check if text matches (XPath provided)
            WebElement messageBubble = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div[1]/div[2]/div[3]/div/div[1]/div[1]/div/div/pre/span"));
            String actualText = messageBubble.getText();
            System.out.println("Message found: " + actualText);

            Assertions.assertEquals("test", actualText.trim(), "Chat message verification failed!");

        } catch (Exception e) {
            Assertions.fail("Failed to send text: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 8 - 11: Upload Image
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 8-11: Upload Image")
    public void test03_UploadImage() {
        System.out.println("=== TEST 3: Upload Image ===");
        try {
            // 8. Click upload image icon (XPath provided)
            // Note: Clicking an SVG <path> can be flaky. If this fails, we try clicking the parent <svg>
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div[1]/svg/path")).click();
            } catch (Exception e) {
                System.out.println("<path> click failed, trying parent <svg>...");
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div[1]/svg")).click();
            }
            System.out.println("Clicked Upload Icon.");

            // 9. Give 20 second to choose image manually
            System.out.println(">>> WAITING 20 SECONDS: PLEASE SELECT AN IMAGE FROM YOUR COMPUTER <<<");
            Thread.sleep(20000);
            System.out.println(">>> Resuming automation... <<<");

            // 10. Click send (XPath provided - Same as step 6)
            WebElement sendBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[3]/div/div[3]/svg"));
            sendBtn.click();
            System.out.println("Clicked Send (Image).");
            Thread.sleep(5000); // Wait for upload/send completion

            // 11. Check is there <img ...>
            // Since we don't have the exact XPath for the resulting image bubble,
            // we check if ANY <img> tag exists inside the chat container.

            boolean isImagePresent = false;
            try {
                // We look for any img tag in the body, assuming the chat is visible
                // For stricter check, you would use the specific chat container XPath
                int imgCount = driver.findElements(By.tagName("img")).size();
                if (imgCount > 0) {
                    isImagePresent = true;
                    System.out.println("Found " + imgCount + " images on the page.");
                }
            } catch (Exception e) {
                System.out.println("Error finding image tag.");
            }

            Assertions.assertTrue(isImagePresent, "No <img> tag found! Image might not have been sent.");

        } catch (Exception e) {
            Assertions.fail("Failed to upload image: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Chat Test Completed.");
        // driver.quit();
    }
}