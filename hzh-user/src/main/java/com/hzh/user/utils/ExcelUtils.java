package com.hzh.user.utils;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Hou Zhonghu
 * @since 2022/9/1 10:34
 */
public class ExcelUtils {
    public static List<Map<String,Object>> excelToShopIdList(InputStream inputStream) throws IOException, InvalidFormatException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        int totalRows = sheet.getPhysicalNumberOfRows();
        // 获取表头的总列数
        int totalCols = sheet.getRow(0).getPhysicalNumberOfCells();
        // 遍历行
        for (int i = 0; i < totalRows; i++) {
            // 遍历列
            map = new HashMap<String, Object>();
            for (int j = 0; j < totalCols; j++) {
                // 获取 i 行 j 列
                Cell cell = sheet.getRow(i).getCell(j);
                // 判断该列是否为 null
                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    continue;
                }
                // 判断是否是字符类型
                if (cell.getCellType() == CellType.STRING) {
                    map.put("col" + j, cell.getStringCellValue());
                } else {
                    // 单元格为数值类型
                    cell.setCellType(CellType.NUMERIC);
                    map.put("col" + j, (int) cell.getNumericCellValue());
                }
            }
            list.add(map);
        }
        return list;
    }
}
