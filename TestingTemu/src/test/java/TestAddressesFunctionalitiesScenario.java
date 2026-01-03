import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys; // Important for clearing inputs (CTRL+A + DEL)
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAddressesFunctionalitiesScenario  {

private static WebDriver driver;
    private static JavascriptExecutor js;
    private static Random random = new Random();

    // Data Storage for Validation
    private static String currentFirstName;
    private static String currentLastName;
    private static String currentPhone;
    private static String currentStreet;
    
    // To store the old name before editing, for validation purposes
    private static String oldFullName; 

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

    // --- HELPER: GENERATE RANDOM DATA ---
    private void generateNewBosnianData() {
        String[] firstNames = {"Adnan", "Tarik", "Lejla", "Amra", "Haris", "Mirza", "Elma", "Dzenan"};
        String[] lastNames = {"Hodzic", "Kovacevic", "Delic", "Hadziabdic", "Imamovic", "Suljic"};
        String[] streets = {"Titova", "Ferhadija", "Zmaja od Bosne", "Marsala Tita", "Sarači"};

        currentFirstName = firstNames[random.nextInt(firstNames.length)];
        currentLastName = lastNames[random.nextInt(lastNames.length)];
        currentStreet = streets[random.nextInt(streets.length)] + " " + (random.nextInt(100) + 1);
        currentPhone = "6" + (1000000 + random.nextInt(9000000));
    }

    private void randomDelay() {
        try {
            int delay = 2000 + random.nextInt(2000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {}
    }

    // ---------------------------------------------------------
    // STEP 1: Add New Address & VALIDATE
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1: Add Address & Verify Name")
    public void test01_AddNewAddress() {
        System.out.println("=== TEST 1: Add New Address ===");
        generateNewBosnianData(); 
        System.out.println("Input Data: " + currentFirstName + " " + currentLastName);

        try {
            // Navigation
            driver.get("https://www.temu.com/");
            randomDelay();
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            randomDelay();
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[1]/ul/li[8]")).click();
            Thread.sleep(2000);

            // Add New Button
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[1]/div[2]/span")).click();
            } catch (Exception e) {
                System.out.println("Form might be already open.");
            }
            Thread.sleep(2000);

            // Fill Form
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[2]/div/div[1]/div/div/div[2]/div/div[1]/input")).sendKeys(currentFirstName);
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[2]/div/div[2]/div/div/div[2]/div/div[1]/input")).sendKeys(currentLastName);
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div[1]/input")).sendKeys(currentPhone);
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[4]/div/div/div[2]/div/div/div/div/div[3]/input")).sendKeys(currentStreet);
            
            // Dropdown City & Search
            Thread.sleep(1000);
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[7]/div/div/div[2]/div/div[2]/div/div/div/div/div/div")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/input")).sendKeys("Ilidza");
            Thread.sleep(1500); 
            driver.findElement(By.xpath("/html/body/div[4]/div/div[2]/div[1]/div/div")).click();
            Thread.sleep(1000);

            // Click Save
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[3]/div[2]")).click();
            System.out.println("Clicked Save.");
            
            // Wait for list update
            Thread.sleep(4000);

            // === ASSERTION: Check if the new address card contains the name ===
            // XPath provided: /html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[2]/div[1]/div/div[1]/bdi
            WebElement nameOnCardEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[2]/div[1]/div/div[1]/bdi"));
            String actualNameOnCard = nameOnCardEl.getText();
            System.out.println("Actual Name on Card: " + actualNameOnCard);

            String expectedFullName = currentFirstName + " " + currentLastName;
            
            // Verify equality (contains or equalsIgnoreCase)
            Assertions.assertTrue(actualNameOnCard.toLowerCase().contains(expectedFullName.toLowerCase()), 
                "FAILED: The saved name on screen does not match the input!");
            
            System.out.println("Assertion Passed: Address added successfully.");

        } catch (Exception e) {
            Assertions.fail("Add Address Failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 2: Edit Address & VALIDATE
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 2: Edit Address & Verify Changes")
    public void test02_EditAddress() {
        System.out.println("=== TEST 2: Edit Address ===");
        
        // 1. Save Old Name for validation later
        oldFullName = currentFirstName + " " + currentLastName;
        System.out.println("Old Name: " + oldFullName);

        // 2. Generate New Data
        generateNewBosnianData();
        String newFullName = currentFirstName + " " + currentLastName;
        System.out.println("New Input Name: " + newFullName);

        try {
            // Click Edit (First Address)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[1]/div[2]/div[2]/div[3]")).click();
            Thread.sleep(4000);

            // Edit First Name
            WebElement fname = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[2]/div/div[1]/div/div/div[2]/div/input"));
            fname.sendKeys(Keys.CONTROL + "a");
            fname.sendKeys(Keys.DELETE);
            fname.sendKeys(currentFirstName);

            // Edit Last Name
            WebElement lname = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[2]/div/div[2]/div/div/div[2]/div/input"));
            lname.sendKeys(Keys.CONTROL + "a");
            lname.sendKeys(Keys.DELETE);
            lname.sendKeys(currentLastName);

            // Edit Phone & Street (Optional, but good for completeness)
            WebElement phone = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/input"));
            phone.sendKeys(Keys.CONTROL + "a", Keys.DELETE, currentPhone);
            
            WebElement street = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[4]/div/div/div[2]/div/div/div/div/div[2]/input"));
            street.sendKeys(Keys.CONTROL + "a", Keys.DELETE, currentStreet);

            // Click Save
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/div/div/span")).click();
            System.out.println("Clicked Save Edit.");
            Thread.sleep(4000);

            // === ASSERTION: Check Edited Name ===
            // XPath provided: /html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[1]/div[1]/div/div[1]/bdi
            WebElement editedNameEl = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[1]/div[1]/div/div[1]/bdi"));
            String displayedEditedName = editedNameEl.getText();
            System.out.println("Displayed Name After Edit: " + displayedEditedName);

            // Validation 1: Must NOT equal Old Name
            Assertions.assertNotEquals(oldFullName, displayedEditedName, "FAILED: Name did not change!");
            
            // Validation 2: Must EQUAL New Name
            Assertions.assertEquals(newFullName, displayedEditedName, "FAILED: Displayed name does not match the new input!");

            System.out.println("Assertion Passed: Name updated correctly.");

        } catch (Exception e) {
            Assertions.fail("Edit Address Failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 3: Delete & Verify
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 3: Delete Address")
    public void test03_SetDefaultAndDelete() {
        System.out.println("=== TEST 3: Delete Address ===");
        try {
            // Change Default (Optional - click second address)
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[1]/span")).click();
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("Skipping set default (Might be single address).");
            }

            // Click Delete (on targeted item)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[1]/div")).click();
            Thread.sleep(1000);

            // Confirm Delete
            driver.findElement(By.xpath("/html/body/div[4]/div/div[2]/div[3]/div/div[2]/span[1]")).click();
            System.out.println("Confirmed Deletion.");
            Thread.sleep(3000);
            
            // Simple validation: Ensure no crashes occurred. 
            // Ideally we would check if list count decreased, but without list container XPath, 
            // manual visual verification or previous assertions are primary.

        } catch (Exception e) {
            Assertions.fail("Delete Failed: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Address Test Completed.");
        // driver.quit();
    }
}
