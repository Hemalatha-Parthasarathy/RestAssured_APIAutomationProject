package api.utilities;

import org.testng.annotations.DataProvider;

import java.io.IOException;

public class DataProviders {

    @DataProvider(name = "Data")
    public Object[][] getExcelData() throws IOException {
        String path = System.getProperty("user.dir") + "//TestData//TestData.xlsx";
        ExcelUtilities xl = new ExcelUtilities(path);

        int rowCount = xl.getRowCount("Sheet1");
        int colCount = xl.getCellCount("Sheet1", 1);

        Object apiData[][] = new Object[rowCount][colCount + 1];

        for (int i = 1; i <= rowCount; i++) {
            apiData[i - 1][0] = i;
            for (int j = 0; j < colCount; j++) {
                apiData[i - 1][j + 1] = xl.getCellData("Sheet1", i, j);
            }
        }

        return apiData;
    }


    @DataProvider(name = "Id")
    public Object[] getIdData() throws IOException {
        String path = System.getProperty("user.dir") + "//TestData//TestData.xlsx";
        ExcelUtilities xl = new ExcelUtilities(path);
        int rowCount = xl.getRowCount("Sheet1");

        Object apidata[] = new Object[rowCount];
        for (int i = 1; i <= rowCount; i++) {
            apidata[i - 1] = xl.getCellData("Sheet1", i, 4);

        }

        return apidata;
    }


}
