package org.example.validate;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.dto.ExcelDTO;
import org.example.exception.ExcelNotValidException;
import org.example.util.*;
import org.springframework.validation.Validator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ExcelData<T extends ExcelDTO> {
    protected final Class<T> tClass;
    protected final Validator validator;

    /**
     * check class truoc
     * check col sau
     * check title sau cung
     *
     * @param tClass
     */
    public ExcelData(Class<T> tClass, Validator validator) {
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

    public List<T> getListFromExcel(Workbook workbook) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int[] readSheets = ExcelUtil.getReadSheet(workbook, tClass);
        List<T> data = new ArrayList<>();
        for (int i = 0; i < readSheets.length; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            List<T> dataTmp = ExcelUtil.getListObjectFromExcel(sheet, tClass);
            data.addAll(dataTmp);
        }
        ValidateExcel.validateData(data, validator, tClass);
        ValidateExcel.checkPrimary(data, tClass);
        return data;
    }
    // lấy danh sách Lỗi

}
