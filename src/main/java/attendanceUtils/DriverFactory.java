package attendanceUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static WebDriver driver;

    public static WebDriver initDriver() {
        if (driver == null) {

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            boolean isCI = System.getenv("CI") != null;

            if (isCI) {
                System.out.println("CI Mode Detected → Running Chrome in NON-HEADLESS (XVFB) MODE");

                // ❌ REMOVE HEADLESS MODE (Hikvision blocks headless)
                // options.addArguments("--headless=new");

                // 🟦 CI runs inside virtual display → resize only
                options.addArguments("--window-size=1920,1080");

                // 🛡 Remove Selenium footprints (anti-bot bypass)
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.setExperimentalOption("useAutomationExtension", false);

            } else {
                // Local machine → normal visible browser
                System.out.println("Local Mode → Running Normal Chrome");
            }

            // Stability arguments
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);

            if (!isCI) {
                driver.manage().window().maximize();
            }

        }
        return driver;
    }
}
