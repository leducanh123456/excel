package org.example.validate;

import org.apache.poi.ss.usermodel.Workbook;
import org.example.dto.ExcelDTO;
import org.example.exception.ExcelNotValidException;
import org.example.util.ValidateExcelColum;
import org.example.util.ValidateExcelMapping;
import org.example.util.ValidateTitleExcel;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Predicate;

public abstract class ExcelData<T extends ExcelDTO> {
    protected final Class<T> tClass;

    public ExcelData(Class<T> tClass) {
        this.tClass = tClass;
        Predicate<Class<T>> predicate = ValidateExcelMapping::checkAnnotationClass;
        Predicate<Class<T>> checkValid = predicate
                .and(ValidateExcelMapping::checkExcelMaxRow)
                .and(ValidateExcelMapping::checkExcelConfigPath)
                .and(ValidateExcelMapping::checkExcelValidReadSheet)
                .and(ValidateExcelMapping::checkExcelStartRow)
                .and(ValidateExcelMapping::checkExcelSMaxRow)
                .and(ValidateExcelColum::checkAllFieldIsAnnotation)
                .and(ValidateExcelColum::checkDuplicateAnnotationExcelColum)
                .and(ValidateTitleExcel::checkAllFieldIsAnnotation);
        if (!checkValid.test(tClass)) {
            throw new ExcelNotValidException("Cấu hình đọc excel không hợp lệ");
        }
    }

    public List<T> getListFromExcel(Workbook workbook) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return null;
    }
}
