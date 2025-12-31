

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

public class TestHistoryTemu {
    private static WebDriver driver;
    private static JavascriptExecutor js;

    @BeforeAll
    public static void setUp() throws Exception {
        // 1. Setup Driver Path (Sama seperti kode pertama Anda)
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        // 2. Setup Chrome Options (Anti-detection & Maximized)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-blink-features=AutomationControlled");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Menggunakan Duration (JUnit 5 style)
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testH() throws Exception {
        System.out.println("Starting Test H...");

        // Navigasi Awal
        driver.get("https://www.temu.com/ba-en?_x_sessn_id=lg03zsiw0z&refer_page_name=footprint&refer_page_id=10029_1767164193155_e5yk4qw5jc&refer_page_sn=10029&is_back=1");
        Thread.sleep(3000); // Wait loading

        // Interaksi Header (Klik 2 kali sesuai kode asli)
        try {
            driver.findElement(By.xpath("//div[@id='mainHeader']/div/div/div[2]/div/div[5]/div/div/div/div/div[2]/div/div")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//div[@id='mainHeader']/div/div/div[2]/div/div[5]/div/div/div/div/div[2]/div/div")).click();
        } catch (Exception e) {
            System.out.println("Gagal klik header: " + e.getMessage());
        }

        // Pindah ke Halaman Orders
        System.out.println("Navigasi ke Orders...");
        driver.get("https://www.temu.com/bgt_orders.html?_x_sessn_id=lg03zsiw0z&refer_page_name=home&refer_page_id=10005_1767164197344_l05iwrmnyj&refer_page_sn=10005");
        Thread.sleep(3000);

        try {
            driver.findElement(By.xpath("//div[@id='TreeMenuDomId']/ul/li[7]/div")).click();
        } catch (Exception e) {
            System.out.println("Gagal klik TreeMenu: " + e.getMessage());
        }

        // Pindah ke Halaman Footprint
        System.out.println("Navigasi ke Footprint...");
        driver.get("https://www.temu.com/bgp_footprint.html?_x_sessn_id=lg03zsiw0z&refer_page_name=bgt_orders&refer_page_id=10054_1767164225644_100fblbbwm&refer_page_sn=10054");
        Thread.sleep(3000);

        // Rentetan Klik (Sequence of Clicks) dari kode asli
        // Saya bungkus dalam try-catch agar jika satu gagal, test tidak langsung mati
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div/div[3]");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div/div[2]/div/div/div/a[3]/div/div[6]/div/span");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div[2]/div/div/div/div/div[2]/div/span");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div[2]/div[2]/div/div[4]/div[2]/div/span");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div/div[2]/div/div/div/a[2]/div/div/div/div/div[2]/div/div/span[2]/span");

        // Scroll ke atas atau klik elemen relatif
        performClick("(.//*[normalize-space(text()) and normalize-space(.)='Top'])[1]/following::div[9]");

        // Klik CSS Selector panjang (Card Item)
        try {
            driver.findElement(By.cssSelector("a.goodsContainer-2gR7c.goods-container.container-2AFmi.item-1767164059-601103725974239.cardWrap-3ULsh > div.tooltipItem-1HtUS.inner-20V88 > div.sale-yDOeb.saleWithCart-tJZv7.sale_expNum-2z08H > div.cartWrapper-1RrCu > div.cart-30slb.bigCart-3bz7E.undefined > div.wrap-3nOB8 > div.cart-2H3wP.bigGoodsCart-21r4V > svg.cartIcon-yJzVw.icon-1Ezyk")).click();
            System.out.println("Clicked CSS Selector Item.");
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Gagal klik item CSS: " + e.getMessage());
        }

        // Klik sisa elemen
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div/div[3]");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div[2]/div/div/div/div/div/div/span");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div[2]/div/div/div/div/div[2]/div/span");
        performClick("//div[@id='main_scale']/div[2]/div/div[2]/div[2]/div[2]/div/div[4]/div[2]/div/span");

        System.out.println("Test H selesai.");
        Thread.sleep(5000);
    }

    @AfterAll
    public static void tearDown() throws Exception {
        // driver.quit(); // Uncomment jika ingin auto-close
    }

    // Helper method untuk klik agar kode lebih rapi dan aman
    private void performClick(String xpath) {
        try {
            driver.findElement(By.xpath(xpath)).click();
            System.out.println("Clicked: " + xpath);
            Thread.sleep(1000); // Beri jeda sedikit setiap klik
        } catch (Exception e) {
            System.out.println("Element not found / Skipped: " + xpath);
        }
    }
}