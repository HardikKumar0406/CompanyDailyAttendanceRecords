package attendancePages;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import attendanceUtils.ExcelExporter;

public class AccessControl {
    private WebDriver driver;
    private WebDriverWait wait;
    public LocalDate selectedStartDate;
    public LocalDate selectedEndDate;


    public AccessControl(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }

    // WebElements
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

    @FindBy(xpath = "(//span[@class='el-checkbox__inner'])[18]")
    private WebElement clickOnAttendanceType;

    @FindBy(xpath = "(//i[@class='el-input__icon h-icon-angle_down_sm'])[5]")
    private WebElement clickOnDropDownToChangePagination;

    @FindBy(xpath = "(//span[@class='item-text-style'][normalize-space()='10'])[2]")
    private WebElement chooseRowTotalToTen;

    @FindBy(xpath = "(//input[@placeholder='Please Select'])[2]")
    private WebElement chooseTime;
    
    @FindBy(xpath = "(//span[normalize-space()='Yesterday'])[1]")
    private WebElement clickOnyesterday;

    @FindBy(xpath = "(//span[normalize-space()='Custom Time Interval'])[1]")
    private WebElement chooseCustomDate;
    
    @FindBy(xpath = "(//button[@type='button'])[68]")
    private WebElement goToPreviousMonth;
    
    @FindBy(xpath = "(//span[contains(text(),'6')])[2]")
    private WebElement selectStartDate;
    
    @FindBy(xpath = "(//button[@type='button'])[72]")
    private WebElement goToPreviousMonthEndDate;
    
    @FindBy(xpath = "(//span[contains(text(),'6')])[6]")
    private WebElement selectEndDate;
    
    @FindBy(xpath = "(//i[@class='el-input__icon el-range__icon el-date-editor__icon h-icon-calendar'])[1]")
    private WebElement chooseDateAgain;
    
    @FindBy(xpath = "(//button[contains(text(),'OK')])[1]")
    private WebElement clickOnOk;
    
    @FindBy(xpath = "/html/body/div[6]/div[2]/div[1]/span[1]/span[1]")
    private WebElement startDateSpan;

    @FindBy(xpath = "/html/body/div[6]/div[2]/div[1]/span[1]/span[4]")
    private WebElement endDateSpan;
    
    
    public LocalDate selectedReportDate;

    
    // Public to call from test
    public void fetchYesterdayRecords() {
        wait.until(ExpectedConditions.visibilityOf(chooseTime));
        chooseTime.click();
        wait.until(ExpectedConditions.visibilityOf(clickOnyesterday));
        clickOnyesterday.click();
        selectedReportDate = LocalDate.now().minusDays(1);
        selectedStartDate = selectedEndDate = selectedReportDate;// Set yesterday
    }
    
