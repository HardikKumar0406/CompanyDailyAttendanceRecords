package baseTestFile;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import attendancePages.Login;
import attendanceUtils.ConfigReader;
import attendanceUtils.DriverFactory;

public class BaseTests {
    protected static WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void globalSetUp() {
        ConfigReader.loadProperties();
        driver = DriverFactory.initDriver();
        driver.get(ConfigReader.get("base.url"));

        try {
            // Determine if running in CI
            boolean isCI = System.getenv("CI") != null;

            String username = isCI
                ? System.getenv("QA_USERNAME")
                : ConfigReader.get("username");

            String password = isCI
                ? System.getenv("QA_PASSWORD")
                : ConfigReader.get("password");
            
            System.out.println("CI Mode: " + isCI);
            System.out.println("Username being used: " + username);
            // DO NOT log password


            if (username == null || password == null) {
                throw new RuntimeException("Username or password is not set.");
            }

            Login loginPage = new Login(driver);
            loginPage.performLogin(username, password);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void globalTearDown() {
        System.out.println("🔻 @AfterSuite - Closing browser...");
        if (driver != null) {
            driver.quit();
        }
    }
}
