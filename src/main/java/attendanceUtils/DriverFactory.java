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

            // Check if running in CI environment
            boolean isCI = System.getenv("CI") != null;

            if (isCI) {
                options.addArguments("--headless=new");
                // ✅ Use a unique temporary user data directory to avoid session conflict
                options.addArguments("--user-data-dir=/tmp/chrome-" + System.currentTimeMillis());
                options.addArguments("--window-size=1920,1080");
            }


            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);

            // Only maximize if NOT in CI (maximizing in headless mode causes no effect or issues)
            if (!isCI) {
                driver.manage().window().maximize();
            }
        }
        return driver;
    }
}
