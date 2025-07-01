package attendanceUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static WebDriver driver;

    public static WebDriver initDriver() {
        if (driver == null) {
            // auto-manage driver binary
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            // allow DevTools WebSocket
            options.addArguments("--remote-allow-origins=*");
           //  sandbox flags for Linux
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }
}
