package org.example.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.TitleExcel;
import org.example.dto.ExcelDTO;
import org.example.exception.DefineExcelException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ValidateUtilImpl<T extends ExcelDTO> implements ValidateUtil<T> {
    public Boolean validateHeaderMetaData(Class<T> headerClass) {
        Field[] fields = headerClass.getDeclaredFields();
        int tmp = 0;
        for (Field field : fields) {
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            if (titleExcel == null) {
                continue;
            }
            String[] titles = titleExcel.title();
            int[] rows = titleExcel.rowNum();
            int[] cols = titleExcel.colNum();
            if (titles.length == 0) {
                throw new DefineExcelException("Định nghĩa độ sâu của tile không hợp lệ");
            }
            if (!(titles.length == rows.length && rows.length == cols.length)) {
                throw new DefineExcelException("Định nghĩa độ sâu của tile không hợp lệ");
            }
            IntStream intStreamRow = Arrays.stream(rows);
            IntStream intStreamCol = Arrays.stream(cols);
            if (intStreamRow.anyMatch(tmpInt -> tmpInt < 0)) {
                throw new DefineExcelException("Định nghĩa độ sâu của tile không hợp lệ");
            }
            if (intStreamCol.anyMatch(tmpInt -> tmpInt < 0)) {
                throw new DefineExcelException("Định nghĩa độ sâu của tile không hợp lệ");
            }
            tmp++;
            if (tmp == 0) {
                throw new DefineExcelException("Class định nghĩa cho title excel không hợp lệ");
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean validateHeader(Class<T> headerClass, Sheet sheet) {
        Boolean validateMetaData = validateHeaderMetaData(headerClass);
        if (!validateMetaData) {
            return validateMetaData;
        }
        Field[] fields = headerClass.getDeclaredFields();
        for (Field field : fields) {
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            if (titleExcel == null) {
                continue;
            }
            String[] titles = titleExcel.title();
            int[] rows = titleExcel.rowNum();
            int[] cols = titleExcel.colNum();
            for (int i = 0; i < rows.length; i++) {
                Row row = sheet.getRow(rows[i]);
                Cell cell = row.getCell(cols[i]);
                if (cell.getStringCellValue() == null || !cell.getStringCellValue().equalsIgnoreCase(titles[i])) {
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public Errors getErrorFromExcelObject(T t, Validator validator) {
        Errors errors = new BeanPropertyBindingResult(t, "error");
        validator.validate(t, errors);
        return errors;
    }
}
