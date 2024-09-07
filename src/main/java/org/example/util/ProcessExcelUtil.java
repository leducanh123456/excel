package org.example.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.*;
import org.example.collection.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;
import org.example.exception.DefineExcelException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class ProcessExcelUtil {
    private ProcessExcelUtil() {

    }

    public static <T extends ExcelDTO<T>> Boolean validateHeaderMetaData(Class<T> headerClass) {
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

    public static <T extends ExcelDTO<T>> Boolean validateHeader(Class<T> headerClass, Sheet sheet) {
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

    public static <T extends ExcelDTO<T>> Errors getErrorFromExcelObject(T t, Validator validator) {
        Errors errors = new BeanPropertyBindingResult(t, "error");
        validator.validate(t, errors);
        return errors;
    }

    public static <T extends ExcelDTO<T>> void checkPrimary(List<T> list, Class<T> t) throws IllegalAccessException {
        Field[] fields = t.getDeclaredFields();
        List<Field> fieldsPrimary = new ArrayList<>();
        for (Field item : fields) {
            if (item.getAnnotation(ExcelPrimary.class) != null) {
                fieldsPrimary.add(item);
            }
        }
        for (Field field : fieldsPrimary) {
            Set<Object> idSet = new HashSet<>();
            for (T obj : list) {
                Object value = field.get(obj);
                if (value == null) {
                    continue;
                }
                if (!idSet.add(value)) {
                    ExcelError excelError = new ExcelError();
                    excelError.setRowNum(obj.getRowNumber());
                    excelError.setRowNumContent(obj.getContentNumber());
                    TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
                    ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
                    ExcelPrimary excelPrimary = field.getAnnotation(ExcelPrimary.class);
                    excelError.setMessage(excelPrimary.message());
                    excelError.setColNum(excelColum.colNum());
                    excelError.setTitleExcel(Arrays.asList(titleExcel.title()));
                    obj.getErrors().add(excelError);
                    obj.getExcelCollection().getExcelErrors().add(excelError);
                }
            }
        }
    }

    public static <T extends ExcelDTO<T>> void validateData(List<T> list, Validator validator, Class<T> excelClass) throws InvocationTargetException, IllegalAccessException {
        for (T t : list) {
            List<ExcelError> errors = new ArrayList<>();
            Errors errorObject = new BeanPropertyBindingResult(t, "errorObject");
            validator.validate(t, errorObject);
            List<ObjectError> objectErrors = errorObject.getAllErrors();
            Set<String> cellNotCheck = t.getCellNotCheck();
            Set<String> cellInvalidType = t.getCellInValidType();
            for (ObjectError objectError : objectErrors) {
                if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    if (!ObjectUtils.isEmpty(cellNotCheck) && cellNotCheck.contains(fieldError.getField())) {
                        continue;
                    }
                    if (!ObjectUtils.isEmpty(cellInvalidType) && cellInvalidType.contains(fieldError.getField())) {
                        continue;
                    }
                    Field[] fields = excelClass.getDeclaredFields();
                    ExcelError excelError = new ExcelError();
                    excelError.setRowNum(t.getRowNumber());
                    excelError.setRowNumContent(t.getContentNumber());
                    excelError.setMessage(fieldError.getDefaultMessage());
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].getName().equals(fieldError.getField())) {
                            TitleExcel titleExcel = fields[i].getAnnotation(TitleExcel.class);
                            ExcelColum excelColum = fields[i].getAnnotation(ExcelColum.class);
                            excelError.setTitleExcel(Arrays.asList(titleExcel.title()));
                            excelError.setColNum(excelColum.colNum());
                        }
                    }
                    errors.add(excelError);
                }
            }
            if (cellInvalidType != null) {
                for (String cell : cellInvalidType) {
                    ExcelError excelError = new ExcelError();
                    excelError.setRowNum(t.getRowNumber());
                    excelError.setRowNumContent(t.getContentNumber());
                    excelError.setMessage(cell + " : Không đúng định dạng dữ liệu");
                    Field[] fields = excelClass.getDeclaredFields();
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].getName().equals(cell)) {
                            TitleExcel titleExcel = fields[i].getAnnotation(TitleExcel.class);
                            ExcelColum excelColum = fields[i].getAnnotation(ExcelColum.class);
                            excelError.setTitleExcel(Arrays.asList(titleExcel.title()));
                            excelError.setColNum(excelColum.colNum());
                        }
                    }
                    errors.add(excelError);
                }
            }
            // chay cac ham lien quan den single error
            Method[] methods = excelClass.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].isAnnotationPresent(ValidateSingleError.class)) {
                    Object result = methods[i].invoke(t);
                    if (result != null) {
                        errors.add((ExcelError) result);
                    }
                }
            }

            t.setErrors(errors);
            t.getExcelCollection().getExcelErrors().addAll(errors);
        }
    }

    public static <T extends ExcelDTO<T>, R extends ExcelCollection<T>> void validateDataExcel(R r, Class<T> excelClass) throws InvocationTargetException, IllegalAccessException {
        ExcelCollectionClass collectionExcelClass = excelClass.getAnnotation(ExcelCollectionClass.class);
        Class<R> classCollection = (Class<R>) collectionExcelClass.colectionClass();
        Method[] methods = classCollection.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(ValidateListError.class)) {
                Object result = methods[i].invoke(r);
                if (result != null) {
                    r.getExcelFileErrors().addAll((ArrayList) result);
                }
            }
        }
    }
}