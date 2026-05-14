package api.utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;

public class ExcelUtilities {
    public FileInputStream fi;
    public FileOutputStream fo;
    public XSSFWorkbook wb;
    public XSSFSheet sh;
    public XSSFRow row;
    public XSSFCell cell;
    public XSSFCellStyle style;
    String path;


    public ExcelUtilities(String path) {
        this.path = path;
    }

    public int getRowCount(String sheetName) throws IOException {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        sh = wb.getSheet(sheetName);
        int rowCount = sh.getLastRowNum();
        wb.close();
        fi.close();
        return rowCount;
    }

    public int getCellCount(String sheetName, int rownum) throws IOException {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        sh = wb.getSheet(sheetName);
        row = sh.getRow(rownum);
        int cellCount = row.getLastCellNum();
        wb.close();
        fi.close();
        return cellCount;
    }

    public String getCellData(String sheetName, int rownum, int colnum) throws IOException {
        fi = new FileInputStream(path);
        wb = new XSSFWorkbook(fi);
        sh = wb.getSheet(sheetName);
        row = sh.getRow(rownum);
        cell = row.getCell(colnum);

        DataFormatter dataFormatter = new DataFormatter();
        String data;
        data = dataFormatter.formatCellValue(cell);

        wb.close();
        fi.close();
        return data;

    }

    public void setCellData(String sheetName, int rownum, int colnum, String id) throws IOException {
        File fl = new File(path);
        fi = new FileInputStream(path);
        if (!fl.exists()) {            //if file doesnot exist
            wb = new XSSFWorkbook();
            fo = new FileOutputStream(path);
            wb.write(fo);
        }


        wb = new XSSFWorkbook(fi);
        sh = wb.getSheet(sheetName);

        if (wb.getSheetIndex(sheetName) == -1) {      //if sheet doesnt exist
            wb.createSheet(sheetName);
            sh = wb.getSheet(sheetName);
        }

        row = sh.getRow(rownum);

        if (row == null) {         //if row doesnt exist
            row = sh.createRow(rownum);
        }

        cell = row.getCell(colnum);

        if (cell == null) {         //if row doesnt exist
            cell = row.createCell(colnum);
        }

        cell.setCellValue(id);
        fo = new FileOutputStream(path);
        wb.write(fo);
        wb.close();
        fi.close();
        fo.close();
    }
}

