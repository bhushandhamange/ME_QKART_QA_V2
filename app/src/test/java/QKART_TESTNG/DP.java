package QKART_TESTNG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;




public class DP {
    
    @DataProvider(name = "data-provider")
	public Object[][] dpMethod(Method m) throws IOException {
		return readExcelData("src/test/resources/Dataset.xlsx", m.getName());
	}


    public static Object[][] readExcelData(String Path, String SheetName) throws IOException {

        System.out.println("Sheet Name : "+ SheetName);

        FileInputStream FIS = new FileInputStream(new File(Path));
        Object[][] data = null;
        try (XSSFWorkbook workbook = new XSSFWorkbook(FIS)) {
            XSSFSheet sheet = workbook.getSheet(SheetName);
            int rowsCount = countRowsWithData(sheet);//sheet.getLastRowNum() + 1; //sheet.getPhysicalNumberOfRows();

            System.out.println("row count : "+ rowsCount);

            XSSFRow row = sheet.getRow(0);
            int colCount = row.getPhysicalNumberOfCells();

            System.out.println("col count : "+ colCount);

            data = new Object[rowsCount-1][colCount-1];

            for (int i = 0; i < rowsCount - 1; i++) {
                row = sheet.getRow(i+1);
                for (int j = 0; j < colCount - 1; j++) {
                    XSSFCell cell = row.getCell(j+1);

                    // if(cell.getCellType() == CellType.BLANK){
                    //     return data;
                    // }

                    switch (cell.getCellType()) {
                        case STRING:
                            data[i][j] = cell.getStringCellValue();
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case NUMERIC:
                            // Handle numeric values as needed
                            if (DateUtil.isCellDateFormatted(cell)) {
                                data[i][j] = cell.getDateCellValue();
                                System.out.print(cell.getDateCellValue() + "\t");
                            } else {
                                data[i][j] = (int) cell.getNumericCellValue();
                                System.out.print((int) cell.getNumericCellValue() + "\t");
                            }
                            break;
                           // Handle other cell types if necessary
                        // case BOOLEAN:
                        //     System.out.print(cell.getBooleanCellValue() + "\t");
                        //     break;
                        // case FORMULA:
                        //     System.out.print(cell.getCellFormula() + "\t");
                        //     break;
                        default:
                            data[i][j] = "";
                            System.out.print("\t");
                    }

                }
                System.out.println("");
            }
        }
        return data;
    }

    private static int countRowsWithData(XSSFSheet sheet) {
        int rowCount = 0;
        Iterator<Row> rowIterator = sheet.iterator();
    
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
    
            // Check if the row has any non-blank cells
            boolean hasData = false;
            Iterator<Cell> cellIterator = row.iterator();
    
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() != CellType.BLANK) {
                    hasData = true;
                    break;
                }
            }
    
            if (hasData) {
                rowCount++;
            }
        }
    
        return rowCount;
    }
    
    

}