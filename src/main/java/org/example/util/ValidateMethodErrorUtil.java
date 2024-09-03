package org.example.util;

import org.example.dto.ExcelDTO;

public class ValidateMethodErrorUtil {
    public static <T extends ExcelDTO<T>> Boolean validateSingleError(Class<T> t) {

        return Boolean.TRUE;
    }
}
