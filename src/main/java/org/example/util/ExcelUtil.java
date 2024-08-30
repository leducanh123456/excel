package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.example.antation.CheckIfNotNull;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelMapping;
import org.example.dto.ExcelDTO;
import org.example.exception.NoSheetException;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class ExcelUtil {

    private ExcelUtil() {

    }

    public static <T extends ExcelDTO> Boolean checkValidConfigRowData(List<T> excelDTOS) {
        if (ObjectUtils.isEmpty(excelDTOS)) {
            return Boolean.FALSE;
        }
        excelDTOS.stream().parallel().anyMatch(tmp -> ObjectUtils.isEmpty(tmp.getRowNumber()) || ObjectUtils.isEmpty(tmp.getContentNumber()) || tmp.getRowNumber().compareTo(0) < 0 || tmp.getContentNumber().compareTo(0) < 0);
        return Boolean.TRUE;
    }

    public static <T extends ExcelDTO> Optional<String> getPath(Class<T> excelClass) {
        if (ValidateExcelMapping.checkExcelConfigPath(excelClass)) {
            ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
            return Optional.of(annotation.path());
        }
        return Optional.empty();
    }

    public static <T extends ExcelDTO> Integer getRowStart(Class<T> excelClass) {
        ExcelMapping annotation = excelClass.getAnnotation(ExcelMapping.class);
        return annotation.startRow();
    }

    public static Optional<String> getTitle() {

        return Optional.empty();
    }

    public static <T extends ExcelDTO> T getObjectFromExcel(Row row, int rowNum, int contentNum, Class<T> excelClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = excelClass.getDeclaredConstructor().newInstance();
        Field[] fields = excelClass.getDeclaredFields();
        Set<String> cellInValidType = new HashSet<>();
        Set<String> cellNotCheck = new HashSet<>();
        for (Field field : fields) {
            ExcelColum excelColum = field.getAnnotation(ExcelColum.class);
            CheckIfNotNull checkIfNotNull = field.getAnnotation(CheckIfNotNull.class);
            if (checkIfNotNull != null) {
                cellNotCheck.add(field.getName());
            }
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

    public static <T extends ExcelDTO> List<T> getListObjectFromExcel(Sheet sheet, Class<T> excelClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        ExcelMapping rowStart = excelClass.getAnnotation(ExcelMapping.class);
        if (ObjectUtils.isEmpty(rowStart)) {
            throw new RuntimeException("Không tồn tại row start");
        }
        for (Row row : sheet) {
            if (row.getRowNum() < rowStart.startRow()) {
                continue;
            }
            T t = getObjectFromExcel(row, row.getRowNum(), row.getRowNum() - rowStart.startRow(), excelClass);
            list.add(t);
        }
        return list;
    }

    public static <T extends ExcelDTO> int[] getReadSheet(Workbook workbook, Class<T> excelClass) {
        if (workbook.getNumberOfSheets() <= 0) {
            throw new NoSheetException("Sheet không tồn tại");
        }
        ExcelMapping excelMapping = excelClass.getAnnotation(ExcelMapping.class);
        if (excelMapping.readAllSheet()) {
            int[] sheets = new int[workbook.getNumberOfSheets()];
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheets[i] = i;
            }
            return sheets;
        }
        return excelMapping.readSheet();
    }
}
