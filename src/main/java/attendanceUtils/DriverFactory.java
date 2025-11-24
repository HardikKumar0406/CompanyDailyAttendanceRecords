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
                System.out.println("CI Mode Detected → Running Chrome in HEADLESS MODE");

                // Headless required for GitHub Actions CI
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");

                // Anti-detection / Selenium footprints
                options.addArguments("--disable-blink-features=AutomationControlled");
                options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                options.setExperimentalOption("useAutomationExtension", false);

            } else {
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
