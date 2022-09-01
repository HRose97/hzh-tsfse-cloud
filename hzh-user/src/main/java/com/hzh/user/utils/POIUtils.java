package com.hzh.user.utils;

import com.hzh.common.pojo.HzhUser;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能 ： 解析成数据集合
 * @author Hou Zhonghu
 * @since 2022/8/31 19:11
 */
public class POIUtils {

    /**
     * Excel 解析成数据集合
     *
     * @return
     */
    public static List<HzhUser> excel2Employee(MultipartFile file) {
        List<HzhUser> list = new ArrayList<>();
        HzhUser hzhUser = null;
        try {
            //1. 创建一个 workbook 对象
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            //2. 获取 workbook 中表单的数量
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                //3. 获取表单
                XSSFSheet sheet = workbook.getSheetAt(i);
                //4. 获取表单中的行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                for (int j = 0; j < physicalNumberOfRows; j++) {
                    //5. 跳过标题行
                    if (j == 0) {
                        continue;//跳过标题行
                    }
                    //6. 获取行
                    XSSFRow row = sheet.getRow(j);
                    if (row == null) {
                        continue;//防止数据中间有空行
                    }
                    //7. 获取列数
                    int physicalNumberOfCells = row.getPhysicalNumberOfCells();
                    hzhUser = new HzhUser();
                    for (int k = 0; k < physicalNumberOfCells; k++) {
                        XSSFCell cell = row.getCell(k);
                        switch (cell.getCellType()) {
                            // 类型是 String 进入此 case 块
                            case STRING:
                                String cellValue = cell.getStringCellValue();
                                switch (k) {
                                    case 2:
                                        hzhUser.setUserName(cellValue);
                                        break;
                                    case 3:
                                        hzhUser.setUserDescription(cellValue);
                                        break;
                                    case 5:
                                        hzhUser.setStatus(cellValue);
                                        break;
                                    case 6:
                                        hzhUser.setEmail(cellValue);
                                        break;
                                    case 7:
                                        hzhUser.setPhonenumber(cellValue);
                                        break;
                                    case 8:
                                        hzhUser.setSex(cellValue);
                                        break;
                                    case 9:
                                        hzhUser.setSalt(cellValue);
                                        break;
                                    case 10:
                                        hzhUser.setAvatar(cellValue);
                                        break;
                                    case 11:
                                        hzhUser.setLevel(cellValue);
                                        break;
                                    case 12:
                                        hzhUser.setUserType(cellValue);
                                        break;
                                    case 13:
                                        hzhUser.setCreateTime(cellValue);
                                        break;
                                    case 14:
                                        hzhUser.setUpdateTime(cellValue);
                                        break;

                                    case 16:
                                        hzhUser.setDelFlag(cellValue);
                                        break;
                                }
                                break;
                            // 类型是 Date或者数字 进入此 case 块
                            default: {
                                switch (k) {
                                    case 1:
                                        hzhUser.setId((long) cell.getNumericCellValue());
                                        break;
                                    case 15:
                                        hzhUser.setUpdateBy((long) cell.getNumericCellValue());
                                        break;
                                }
                            }
                            break;
                        }
                        }
                        // 最后将解析后的数据添加到员工集合中
                        list.add(hzhUser);
                    }
                }

            } catch(IOException e){
                e.printStackTrace();
            }
            return list;
        }

}
