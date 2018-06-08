package com.gxg.util;

import com.gxg.entities.EnterpriseInformation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by 郭欣光 on 2018/6/7.
 */


public class ExcelReader {


    public static ArrayList<JSONObject> readExcel(String excelPath) {
        ArrayList<JSONObject> enterpriseInformationList = new ArrayList<>();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(excelPath);
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
            for (int i = 1; i  <= hssfSheet.getLastRowNum(); i++) {
                HSSFRow hssfRow = hssfSheet.getRow(i);
                EnterpriseInformation enterpriseInformation = new EnterpriseInformation();
                enterpriseInformation.setName(hssfRow.getCell(0).getStringCellValue());
                enterpriseInformation.setRegistrationNumber(hssfRow.getCell(1).getStringCellValue());
                enterpriseInformationList.add(enterpriseInformation.toJson());
            }
            return enterpriseInformationList;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
