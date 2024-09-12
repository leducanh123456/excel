package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.example.antation.ValidateListError;
import org.example.antation.ValidateSingleError;
import org.example.dto.ExcelDTO;
import org.example.dto.ExcelError;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class ValidateMethodErrorUtil {

    private ValidateMethodErrorUtil() {
    }

    public static <T extends ExcelDTO<T>> Boolean validateSingleError(Class<T> excelClass) {

        Method[] methods = excelClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ValidateSingleError.class)) {
                Class<?> returnType = method.getReturnType();
                if (!ExcelError.class.isAssignableFrom(returnType)) {
                    log.error("method {} invalid", method.getName());
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public static <R> Boolean validateListError(Class<R> collectionClass) {
        Method[] methods = collectionClass.getMethods();
        for (Method method : methods) {
            boolean tmp = isValidMethodReturnType(method);
            if (method.isAnnotationPresent(ValidateListError.class) && !tmp) {
                log.error("Method {} has invalid return type", method.getName());
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    private static Boolean isValidMethodReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        Boolean tmp = Boolean.FALSE;
        if (returnType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) returnType; // Explicitly cast
            tmp = isValidParameterizedType(paramType, method);
        }
        log.error("Method {} does not return a parameterized type", method.getName());
        return tmp;
    }

    private static Boolean isValidParameterizedType(ParameterizedType paramType, Method method) {
        Type rawType = paramType.getRawType();
        Boolean tmp = Boolean.FALSE;
        if (rawType instanceof Class && List.class.isAssignableFrom((Class<?>) rawType)) {
            Type[] typeArguments = paramType.getActualTypeArguments();
            tmp = hasValidTypeArgument(typeArguments, method);
        }
        return tmp;
    }

    private static Boolean hasValidTypeArgument(Type[] typeArguments, Method method) {
        if (typeArguments.length > 0 && typeArguments[0] instanceof Class) {
            Class<?> listType = (Class<?>) typeArguments[0]; // Explicitly cast
            if (!ExcelError.class.isAssignableFrom(listType)) {
                log.error("Method {} has invalid return type", method.getName());
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
