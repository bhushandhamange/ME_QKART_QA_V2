package QKART_TESTNG;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelTestResultListener implements ITestListener {

    private Workbook workbook;
    private Sheet sheet;
    private int rowNum = 1; // Start from 1 to leave the first row for headers

    @Override
    public void onStart(ITestContext context) {

        String filePath = "RunResults.xlsx";  // Replace with the actual file path

        // Convert the file path to a Path object
        Path path = Paths.get(filePath);

        // Check if the file exists
        if (Files.exists(path)) {
            try {
                // Delete the file
                Files.delete(path);
                System.out.println("File deleted successfully.");
            } catch (IOException e) {
                System.err.println("Unable to delete the file: " + e.getMessage());
            }
        } 

        // Initialize the workbook and create the sheet
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("TestResults");

        // Create headers in the first row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Test Case Name");
        headerRow.createCell(1).setCellValue("Test Parameters");
        headerRow.createCell(2).setCellValue("Test Status");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        updateExcel(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        updateExcel(result, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        updateExcel(result, "SKIP");
    }

    @Override
    public void onFinish(ITestContext context) {
        // Write the workbook to a new file with a timestamp to avoid overwriting
        //String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        // String fileName = "TestResults_" + timestamp + ".xlsx";
        String fileName = "RunResults.xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateExcel(ITestResult result, String status) {
        // Get test case name and parameters
        String testCaseName = result.getMethod().getMethodName();
        Object[] parameters = result.getParameters();
        String testParameters = "No parameters";
    
        if (parameters.length > 0) {
            StringBuilder parameterBuilder = new StringBuilder();
            for (Object parameter : parameters) {
                parameterBuilder.append(parameter.toString()).append(", ");
            }
            testParameters = parameterBuilder.substring(0, parameterBuilder.length() - 2);
        }
    
        // Create a new row and update the values
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(testCaseName);
        row.createCell(1).setCellValue(testParameters);
        row.createCell(2).setCellValue(status);
    }
    
}
