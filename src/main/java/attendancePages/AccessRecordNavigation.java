package attendancePages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccessRecordNavigation {
    private WebDriver driver;
    private WebDriverWait wait;

    // Relevant WebElements for navigation
    @FindBy(xpath = "(//i[@class='el-submenu__icon-arrow h-icon-angle_down_sm'])[2]")
    private WebElement clickOnSearch;

    @FindBy(xpath = "(//span[@id='s_menu_access_personnelsearch'])[1]")
    private WebElement clickOnAccessRecordRetrieval;

    @FindBy(xpath = "(//span[@class='pointer'])[1]")
    private WebElement clickOnMore;

    @FindBy(xpath = "(//input[@placeholder='Please Select'])[3]")
    private WebElement clickOnChooseAccessStatus;

    @FindBy(xpath = "//span[@class='item-text-style' and text()='Access Granted']")
    private WebElement selectAccessGrantedOption;

    @FindBy(xpath = "(//div[@class='el-button-slot-wrapper'][normalize-space()='Search'])[1]")
    private WebElement searchButton;

    @FindBy(xpath = "(//i[@class='h-icon-refresh'])[1]")
    private WebElement clickOnRefreshIcon;

    @FindBy(xpath = "//*[@id=\"header\"]/div[5]/div[1]/button")
    private WebElement clickOnOK;

    @FindBy(xpath = "(//button[@title='Custom Column Item'])[1]")
    private WebElement getFilteredData;

    @FindBy(xpath = "(//span[@class='el-checkbox__inner'])[4]")
    private WebElement clickOnCheckbox;

    @FindBy(xpath = "(//div[@class='drawer-icon'])[1]")
    private WebElement clickOnDrawerIcon;

    @FindBy(xpath = "//span[@class='el-checkbox__inner']")
    private WebElement clickOnAttendanceType;

    @FindBy(xpath = "(//i[@class='el-input__icon h-icon-angle_down_sm'])[5]")
    private WebElement clickOnDropDownToChangePagination;

    @FindBy(xpath = "(//span[@class='item-text-style'][normalize-space()='10'])[2]")
    private WebElement chooseRowTotalToTen;

    public AccessRecordNavigation(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    public void navigateToAccessRecordRetrievalPage() throws InterruptedException {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));
        Thread.sleep(6000);
        clickOnOK.click();
        clickOnRefreshIcon.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));
        Thread.sleep(4000);

        wait.until(ExpectedConditions.visibilityOf(clickOnSearch));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnSearch)).click();

        wait.until(ExpectedConditions.visibilityOf(clickOnAccessRecordRetrieval));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnAccessRecordRetrieval)).click();
    }

    public void fetchAttendanceRecord() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Wait for loader to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));

        // Open filters
        wait.until(ExpectedConditions.elementToBeClickable(clickOnMore));
        js.executeScript("arguments[0].click();", clickOnMore);

        wait.until(ExpectedConditions.elementToBeClickable(clickOnChooseAccessStatus)).click();
        wait.until(ExpectedConditions.elementToBeClickable(selectAccessGrantedOption)).click();

        // Search
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
      //  wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));

        // Select filtered data
   //     wait.until(ExpectedConditions.elementToBeClickable(getFilteredData)).click();
        // Drawer & pagination
    //    wait.until(ExpectedConditions.elementToBeClickable(clickOnDrawerIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(clickOnDropDownToChangePagination)).click();
        wait.until(ExpectedConditions.elementToBeClickable(chooseRowTotalToTen)).click();

        System.out.println("10 Attendance Record Fetched Successfully.");
    }

}

