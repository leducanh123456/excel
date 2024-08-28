package org.example.antation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TitleExcel {
    int[] colNum() default {};

    int[] rowNum() default {};

    String[] title() default {};

}
