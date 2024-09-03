package org.example.process;

import lombok.Data;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.antation.ExcelCollectionClass;
import org.example.collection.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.exception.ExcelNotValidException;
import org.example.util.*;
import org.springframework.validation.Validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

@Data
public class ExcelProcess<T extends ExcelDTO<T>, R extends ExcelCollection<T>> {
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
    public ExcelProcess(Class<T> tClass, Validator validator) {
        try {
            ExcelCollectionClass collectionExcelClass = tClass.getAnnotation(ExcelCollectionClass.class);
            Class<? extends ExcelCollection<? extends ExcelDTO<?>>> rClass = collectionExcelClass.colectionClass();
            Constructor<? extends ExcelCollection<? extends ExcelDTO<?>>> constructor = rClass.getConstructor();
            ExcelCollection<? extends ExcelDTO<?>> objectCollection = constructor.newInstance();
            if (rClass.isInstance(objectCollection)) {
                this.setExcelCollection((R) objectCollection);
            }
            this.tClass = tClass;
            this.validator = validator;
            Predicate<Class<T>> checkValid = getClassPredicate();
            if (!checkValid.test(tClass)) {
                throw new ExcelNotValidException("Cấu hình đọc excel không hợp lệ");
            }
        } catch (Exception e) {
            throw new RuntimeException("Khong the khoi tao cac doi tuong do cau hinh sai");
        }
    }

    private static <T extends ExcelDTO<T>> Predicate<Class<T>> getClassPredicate() {
        Predicate<Class<T>> predicate = ValidateExcelMappingUtil::checkAnnotationClass;
        return predicate
                .and(ValidateExcelMappingUtil::checkExcelMaxRow)
                .and(ValidateExcelMappingUtil::checkExcelConfigPath)
                .and(ValidateExcelMappingUtil::checkExcelValidReadSheet)
                .and(ValidateExcelMappingUtil::checkExcelStartRow)
                .and(ValidateExcelMappingUtil::checkExcelSMaxRow)
                .and(ValidateExcelColumUtil::checkAllFieldIsAnnotation)
                .and(ValidateExcelColumUtil::checkDuplicateAnnotationExcelColum)
                .and(ValidateTitleExcelUtil::checkAllFieldIsAnnotation)
                .and(ValidateTitleExcelUtil::checkListTitleAndColAndRow);
    }

    public R getListFromExcel(Workbook workbook) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int[] readSheets = ExcelUtil.getReadSheet(workbook, tClass);
        for (int i = 0; i < readSheets.length; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            ExcelUtil.getListObjectFromExcel(sheet, tClass, excelCollection);
        }
        ProcessExcelUtil.validateData(excelCollection.getData(), validator, tClass);
        ProcessExcelUtil.validateDataExcel(excelCollection, tClass);
        ProcessExcelUtil.checkPrimary(excelCollection.getData(), tClass);
        return excelCollection;
    }
}
