package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.antation.ExcelColum;
import org.example.dto.ExcelDTO;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ValidateExcelColum {
    private ValidateExcelColum() {

    }
    public static <T extends ExcelDTO> Boolean checkAllFieldIsAnnotation(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelColum.class)) {
                log.error("tồn tại các trường không được đánh dấu anotation");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO> Boolean checkDuplicateAnnotationExcelColum(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        Set<Integer> uniqueElements = new HashSet<>();
        for(int i = 0; i < fields.length; i++) {
            ExcelColum excelColum = fields[i].getAnnotation(ExcelColum.class);
            if (!uniqueElements.add(excelColum.colNum())) {
                log.error("Có các cột trùng nhau trong cấu hình");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
