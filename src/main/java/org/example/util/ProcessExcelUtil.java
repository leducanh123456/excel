package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.*;
import org.example.collection.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class ProcessExcelUtil {
    private ProcessExcelUtil() {

    }

    public static <T extends ExcelDTO<T>> Boolean validateHeaderMetaData(Class<T> headerClass) {
        Field[] fields = headerClass.getDeclaredFields();
        int tmp = 0;
        for (Field field : fields) {
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            String[] titles = titleExcel.title();
            int[] rows = titleExcel.rowNum();
            int[] cols = titleExcel.colNum();
            if (titles.length == 0) {
                log.error("Bắt buộc phải định nghĩa title");
                tmp++;
            }
            if (!(titles.length == rows.length && rows.length == cols.length)) {
                log.error("Sai lệch giữa việc định nghĩa title và số cột");
                tmp++;
            }
            IntStream intStreamRow = Arrays.stream(rows);
            IntStream intStreamCol = Arrays.stream(cols);
            if (intStreamRow.anyMatch(tmpInt -> tmpInt < 0)) {
                log.error("Không được phép có định nghĩa về hàng nào nhỏ hơn 0");
                tmp++;
            }
            if (intStreamCol.anyMatch(tmpInt -> tmpInt < 0)) {
                log.error("Không được phép có cột nào nhỏ hơn 0");
                tmp++;
            }
        }
        if (tmp != 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO<T>> Boolean validateHeader(Class<T> headerClass, Sheet sheet) {
        boolean validateMetaData = validateHeaderMetaData(headerClass);
        if (!validateMetaData) {
            return false;
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
            Errors errorObject = new BeanPropertyBindingResult(t, "errorObject");
            validator.validate(t, errorObject);
            List<ObjectError> objectErrors = errorObject.getAllErrors();
            Set<String> cellNotCheck = t.getCellNotCheck();
            Set<String> cellInvalidType = t.getCellInValidType();
            addErrorFromValidation(excelClass, t, objectErrors, cellNotCheck, cellInvalidType);
            addCellInvalidType(excelClass, t, cellInvalidType);
            // chay cac ham lien quan den single error
            excuseSingleError(excelClass, t);
            t.getExcelCollection().getExcelErrors().addAll(t.getErrors());
        }
    }

    private static <T extends ExcelDTO<T>> void excuseSingleError(Class<T> excelClass, T t) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = excelClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ValidateSingleError.class)) {
                Object result = method.invoke(t);
                if (result != null) {
                    t.getErrors().add((ExcelError) result);
                }
            }
        }
    }

    protected static <T extends ExcelDTO<T>> void addErrorFromValidation(Class<T> excelClass, T t, List<ObjectError> objectErrors, Set<String> cellNotCheck, Set<String> cellInvalidType) {
        for (ObjectError objectError : objectErrors) {
            if (objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError; // Explicitly cast
                if ((!ObjectUtils.isEmpty(cellNotCheck) && cellNotCheck.contains(fieldError.getField()))
                        || (!ObjectUtils.isEmpty(cellInvalidType) && cellInvalidType.contains(fieldError.getField()))) {
                    continue;
                }
                generateError(excelClass, t, fieldError);
            }
        }
    }

    private static <T extends ExcelDTO<T>> void generateError(Class<T> excelClass, T t, FieldError fieldError) {
        Field[] fields = excelClass.getDeclaredFields();
        ExcelError excelError = new ExcelError();
        excelError.setRowNum(t.getRowNumber());
        excelError.setRowNumContent(t.getContentNumber());
        excelError.setMessage(fieldError.getDefaultMessage());
        generateError(fieldError.getField(), fields, excelError);
        t.getErrors().add(excelError);
    }

    protected static <T extends ExcelDTO<T>> void addCellInvalidType(Class<T> excelClass, T t, Set<String> cellInvalidType) {
        if (cellInvalidType != null) {
            for (String cell : cellInvalidType) {
                ExcelError excelError = new ExcelError();
                excelError.setRowNum(t.getRowNumber());
                excelError.setRowNumContent(t.getContentNumber());
                excelError.setMessage("Không đúng định dạng dữ liệu");
                Field[] fields = excelClass.getDeclaredFields();
                generateError(cell, fields, excelError);
                t.getErrors().add(excelError);
            }
        }
    }

    private static void generateError(String cell, Field[] fields, ExcelError excelError) {
        for (Field field : fields) {
            if (field.getName().equals(cell)) {
                TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
                ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
                excelError.setTitleExcel(Arrays.asList(titleExcel.title()));
                excelError.setColNum(excelColum.colNum());
            }
        }
    }

    public static <T extends ExcelDTO<T>, R extends ExcelCollection<T>> void validateDataExcel(R r, Class<T> excelClass) throws InvocationTargetException, IllegalAccessException {
        ExcelCollectionClass collectionExcelClass = excelClass.getAnnotation(ExcelCollectionClass.class);
        Class<R> classCollection = (Class<R>) collectionExcelClass.colectionClass();
        Method[] methods = classCollection.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ValidateListError.class)) {
                Object result = method.invoke(r);
                if (result != null) {
                    r.getExcelFileErrors().addAll((ArrayList) result);
                }
            }
        }
    }
}