package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.antation.ExcelColum;
import org.example.antation.TitleExcel;
import org.example.dto.ExcelDTO;

import java.lang.reflect.Field;

@Slf4j
public class ValidateTitleExcel {
    private ValidateTitleExcel() {

    }

    public static <T extends ExcelDTO> Boolean checkAllFieldIsAnnotation(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(TitleExcel.class)) {
                log.error("tồn tại các trường không được đánh dấu anotation");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO> Boolean checkListTitleAndColAndRow(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        for (Field field : fields) {
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            if (titleExcel.rowNum().length == 0 || titleExcel.colNum().length == 0 || titleExcel.title().length == 0) {
                log.error("Tồn tại 1 col đang không được cấu hình title đúng cách");
                return Boolean.FALSE;
            }
            if (titleExcel.rowNum().length != titleExcel.colNum().length || titleExcel.colNum().length != titleExcel.title().length
                    || titleExcel.rowNum().length != titleExcel.title().length) {
                log.error("ồn tại 1 col đang không được cấu hình title đúng cách");
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
}
