package org.example.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelPrimary;
import org.example.antation.TitleExcel;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;
import org.example.exception.DefineExcelException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.*;

import java.lang.reflect.Field;
import java.util.*;
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

    public List<ExcelError> checkPrimary(List<T> list, Class<T> t) throws IllegalAccessException {
        List<ExcelError> errors = new ArrayList<>();
        Field[] fields = t.getDeclaredFields();
        List<Field> fieldsPrimary = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getAnnotation(ExcelPrimary.class) != null) {
                fieldsPrimary.add(fields[i]);
            }
        }
        if (fieldsPrimary.isEmpty()) {
            return errors;
        }
        for (Field field : fieldsPrimary) {
            Set<Object> idSet = new HashSet<>();
            for (T obj : list) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (!idSet.add(value)) {
                    ExcelError excelError = new ExcelError();
                    excelError.setRowNum(obj.getRowNumber());
                    excelError.setRowNumContent(obj.getContentNumber());
                    TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
                    ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
                    excelError.setMessage(titleExcel.title()[0]);
                    excelError.setColNum(excelColum.colNum());
                    errors.add(excelError);
                }
            }
        }
        return errors;
    }

    public List<ExcelError> validateData(List<T> list, Validator validator, Class<T> excelClass) {
        List<ExcelError> errors = new ArrayList<>();
        for (T t : list) {
            Errors errorObject = new BeanPropertyBindingResult(t, "errorObject");
            validator.validate(t, errorObject);
            List<ObjectError> objectErrors = errorObject.getAllErrors();
            for (ObjectError objectError : objectErrors) {
                if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    Set<String> cellNotCheck = t.getCellNotCheck();
                    Set<String> cellInvalidType = t.getCellInValidType();
                    if (!ObjectUtils.isEmpty(cellNotCheck) && cellNotCheck.contains(fieldError.getField())) {
                        continue;
                    }
                    if (!ObjectUtils.isEmpty(cellInvalidType) && cellInvalidType.contains(fieldError.getField())) {
                        continue;
                    }
                    ExcelError excelError = new ExcelError();
                    excelError.setRowNum(t.getRowNumber());
                    excelError.setRowNumContent(t.getContentNumber());
                    excelError.setMessage(fieldError.getDefaultMessage());
                    excelError.setColNum(1);
                    errors.add(excelError);
                }
            }
        }
        return errors;
    }
}