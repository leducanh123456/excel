package org.example.antation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColum {
    String title() default "";

    String comment() default "";

    int colNum() default 0;

    int level() default 0;

    String path() default "";

    boolean isMergeRow() default false;

    boolean isMergeColum() default false;
}
