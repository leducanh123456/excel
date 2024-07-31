package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelPath;
import org.example.antation.ExcelRowStart;
import org.example.dto.ExcelDTO;
import org.example.dto.UserDTO;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public T getObjectFromExcel(Row row) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        T t = excelClass.getDeclaredConstructor().newInstance();
        Field[] fields = excelClass.getDeclaredFields();
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
            switch (cell.getCellType()) {
                case STRING:
                    if (fieldType.equals(String.class)) {
                        field.set(t, cell.getStringCellValue());
                    }
                    break;
                case NUMERIC:
                    if (fieldType.equals(Integer.class)) {
                        field.set(t, (int) cell.getNumericCellValue()) ;
                    } else if (fieldType.equals(Double.class)) {
                        field.set(t, cell.getNumericCellValue());
                    } else if (fieldType.equals(Float.class)) {
                        field.set(t, (float) cell.getNumericCellValue());
                    } else if (fieldType.equals(BigDecimal.class)) {
                        field.set(t, BigDecimal.valueOf(cell.getNumericCellValue()));
                    } else if (fieldType.equals(Date.class)) {
                        field.set(t, cell.getDateCellValue());
                    } else if (fieldType.equals(LocalDate.class)) {
                        Date date = cell.getDateCellValue();
                        field.set(t, date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
                    } else if (fieldType.equals(LocalDateTime.class)) {
                        Date date = cell.getDateCellValue();
                        field.set(t, date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                    }
                    break;
                case BOOLEAN:
                    if (fieldType.equals(Boolean.class)) {
                        field.set(t, cell.getBooleanCellValue());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported cell type: " + cell.getCellType());
            }
        }
        return t;
    }

    public List<T> getListObjectFromExcel(Sheet sheet) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        ExcelRowStart rowStart = excelClass.getAnnotation(ExcelRowStart.class);
        if(ObjectUtils.isEmpty(rowStart)){
            throw new RuntimeException("Không tồn tại row start");
        }
        for (Row row : sheet) {
            if(row.getRowNum() < rowStart.startRow()) {
                continue;
            }
            T t = getObjectFromExcel(row);
            list.add(t);
        }
        return list;
    }

    public static void main(String[] args) {
        ExcelUtilImpl<UserDTO> excelUtil = new ExcelUtilImpl<>(UserDTO.class);
        Optional<String> pathExcel = excelUtil.getPath();
        try (InputStream inputStream = ExcelUtilImpl.class.getClassLoader().getResourceAsStream(pathExcel.get())) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            List<UserDTO> userDTOS = excelUtil.getListObjectFromExcel(sheet);
            System.out.println("aaaaaa");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
