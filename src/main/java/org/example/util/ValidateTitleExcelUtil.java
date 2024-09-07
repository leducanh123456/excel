package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.ExcelColum;
import org.example.antation.TitleExcel;
import org.example.dto.ExcelDTO;

import java.lang.reflect.Field;

@Slf4j
public class ValidateTitleExcelUtil {
    private ValidateTitleExcelUtil() {

    }

    public static <T extends ExcelDTO<T>> Boolean checkAllFieldIsAnnotation(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(TitleExcel.class)) {
                log.error("tồn tại các trường không được đánh dấu anotation");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO<T>> Boolean checkListTitleAndColAndRow(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        for (Field field : fields) {
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            if (titleExcel.rowNum().length == 0 || titleExcel.colNum().length == 0 || titleExcel.title().length == 0) {
                log.error("Tồn tại 1 col đang không được cấu hình title đúng cách");
                return Boolean.FALSE;
            }
            if (titleExcel.rowNum().length != titleExcel.colNum().length || titleExcel.colNum().length != titleExcel.title().length
                    || titleExcel.rowNum().length != titleExcel.title().length) {
                log.error("tồn tại 1 col đang không được cấu hình title đúng cách");
                return Boolean.FALSE;
            }
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            int colNumber = excelColum.colNum();
            int colTitle = titleExcel.colNum()[titleExcel.colNum().length - 1];
            if (colNumber != colTitle) {
                log.error("Tồn tại col đang không được cấu hình title đúng cách");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO<T>> Boolean checkHeaderExcel(Class<T> excelClass, Sheet sheet) {
        Field[] fields = excelClass.getDeclaredFields();
        // lấy danh sách các title của từng cột để check
        for (Field field : fields) {
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            String[] titles = titleExcel.title();
            int[] rows = titleExcel.rowNum();
            int[] cols = titleExcel.colNum();
            for (int i = 0; i < titles.length; i++) {
                Row row = sheet.getRow(rows[i]);
                Cell cell = row.getCell(cols[i]);
                String titleValue = cell.getStringCellValue();
                if(!titles[i].equals(titleValue)){
                    log.error("title {} không hợp lệ" , titles[i]);
                    return Boolean.FALSE;
                }
            }
        }

        return Boolean.TRUE;
    }
}
