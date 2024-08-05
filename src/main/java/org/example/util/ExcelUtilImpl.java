package org.example.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelPath;
import org.example.antation.ExcelRowStart;
import org.example.dto.ExcelDTO;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ExcelUtilImpl<T extends ExcelDTO> implements ExcelUtil<T> {
    private final Class<T> excelClass;

    public ExcelUtilImpl(Class<T> clazz) {
        this.excelClass = clazz;
    }

    public Boolean checkValidConfigRowData(List<T> excelDTOS) {
        if (ObjectUtils.isEmpty(excelDTOS)) {
            return Boolean.FALSE;
        }
        excelDTOS.stream().parallel().anyMatch(tmp -> ObjectUtils.isEmpty(tmp.getRowNumber()) || ObjectUtils.isEmpty(tmp.getContentNumber()) || tmp.getRowNumber().compareTo(0) < 0 || tmp.getContentNumber().compareTo(0) < 0);
        return Boolean.TRUE;
    }

    public Boolean checkValidRowStart() {
        if (excelClass.isAnnotationPresent(ExcelRowStart.class)) {
            ExcelRowStart annotation = excelClass.getAnnotation(ExcelRowStart.class);
            return annotation.startRow() >= 0;
        }
        return Boolean.FALSE;
    }

    public Boolean checkExcelPath() {
        if (excelClass.isAnnotationPresent(ExcelPath.class)) {
            ExcelPath annotation = excelClass.getAnnotation(ExcelPath.class);
            return !annotation.path().trim().isEmpty();
        }
        return Boolean.FALSE;
    }

    public Optional<String> getPath() {
        if (checkExcelPath()) {
            return Optional.of(excelClass.getAnnotation(ExcelPath.class).path());
        }
        return Optional.empty();
    }

    public Integer getRowStart() {
        ExcelRowStart annotation = excelClass.getAnnotation(ExcelRowStart.class);
        return annotation.startRow();
    }

    public Optional<String> getTitle() {

        return Optional.empty();
    }

    public T getObjectFromExcel(Row row, int rowNum, int contentNum) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = excelClass.getDeclaredConstructor().newInstance();
        Field[] fields = excelClass.getDeclaredFields();
        Set<String> cellInValidType = new HashSet<>();
        Set<String> cellNotCheck = new HashSet<>();
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            // thiết lập metadata
            if (excelColum == null) {
                continue;
            }
            field.setAccessible(true);
            int index = excelColum.colNum();
            Cell cell = row.getCell(index);

            Class<?> fieldType = field.getType();
            if (cell == null) {
                field.set(t, null);
                continue;
            }
            // phải đi từ object để biết được lỗi
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
            } else {
                cellInValidType.add(field.getName());
            }
        }
        t.setCellInValidType(cellInValidType);
        t.setRowNumber(rowNum);
        t.setContentNumber(contentNum);
        return t;
    }

    public List<T> getListObjectFromExcel(Sheet sheet) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        ExcelRowStart rowStart = excelClass.getAnnotation(ExcelRowStart.class);
        if (ObjectUtils.isEmpty(rowStart)) {
            throw new RuntimeException("Không tồn tại row start");
        }
        for (Row row : sheet) {
            if (row.getRowNum() < rowStart.startRow()) {
                continue;
            }
            T t = getObjectFromExcel(row, row.getRowNum(), row.getRowNum() - rowStart.startRow());
            list.add(t);
        }
        return list;
    }
}
