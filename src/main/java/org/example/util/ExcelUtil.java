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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public static <T extends ExcelDTO<T>> T getObjectFromExcel(Row row, int rowNum, int contentNum, Class<T> excelClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = excelClass.getDeclaredConstructor().newInstance();
        Field[] fields = excelClass.getDeclaredFields();
        Set<String> cellInValidType = new HashSet<>();
        Set<String> cellNotCheck = new HashSet<>();
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            field.setAccessible(true);
            int index = excelColum.colNum();
            Cell cell = row.getCell(index);

            Class<?> fieldType = field.getType();
            if (cell == null) {
                field.set(t, null);
                continue;
            }
            if (fieldType.equals(String.class) && cell.getCellType().equals(CellType.STRING)) {
                field.set(t, cell.getStringCellValue());
            } else if (fieldType.equals(Integer.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                field.set(t, (int) cell.getNumericCellValue());
            } else if (fieldType.equals(Double.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                field.set(t, cell.getNumericCellValue());
            } else if (fieldType.equals(Float.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                field.set(t, (float) cell.getNumericCellValue());
            } else if (fieldType.equals(BigDecimal.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                field.set(t, BigDecimal.valueOf(cell.getNumericCellValue()));
            } else if (fieldType.equals(Date.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                field.set(t, org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()));
            } else if (fieldType.equals(LocalDate.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                Date date = cell.getDateCellValue();
                field.set(t, date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            } else if (fieldType.equals(LocalDateTime.class) && cell.getCellType().equals(CellType.NUMERIC)) {
                Date date = cell.getDateCellValue();
                field.set(t, date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            } else if (fieldType.equals(Boolean.class) && cell.getCellType().equals(CellType.BOOLEAN)) {
                field.set(t, cell.getBooleanCellValue());
            } else if (!cell.getCellType().equals(CellType.BLANK)) {
                cellInValidType.add(field.getName());
            }
        }
        t.setCellInValidType(cellInValidType);
        t.setRowNumber(rowNum);
        t.setContentNumber(contentNum);
        t.setCellNotCheck(cellNotCheck);
        return t;
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
}
