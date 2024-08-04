package org.example.antation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColum {
    String comment() default "";

    int colNum() default 0;

    boolean isMergeRow() default false;

    boolean isMergeColum() default false;
}
