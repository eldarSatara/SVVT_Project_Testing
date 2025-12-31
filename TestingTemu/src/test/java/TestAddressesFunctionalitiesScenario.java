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

    // Data Storage to be used across test methods
    private static String currentFirstName;
    private static String currentLastName;
    private static String currentPhone;
    private static String currentStreet;

    @BeforeAll
    public static void setUp() {
        // Driver Path Setup
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");
        // User Agent to simulate real browser
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
    }

    // --- HELPER: GENERATE RANDOM BOSNIAN DATA ---
    private void generateNewBosnianData() {
        String[] firstNames = {"Adnan", "Tarik", "Lejla", "Amra", "Haris", "Mirza", "Elma", "Dzenan"};
        String[] lastNames = {"Hodzic", "Kovacevic", "Delic", "Hadziabdic", "Imamovic", "Suljic"};
        String[] streets = {"Titova", "Ferhadija", "Zmaja od Bosne", "Marsala Tita", "Sarači"};

        currentFirstName = firstNames[random.nextInt(firstNames.length)];
        currentLastName = lastNames[random.nextInt(lastNames.length)];
        currentStreet = streets[random.nextInt(streets.length)] + " " + (random.nextInt(100) + 1);

        // Phone: Start with 6, total 8-9 digits (e.g., 61xxxxxx)
        // Here we generate '6' + 7 random digits = 8 digits total
        currentPhone = "6" + (1000000 + random.nextInt(9000000));
    }

    // Helper for random small delays
    private void randomDelay() {
        try {
            int delay = 2000 + random.nextInt(2000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {}
    }

    // ---------------------------------------------------------
    // STEP 1 - 13: Add New Address
    // ---------------------------------------------------------
    @Test
    @Order(1)
    @DisplayName("Step 1-13: Add New Address (Bosnia Data)")
    public void test01_AddNewAddress() {
        System.out.println("=== TEST 1: Add New Address ===");
        generateNewBosnianData(); // Generate new random data
        System.out.println("Generated Data: " + currentFirstName + " " + currentLastName + ", Phone: " + currentPhone);

        try {
            // 1. Visit Temu
            driver.get("https://www.temu.com/");
            randomDelay();

            // 2. Go to orders and Account (Class: _1MI18fma _2eKJ81QH _2PffkKmv)
            // Using CSS Selector (replacing spaces with dots)
            driver.findElement(By.cssSelector("._1MI18fma._2eKJ81QH._2PffkKmv")).click();
            System.out.println("Clicked Account Icon.");
            randomDelay();

            // 3. Go to Address section (XPath provided)
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[1]/ul/li[8]")).click();
            System.out.println("Clicked Address Section.");
            Thread.sleep(2000);

            // 4. Press 'Add new address' (XPath provided)
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[1]/div[2]/span")).click();
                System.out.println("Clicked 'Add New Address'.");
            } catch (Exception e) {
                System.out.println("Add New Address button not found or form already open.");
            }

            // 5. Wait 3 seconds
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 6. Input First Name
            WebElement fname = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[2]/div/div[1]/div/div/div[2]/div/div[1]/input"));
            fname.sendKeys(currentFirstName);

            // 7. Input Last Name
            WebElement lname = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[2]/div/div[2]/div/div/div[2]/div/div[1]/input"));
            lname.sendKeys(currentLastName);

            // 8. Input Phone Number
            WebElement phone = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div[1]/input"));
            phone.sendKeys(currentPhone);

            // 9. Input Street and House Number
            WebElement street = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[4]/div/div/div[2]/div/div/div/div/div[3]/input"));
            street.sendKeys(currentStreet);
            System.out.println("Form inputs filled.");
            Thread.sleep(1000);

            // 10. Click dropdown for selecting city and postal code
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div[1]/div/div[7]/div/div/div[2]/div/div[2]/div/div/div/div/div/div")).click();
            Thread.sleep(1000);

            // 11. Search for 'Ilidza'
            WebElement searchInput = driver.findElement(By.xpath("/html/body/div[4]/div/div[1]/input"));
            searchInput.sendKeys("Ilidza");
            Thread.sleep(1500); // Wait for search results

            // 12. Click/Select result
            driver.findElement(By.xpath("/html/body/div[4]/div/div[2]/div[1]/div/div")).click();
            System.out.println("City Selected: Ilidza.");
            Thread.sleep(1000);

            // 13. Click Save
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[3]/div[2]")).click();
            System.out.println("Saved New Address.");

        } catch (Exception e) {
            Assertions.fail("Failed in Add Address Step: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 14 - 20: Edit Address
    // ---------------------------------------------------------
    @Test
    @Order(2)
    @DisplayName("Step 14-20: Edit First Address")
    public void test02_EditAddress() {
        System.out.println("=== TEST 2: Edit Address ===");
        generateNewBosnianData(); // Generate new data for editing
        System.out.println("New Data for Edit: " + currentFirstName + " " + currentLastName);

        try {
            // 14. Wait 4 seconds
            System.out.println("Waiting 4 seconds...");
            Thread.sleep(4000);

            // 15. Select 'Edit' for the first address
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[1]/div[2]/div[2]/div[3]")).click();
            System.out.println("Clicked Edit.");

            // 16. Wait 4 seconds
            System.out.println("Waiting 4 seconds (loading form)...");
            Thread.sleep(4000);

            // 17. Change First Name (Clear and Type)
            WebElement fname = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[2]/div/div[1]/div/div/div[2]/div/input"));
            fname.sendKeys(Keys.CONTROL + "a"); // Select All
            fname.sendKeys(Keys.DELETE);        // Delete
            fname.sendKeys(currentFirstName);

            // 18. Change Last Name (Clear and Type)
            WebElement lname = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[2]/div/div[2]/div/div/div[2]/div/input"));
            lname.sendKeys(Keys.CONTROL + "a");
            lname.sendKeys(Keys.DELETE);
            lname.sendKeys(currentLastName);

            // 19. Change Phone Number
            WebElement phone = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[3]/div/div/div/div[2]/div[2]/div/input"));
            phone.sendKeys(Keys.CONTROL + "a");
            phone.sendKeys(Keys.DELETE);
            phone.sendKeys(currentPhone);

            // 19 (Part 2). Change Street Name
            WebElement street = driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[1]/div/div[4]/div/div/div[2]/div/div/div/div/div[2]/input"));
            street.sendKeys(Keys.CONTROL + "a");
            street.sendKeys(Keys.DELETE);
            street.sendKeys(currentStreet);

            System.out.println("Edit Form Filled.");
            Thread.sleep(1000);

            // 20. Click Save
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div[2]/div/div[2]/div[3]/div/div[2]/div/div/span")).click();
            System.out.println("Saved Edited Address.");

        } catch (Exception e) {
            Assertions.fail("Failed in Edit Address Step: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // STEP 21 - 25: Change Default & Delete
    // ---------------------------------------------------------
    @Test
    @Order(3)
    @DisplayName("Step 21-25: Set Default & Delete")
    public void test03_SetDefaultAndDelete() {
        System.out.println("=== TEST 3: Default & Delete ===");
        try {
            // 21. Wait 3 seconds
            System.out.println("Waiting 3 seconds...");
            Thread.sleep(3000);

            // 22. Change default address by clicking the second address
            // Note: Ensure there is a second address available
            try {
                driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[1]/span")).click();
                System.out.println("Changed Default Address.");
            } catch (Exception e) {
                System.out.println("Failed to set default (Maybe only 1 address exists?): " + e.getMessage());
            }

            // 23. Wait 2 seconds
            Thread.sleep(2000);

            // 24. Delete address by clicking delete icon
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div[2]/div/div/div[2]/div[2]/div/div[2]/div[2]/div[2]/div[2]/div[1]/div")).click();
            System.out.println("Clicked Delete Button.");

            Thread.sleep(1000); // Wait for confirmation modal

            // 25. Confirm deletion
            driver.findElement(By.xpath("/html/body/div[4]/div/div[2]/div[3]/div/div[2]/span[1]")).click();
            System.out.println("Confirmed Deletion.");

        } catch (Exception e) {
            Assertions.fail("Failed in Delete Step: " + e.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Address Test Completed.");
        // driver.quit(); // Uncomment to close browser automatically
    }
}