    public void customedate() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(chooseTime));
        chooseTime.click();
        wait.until(ExpectedConditions.visibilityOf(chooseCustomDate));
        chooseCustomDate.click();
        Thread.sleep(1000);
        chooseDateAgain.click();
      //  wait.until(ExpectedConditions.visibilityOf(goToPreviousMonth));
     //   goToPreviousMonth.click();
        selectStartDate.click();
    //    goToPreviousMonthEndDate.click(); // commented in your code
        selectEndDate.click();
        clickOnOk.click();

        // Wait for the spans to be visible to get text
        wait.until(ExpectedConditions.visibilityOf(startDateSpan));
        wait.until(ExpectedConditions.visibilityOf(endDateSpan));

        String startDateStr = startDateSpan.getText().trim();  // e.g. "2025/05/28"
        String endDateStr = endDateSpan.getText().trim();      // e.g. "2025/06/04"

        // Parse using appropriate formatter (your format is yyyy/MM/dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        selectedStartDate = LocalDate.parse(startDateStr, formatter);
        selectedEndDate = LocalDate.parse(endDateStr, formatter);

        // For backward compatibility
        selectedReportDate = selectedStartDate;
    }


    public void fetchTodaysRecords() {
        selectedReportDate = LocalDate.now();
        selectedStartDate = selectedEndDate = selectedReportDate;
    }


    
    public void navigateToAccessRecordRetrievalPage() throws InterruptedException {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));
        Thread.sleep(4000);
        clickOnOK.click();
        Thread.sleep(2000);
        clickOnRefreshIcon.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));
        Thread.sleep(3000);

        wait.until(ExpectedConditions.visibilityOf(clickOnSearch));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnSearch)).click();

        wait.until(ExpectedConditions.visibilityOf(clickOnAccessRecordRetrieval));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnAccessRecordRetrieval)).click();
    }

    public void fetchAttendanceRecord() throws InterruptedException {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));
        Thread.sleep(4000);
        wait.until(ExpectedConditions.visibilityOf(clickOnMore));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnMore));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", clickOnMore);
        wait.until(ExpectedConditions.visibilityOf(clickOnChooseAccessStatus));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnChooseAccessStatus)).click();

        wait.until(ExpectedConditions.visibilityOf(selectAccessGrantedOption));
        wait.until(ExpectedConditions.elementToBeClickable(selectAccessGrantedOption)).click();

        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));
        Thread.sleep(5000);
        getFilteredData.click();
        clickOnCheckbox.click();
        clickOnAttendanceType.click();
        clickOnDrawerIcon.click();
        Thread.sleep(1000);
        clickOnDropDownToChangePagination.click();
        Thread.sleep(1000);
        chooseRowTotalToTen.click();
        Thread.sleep(2000);

        System.out.println("10 Attendance Record Fetched Successfully.");
    }

    public String getAllRecord(LocalDate reportDate) throws InterruptedException {
        if (selectedReportDate == null) {
            throw new IllegalStateException("❌ Report date not set. Call fetchYesterdayRecords() or fetchTodaysRecords() first.");
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("(//span[@class='path1'])[1]")));

        int pageNumber = 1;
        int totalRecordsFetched = 0;
        List<ExcelExporter.AttendanceRecord> allRecords = new ArrayList<>();

        while (true) {
            System.out.println("Fetching records from page: " + pageNumber);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".el-table__body tbody tr.el-table__row")));
            List<WebElement> rows = driver.findElements(By.cssSelector(".el-table__body tbody tr.el-table__row"));

            System.out.println("Total Rows Found on Page " + pageNumber + ": " + rows.size());

            int index = 1;
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.cssSelector("td .cell"));

                if (cells.size() >= 8) {
                    String firstName = cells.get(0).getText().trim();
                    String lastName = cells.get(1).getText().trim();
                    String accessTime = cells.get(3).getText().trim();
                    String checkType = cells.get(7).getText().trim();

                    System.out.println("------- Row " + index + " Data (Page " + pageNumber + ") -------");
                    System.out.println("First Name : " + firstName);
                    System.out.println("Last Name  : " + lastName);
                    System.out.println("Access Time  : " + accessTime);
                    System.out.println("Check Type : " + checkType);
                    System.out.println("-----------------------------------");

                    allRecords.add(new ExcelExporter.AttendanceRecord(firstName, lastName, accessTime, checkType));
                    totalRecordsFetched++;
                } else {
                    System.out.println("Row " + index + " skipped due to insufficient columns.");
                }
                index++;
            }

            try {
                WebElement nextButton = driver.findElement(By.xpath("(//li[@class='h-icon-angle_right'])[1]"));
                if (nextButton.getAttribute("class").contains("is-disabled")) {
                    System.out.println("No more pages. Reached end of pagination.");
                    break;
                } else {
                    wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
                    Thread.sleep(2000);
                    pageNumber++;
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                System.out.println("Next button not found or not clickable. Stopping pagination.");
                break;
            }
        }

        System.out.println("Total attendance records fetched: " + totalRecordsFetched);

        ExcelExporter exporter = new ExcelExporter(selectedStartDate, selectedEndDate);
        return exporter.writeToExcel(allRecords, selectedReportDate);
    }


    private String getCellValue(WebElement row, String columnClass) {
        try {
            WebElement cell = row.findElement(By.cssSelector("td[class*='" + columnClass + "'] .cell"));
            WebElement innerDiv = cell.findElement(By.cssSelector("div"));
            String value = innerDiv.getAttribute("title");
            return (value != null && !value.isEmpty()) ? value.trim() : innerDiv.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
