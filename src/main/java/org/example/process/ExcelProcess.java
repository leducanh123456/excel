package org.example.process;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.antation.ExcelCollectionClass;
import org.example.collection.ExcelCollection;
import org.example.dto.ExcelDTO;
import org.example.exception.DefineExcelException;
import org.example.exception.ExcelNotValidException;
import org.example.util.*;
import org.springframework.validation.Validator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

@Data
@Slf4j
public class ExcelProcess<T extends ExcelDTO<T>, R extends ExcelCollection<T>> {
    protected final Class<T> tClass;
    protected final Validator validator;
    protected R excelCollection;

    @SuppressWarnings("unchecked")
    public ExcelProcess(Class<T> tClass, Validator validator) {
        try {
            ExcelCollectionClass collectionExcelClass = tClass.getAnnotation(ExcelCollectionClass.class);
            Class<R> rClass = (Class<R>) collectionExcelClass.colectionClass();
            Constructor<R> constructor = rClass.getConstructor();
            R objectCollection = constructor.newInstance();
            if (rClass.isInstance(objectCollection)) {
                this.setExcelCollection(objectCollection);
            }
            this.tClass = tClass;
            this.validator = validator;
            Predicate<Class<T>> checkValid = getClassPredicate();
            Predicate<Class<R>> checkValidCollection = ValidateMethodErrorUtil::validateListError;
            if (!(checkValid.test(tClass) && checkValidCollection.test((Class<R>) excelCollection.getClass()))) {
                throw new ExcelNotValidException("Config object excel invalid");
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            String message = e.getMessage();
            log.error("ExcelProcess constructor : {}", message);
            throw new DefineExcelException("Not init object read excel, config invalid");
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
                .and(ValidateTitleExcelUtil::checkListTitleAndColAndRow)
                .and(ValidateMethodErrorUtil::validateSingleError);
    }

    public R getListFromExcel(Sheet sheet) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExcelUtil.getListObjectFromExcel(sheet, tClass, excelCollection);
        ProcessExcelUtil.validateData(excelCollection.getData(), validator, tClass);
        ProcessExcelUtil.validateDataExcel(excelCollection, tClass);
        ProcessExcelUtil.checkPrimary(excelCollection.getData(), tClass);
        return excelCollection;
    }

    public Boolean checkHeaderExcel(Sheet sheet) {
        return ValidateTitleExcelUtil.checkHeaderExcel(tClass, sheet);
    }
}
