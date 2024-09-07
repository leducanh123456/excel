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

    public static <T extends ExcelDTO<T>> Boolean validateSingleError(Class<T> excelClass) {

        Method[] methods = excelClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(ValidateSingleError.class)) {
                Class<?> returnType = methods[i].getReturnType();
                if (!ExcelError.class.isAssignableFrom(returnType)) {
                    log.error("method {} invalid", methods[i].getName());
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public static <R> Boolean validateListError(Class<R> collectionClass) {
        Method[] methods = collectionClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(ValidateListError.class)) {
                if (!isValidMethodReturnType(method)) {
                    log.error("Method {} has invalid return type", method.getName());
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    private static boolean isValidMethodReturnType(Method method) {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType paramType) {
            return isValidParameterizedType(paramType, method);
        }
        log.error("Method {} does not return a parameterized type", method.getName());
        return Boolean.FALSE;
    }

    private static boolean isValidParameterizedType(ParameterizedType paramType, Method method) {
        Type rawType = paramType.getRawType();
        if (rawType instanceof Class && List.class.isAssignableFrom((Class<?>) rawType)) {
            Type[] typeArguments = paramType.getActualTypeArguments();
            return hasValidTypeArgument(typeArguments, method);
        }
        return Boolean.FALSE;
    }

    private static boolean hasValidTypeArgument(Type[] typeArguments, Method method) {
        if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?> listType) {
            if (!ExcelError.class.isAssignableFrom(listType)) {
                log.error("Method {} has invalid return type", method.getName());
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
