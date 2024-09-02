package org.example.validate;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.composite.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.exception.ExcelNotValidException;
import org.example.util.*;
import org.springframework.validation.Validator;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

public abstract class ExcelData<T extends ExcelDTO, R extends ExcelCollection<T>> {
    protected final Class<T> tClass;
    protected final Validator validator;
    protected R excelCollection;

    /**
     * check class truoc
     * check col sau
     * check title sau cung
     *
     * @param tClass
     */
    public ExcelData(R excelCollection, Class<T> tClass, Validator validator) {
        this.excelCollection = excelCollection;
        this.tClass = tClass;
        this.validator = validator;
        Predicate<Class<T>> predicate = ValidateExcelMapping::checkAnnotationClass;
        Predicate<Class<T>> checkValid = predicate
                .and(ValidateExcelMapping::checkExcelMaxRow)
                .and(ValidateExcelMapping::checkExcelConfigPath)
                .and(ValidateExcelMapping::checkExcelValidReadSheet)
                .and(ValidateExcelMapping::checkExcelStartRow)
                .and(ValidateExcelMapping::checkExcelSMaxRow)
                .and(ValidateExcelColum::checkAllFieldIsAnnotation)
                .and(ValidateExcelColum::checkDuplicateAnnotationExcelColum)
                .and(ValidateTitleExcel::checkAllFieldIsAnnotation)
                .and(ValidateTitleExcel::checkListTitleAndColAndRow);
        if (!checkValid.test(tClass)) {
            throw new ExcelNotValidException("Cấu hình đọc excel không hợp lệ");
        }
    }

    public void getListFromExcel(Workbook workbook) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int[] readSheets = ExcelUtil.getReadSheet(workbook, tClass);
        for (int i = 0; i < readSheets.length; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            ExcelUtil.getListObjectFromExcel(sheet, tClass, excelCollection);
        }
        ValidateExcel.validateData(excelCollection.getData(), validator, tClass);
        ValidateExcel.validateDataExcel(excelCollection, tClass);
        ValidateExcel.checkPrimary(excelCollection.getData(), tClass);
    }
}
