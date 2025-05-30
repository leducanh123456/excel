package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelMapping;
import org.example.antation.FormatExcel;
import org.example.antation.TitleExcel;
import org.example.collection.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ExcelUtil {

    private ExcelUtil() {

    }

    public static <T extends ExcelDTO<T>> int maxColum(Class<T> excelClass) {
        ExcelMapping excelMapping = excelClass.getAnnotation(ExcelMapping.class);
        if (excelMapping.maxCol() > 0) {
            return excelMapping.maxCol();
        }
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
        t.setCellInValidType(cellInValidType);
        t.setRowNumber(rowNum);
        t.setContentNumber(contentNum);
        t.setCellNotCheck(cellNotCheck);
        setDataFromExcel(row, fields, t, cellInValidType, excelClass);
        int maxCol = ExcelUtil.maxColum(excelClass);
        int lastCellNum = row.getLastCellNum();
        if (maxCol < lastCellNum - 1) {
            ExcelError excelError = new ExcelError();
            excelError.setRowNum(t.getRowNumber());
            excelError.setRowNumContent(t.getContentNumber());
            excelError.setMessage("Định dạng excel không hợp lệ (số cột vượt quá file mẫu)");
            t.getErrors().add(excelError);
        }
        return t;
    }

    protected static <T extends ExcelDTO<T>> void setDataFromExcel(Row row, Field[] fields, T t, Set<String> cellInValidType, Class<T> excelClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            int index = excelColum.colNum();
            Cell cell = row.getCell(index);
            checkCellStyle(t, field, cell);
            Class<?> fieldType = field.getType();
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
            } else {
                setDateFromExcel(excelClass, t, cellInValidType, field, cell, fieldType);
            }

        }
    }

    private static <T extends ExcelDTO<T>> void checkCellStyle(T t, Field field, Cell cell) {
        FormatExcel formatExcel = field.getAnnotation(FormatExcel.class);
        String[] formatConfig = formatExcel.format();
        String cellStyle = cell.getCellStyle().getDataFormatString();
        int k = 0;
        for (String tmp : formatConfig) {
            if (tmp.equals(cellStyle)) {
                k++;
                break;
            }
        }
        if (k == 0) {
            ExcelError excelError = new ExcelError();
            excelError.setRowNum(t.getRowNumber());
            TitleExcel titleExcel = field.getAnnotation(TitleExcel.class);
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            excelError.setTitleExcel(Arrays.asList(titleExcel.title()));
            excelError.setColNum(excelColum.colNum());
            excelError.setRowNumContent(t.getContentNumber());
            excelError.setMessage(formatExcel.message());
            t.getErrors().add(excelError);
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
