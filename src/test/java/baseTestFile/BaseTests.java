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
            Login loginPage = new Login(driver);
     //       loginPage.acceptCookiesIfPresent(); // ✅ Accept cookies FIRST
            loginPage.performLogin(
                ConfigReader.get("username"),
                ConfigReader.get("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }


    @AfterSuite(alwaysRun = true)
    public void globalTearDown() {
        System.out.println("🔻 @AfterSuite - Closing browser...");
        if (driver != null) {
            driver.close();
        }
    }


}
