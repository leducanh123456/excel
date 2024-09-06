package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.antation.ValidateSingleError;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;

import java.lang.reflect.Method;

@Slf4j
public class ValidateMethodErrorUtil {

    public static <T extends ExcelDTO<T>> Boolean validateSingleError(Class<T> excelClass) {

        Method[] methods = excelClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(ValidateSingleError.class)) {
                Class<?> returnType = methods[i].getReturnType();
                if (!ExcelError.class.isAssignableFrom(returnType)) {
                    log.error("method {} invalid", methods[i].getName());
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }
}
