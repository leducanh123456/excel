package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.antation.ExcelMapping;
import org.example.dto.ExcelDTO;

import java.util.HashSet;
import java.util.Set;
@Slf4j
public class ValidateExcelMapping {

    private ValidateExcelMapping() {

    }
    public static <T extends ExcelDTO> Boolean checkAnnotationClass(Class<T> excelClass) {
        if (excelClass.isAnnotationPresent(ExcelMapping.class)) {
            return Boolean.TRUE;
        }
        log.error("Không có anotation trong class được chỉ định");
        return Boolean.FALSE;
    }

    public static <T extends ExcelDTO> Boolean checkExcelMaxRow(Class<T> excelClass) {
        ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
        int maxRow = annotation.excelMaxRow();
        if (maxRow >= 0) {
            return Boolean.TRUE;
        }
        log.error("Số hàng được đọc tối đa không hợp lệ");
        return Boolean.FALSE;
    }

    public static <T extends ExcelDTO> Boolean checkExcelConfigPath(Class<T> excelClass) {
        ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
        if (annotation.isResourceFolder() && !annotation.path().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO> Boolean checkExcelValidReadSheet(Class<T> excelClass) {
        ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
        if (annotation.readAllSheet()) {
            return Boolean.TRUE;
        }
        int[] sheets = annotation.readSheet();
        if (sheets.length > 100) {
            log.error("Số Sheet được đọc vượt quá 100");
            return Boolean.FALSE;
        }
        Set<Integer> uniqueElements = new HashSet<>();
        for (int num : sheets) {
            if (!uniqueElements.add(num)) {
                log.error("Cấu hình các sheet được đọc trùng lặp");
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO> Boolean checkExcelStartRow(Class<T> excelClass) {
        ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
        int startRow = annotation.startRow();
        if (startRow < 0) {
            log.error("Cấu hình start row không hợp lệ");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO> Boolean checkExcelSMaxRow(Class<T> excelClass) {
        ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
        int maxRow = annotation.excelMaxRow();
        if (maxRow < 0 || maxRow > 10000000) {
            log.error("Cấu hình max row không hợp lệ");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
