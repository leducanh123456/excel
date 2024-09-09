package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelMapping;
import org.example.collection.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ExcelUtil {

    private ExcelUtil() {

    }

    public static <T extends ExcelDTO<T>> int maxColum(Class<T> excelClass) {
        Field[] fields = excelClass.getDeclaredFields();
        int col = 0;
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            int index = excelColum.colNum();
            if (col < index) {
                col = index;
            }
        }
        return col;
    }

    public static <T extends ExcelDTO<T>> T getObjectFromExcel(Row row, int rowNum, int contentNum, Class<T> excelClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = excelClass.getDeclaredConstructor().newInstance();
        Field[] fields = excelClass.getDeclaredFields();
        Set<String> cellInValidType = new HashSet<>();
        Set<String> cellNotCheck = new HashSet<>();
        setDataFromExcel(row, fields, t, cellInValidType, excelClass);
        int maxCol = ExcelUtil.maxColum(excelClass);
        int lastCellNum = row.getLastCellNum();
        t.setCellInValidType(cellInValidType);
        t.setRowNumber(rowNum);
        t.setContentNumber(contentNum);
        t.setCellNotCheck(cellNotCheck);
        if (maxCol < lastCellNum) {
            ExcelError excelError = new ExcelError();
            excelError.setRowNum(t.getRowNumber());
            excelError.setRowNumContent(t.getContentNumber());
            excelError.setMessage("Số cột không hợp lệ");
            t.getErrors().add(excelError);
        }
        return t;
    }

    protected static <T extends ExcelDTO<T>> void setDataFromExcel(Row row, Field[] fields, T t, Set<String> cellInValidType, Class<T> excelClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            int index = excelColum.colNum();
            Cell cell = row.getCell(index);
            Class<?> fieldType = field.getType();
            if (cell == null) {
                continue;
            }
            if (fieldType.equals(String.class) && cell.getCellType().equals(CellType.STRING)) {
                Method setter = excelClass.getMethod("set" + capitalize(field.getName()), String.class);
                setter.invoke(t, cell.getStringCellValue());
            } else if (fieldType.equals(Integer.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                Method setter = excelClass.getMethod("set" + capitalize(field.getName()), Integer.class);
                setter.invoke(t, (int) cell.getNumericCellValue());
            } else if (fieldType.equals(Double.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                Method setter = excelClass.getMethod("set" + capitalize(field.getName()), Double.class);
                setter.invoke(t, cell.getNumericCellValue());
            } else if (fieldType.equals(Float.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                Method setter = excelClass.getMethod("set" + capitalize(field.getName()), Float.class);
                setter.invoke(t, (float) cell.getNumericCellValue());
            } else if (fieldType.equals(BigDecimal.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                Method setter = excelClass.getMethod("set" + capitalize(field.getName()), BigDecimal.class);
                setter.invoke(t, BigDecimal.valueOf(cell.getNumericCellValue()));
            }
            setDateFromExcel(excelClass, t, cellInValidType, field, cell, fieldType);

        }
    }

    protected static <T extends ExcelDTO<T>> void setDateFromExcel(Class<T> excelClass, T t, Set<String> cellInValidType, Field field, Cell cell, Class<?> fieldType) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (fieldType.equals(Date.class) && cell.getCellType().equals(CellType.NUMERIC)) {
            Method setter = excelClass.getMethod("set" + capitalize(field.getName()), Date.class);
            setter.invoke(t, org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()));
        } else if (fieldType.equals(LocalDate.class) && cell.getCellType().equals(CellType.NUMERIC)) {
            Date date = cell.getDateCellValue();
            Method setter = excelClass.getMethod("set" + capitalize(field.getName()), LocalDate.class);
            setter.invoke(t, date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        } else if (fieldType.equals(LocalDateTime.class) && cell.getCellType().equals(CellType.NUMERIC)) {
            Date date = cell.getDateCellValue();
            Method setter = excelClass.getMethod("set" + capitalize(field.getName()), LocalDateTime.class);
            setter.invoke(t, date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
        } else if (fieldType.equals(Boolean.class) && cell.getCellType().equals(CellType.BOOLEAN)) {
            Method setter = excelClass.getMethod("set" + capitalize(field.getName()), Boolean.class);
            setter.invoke(t, cell.getBooleanCellValue());
        } else if (!cell.getCellType().equals(CellType.BLANK)) {
            cellInValidType.add(field.getName());
        }
    }

    public static <T extends ExcelDTO<T>, R extends ExcelCollection<T>> void getListObjectFromExcel(Sheet sheet, Class<T> excelClass, R r) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExcelMapping rowStart = excelClass.getAnnotation(ExcelMapping.class);
        for (Row row : sheet) {
            if (row.getRowNum() < rowStart.startRow()) {
                continue;
            }
            T t = getObjectFromExcel(row, row.getRowNum(), row.getRowNum() - rowStart.startRow(), excelClass);
            t.setExcelCollection(r);
            r.getData().add(t);
        }
    }

    public static <T extends ExcelDTO<T>> void setDataStringToField(Class<T> excelClass, Field field, String value, T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setter = excelClass.getMethod("set" + capitalize(field.getName()), String.class);
        setter.invoke(t, value);
    }

    public static <T extends ExcelDTO<T>> void setDataIntegerToField(Class<T> excelClass, Field field, Integer value, T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setter = excelClass.getMethod("set" + capitalize(field.getName()), Integer.class);
        setter.invoke(t, value);
    }


    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
