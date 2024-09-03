package org.example.antation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColum {
    String comment() default "";

    int colNum() default 0;

    boolean isMergeRow() default false;

    boolean isMergeColum() default false;
}
