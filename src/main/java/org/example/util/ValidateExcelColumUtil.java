package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.antation.ExcelColum;
import org.example.antation.FormatExcel;
import org.example.dto.ExcelDTO;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ValidateExcelColumUtil {
    private ValidateExcelColumUtil() {

    }

    public static <T extends ExcelDTO<T>> Boolean checkAllFieldIsAnnotation(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelColum.class)) {
                log.error("Excel config : There are fields that are not marked with annotation ExcelColum.");
                return Boolean.FALSE;
            }
            if (!field.isAnnotationPresent(FormatExcel.class)) {
                log.error("Excel config : There are fields that are not marked with annotation FormatExcel.");
                return Boolean.FALSE;
            }
            FormatExcel formatExcel = field.getAnnotation(FormatExcel.class);
            String[] formats = formatExcel.format();
            for (String format : formats) {
                if (format == null || format.trim().isEmpty()) {
                    log.error("Excel config : Format configuration cannot be left blank.");
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO<T>> Boolean checkDuplicateAnnotationExcelColum(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        Set<Integer> uniqueElements = new HashSet<>();
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            if (!uniqueElements.add(excelColum.colNum())) {
                log.error("Excel config : There are duplicate columns in the configuration");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
