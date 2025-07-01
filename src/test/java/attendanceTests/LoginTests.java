package attendanceTests;

import attendancePages.Login;
import baseTestFile.BaseTests;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import org.testng.annotations.Test;

import java.time.Duration;

public class LoginTests extends BaseTests {

    private final By accessControlMenuBy = By.xpath("//div[@id='tab-HCBAccessControl']//span[normalize-space()='Access Control']");

    @Test(description = "Verify Access Control menu is visible after successful login")
    public void verifyAccessControlMenuVisibleAfterLogin() {
        // Only verify visibility — do not login again
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            boolean isVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(accessControlMenuBy)).isDisplayed();
            Assert.assertTrue(isVisible, "Access Control menu should be visible after successful login.");
        } catch (Exception e) {
            Assert.fail("Access Control menu not visible: " + e.getMessage());
        }
    }
}
