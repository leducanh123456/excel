package org.example.antation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelExportColum {
    int colum() default 0;
    boolean isMergeHeader() default false;

    int[] mergeColum() default {};

    String[] mergeTitle() default {};


}
