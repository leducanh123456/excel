package org.example.util;

import lombok.extern.slf4j.Slf4j;
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
}
