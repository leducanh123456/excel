package org.example.antation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelClass {

    boolean readAllSheet() default false;

    int[] readSheet() default {0};

    int startRow() default 0;

    int excelMaxRow() default -1;

    boolean isResourceFolder() default false;
    String path() default "";
}
