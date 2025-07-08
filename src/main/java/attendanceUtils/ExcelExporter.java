package attendanceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelExporter {

    // POJO
    public static class AttendanceRecord {
        String firstName;
        String lastName;
        String accessTime; // in IST
        String checkType;

        public AttendanceRecord(String firstName, String lastName, String accessTime, String checkType) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.accessTime = accessTime;
            this.checkType = checkType;
        }
    }

    private LocalDate selectedStartDate;
    private LocalDate selectedEndDate;

    public ExcelExporter(LocalDate startDate, LocalDate endDate) {
        this.selectedStartDate = startDate;
        this.selectedEndDate = endDate;
    }

    public String writeToExcel(List<AttendanceRecord> records, LocalDate reportDate) {
        System.out.println("📌 Using local IST time directly, no conversion.");

        // Reverse records (Page 11 → Page 1)
        Collections.reverse(records);

        String[] columns = {"Sr. No.", "First Name", "Last Name", "Access Time", "Check Type", "Attendance Status"};
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance");

        // Fonts & Styles
        Font defaultFont = workbook.createFont();
        defaultFont.setFontName("Liberation Sans");
        defaultFont.setFontHeightInPoints((short) 10);

        Font boldFont = workbook.createFont();
        boldFont.setFontName("Liberation Sans");
        boldFont.setFontHeightInPoints((short) 10);
        boldFont.setBold(true);

        Font greenFont = workbook.createFont();
        greenFont.setColor(IndexedColors.GREEN.getIndex());
        greenFont.setFontHeightInPoints((short) 10);
        greenFont.setBold(true);

        Font redFont = workbook.createFont();
        redFont.setColor(IndexedColors.RED.getIndex());
        redFont.setFontHeightInPoints((short) 10);
        redFont.setBold(true);

        Font orangeFont = workbook.createFont();
        orangeFont.setColor(IndexedColors.ORANGE.getIndex());
        orangeFont.setFontHeightInPoints((short) 10);
        orangeFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(boldFont);

        CellStyle defaultStyle = workbook.createCellStyle();
        defaultStyle.setFont(defaultFont);

        CellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFont(greenFont);

        CellStyle redStyle = workbook.createCellStyle();
        redStyle.setFont(redFont);

        CellStyle orangeStyle = workbook.createCellStyle();
        orangeStyle.setFont(orangeFont);

        // Header Row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        Set<String> seenCheckIns = new HashSet<>();
        int rowNum = 1, serial = 1;

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        for (AttendanceRecord record : records) {
            Row row = sheet.createRow(rowNum++);
            String userKey = record.firstName + "_" + record.lastName;

            String adjustedCheckType = record.checkType;
            String attendanceStatus = "";
            String formattedAccessTime = record.accessTime;

            try {
                // Directly parse time assuming it's in IST already
            	LocalDateTime utcDateTime = LocalDateTime.parse(record.accessTime, timeFormatter);
            	ZonedDateTime istDateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Kolkata"));
            	LocalTime istTime = istDateTime.toLocalTime();

                if ("Check-In".equalsIgnoreCase(record.checkType)) {
                    if (!seenCheckIns.contains(userKey)) {
                        seenCheckIns.add(userKey);

                        if (!istTime.isAfter(LocalTime.of(10, 0))) {
                            attendanceStatus = "On-time";
                        } else if (!istTime.isAfter(LocalTime.of(10, 15))) {
                            attendanceStatus = "Buffer Late";
                        } else {
                            attendanceStatus = "Late";
                        }
                    } else {
                        adjustedCheckType = "Break";
                    }
                }

            } catch (Exception e) {
                attendanceStatus = "Invalid Time";
            }

            // Fill row data
            row.createCell(0).setCellValue(serial++);
            row.getCell(0).setCellStyle(defaultStyle);

            row.createCell(1).setCellValue(record.firstName);
            row.getCell(1).setCellStyle(defaultStyle);

            row.createCell(2).setCellValue(record.lastName);
            row.getCell(2).setCellStyle(defaultStyle);

            row.createCell(3).setCellValue(formattedAccessTime); // Use IST as-is
            row.getCell(3).setCellStyle(defaultStyle);

            row.createCell(4).setCellValue(adjustedCheckType);
            row.getCell(4).setCellStyle(defaultStyle);

            Cell statusCell = row.createCell(5);
            statusCell.setCellValue(attendanceStatus);
            switch (attendanceStatus) {
                case "On-time":
                    statusCell.setCellStyle(greenStyle);
                    break;
                case "Buffer Late":
                    statusCell.setCellStyle(orangeStyle);
                    break;
                case "Late":
                    statusCell.setCellStyle(redStyle);
                    break;
                default:
                    statusCell.setCellStyle(defaultStyle);
                    break;
            }
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Output directory setup
        String envCI = System.getenv("CI");
        String basePath = (envCI != null && envCI.equalsIgnoreCase("true"))
                ? System.getProperty("user.dir") + "/tempExcel/"
                : "/home/peregrine-it/AttendanceExcels/";

        File directory = new File(basePath);
        if (!directory.exists()) directory.mkdirs();

        // File name logic
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        String fileName;
        if (selectedStartDate != null && selectedEndDate != null && !selectedStartDate.isEqual(selectedEndDate)) {
            String startDateStr = selectedStartDate.format(fileFormatter);
            String endDateStr = selectedEndDate.format(fileFormatter);
            String startDay = selectedStartDate.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
            String endDay = selectedEndDate.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
            fileName = basePath + "AttendanceRecords_" + startDateStr + "_" + startDay +
                       "_to_" + endDateStr + "_" + endDay + ".xlsx";
        } else {
            String formattedDate = reportDate.format(fileFormatter);
            String dayOfWeek = reportDate.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH);
            fileName = basePath + "AttendanceRecords_" + formattedDate + "_" + dayOfWeek + ".xlsx";
        }

        fileName = fileName.replaceAll("[()\\s]", "_");

        try (FileOutputStream out = new FileOutputStream(fileName)) {
            workbook.write(out);
        } catch (IOException e) {
            System.out.println("❌ Failed to write file: " + e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }
}
