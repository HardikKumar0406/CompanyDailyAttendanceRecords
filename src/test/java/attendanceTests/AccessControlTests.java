package attendanceTests;

import org.testng.Assert;
import org.testng.annotations.Test;
import attendancePages.AccessControl;
import attendanceUtils.EmailSender;
import baseTestFile.BaseTests;

import java.io.File;
import java.time.LocalDate;

public class AccessControlTests extends BaseTests {

	@Test(description = "Verify user can export attendance record and send it via email")
	public void testAccessGrantedRecordsRetrieval() throws InterruptedException {
	    AccessControl accesscontrol = new AccessControl(driver);
	    accesscontrol.navigateToAccessRecordRetrievalPage();
	  //  accesscontrol.fetchTodaysRecords();
	    accesscontrol.fetchYesterdayRecords();
	    // Custom date flow
	  //  accesscontrol.customedate(); // MUST set selectedReportDate internally
	    accesscontrol.fetchAttendanceRecord(); // Must work with the selected date

	    String exportedFilePath = accesscontrol.getAllRecord(accesscontrol.selectedReportDate);

	    Assert.assertTrue(new File(exportedFilePath).exists(), "Exported Excel file does not exist!");

	    boolean emailSent = EmailSender.sendEmailWithAttachment(
	           // ,
	    		"mukesh@peregrine-it.com",
	    		"lavanya@peregrine-it.com",
//	    		"hardik040698@gmail.com",
//	    		null,
	            "Attendance Report",
	            "Please find the attached attendance report.",
	            exportedFilePath
	    );

	    Assert.assertTrue(emailSent, "Email was not sent successfully!");
	    System.out.println("✅ Test completed successfully.");
	}


}
