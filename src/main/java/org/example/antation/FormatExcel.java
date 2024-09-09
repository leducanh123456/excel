package org.example.antation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormatExcel {
    String[] format() default {};

    String message() default "Dữ liệu không đúng định dạng";
}
