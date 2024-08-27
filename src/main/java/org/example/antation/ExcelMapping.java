package org.example.antation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExcelMapping {
    /**
     *
     * uu tien hon cau hinh ben duoi
     */
    boolean readAllSheet() default false;

    int[] readSheet() default {0};

    int startRow() default 0;

    /**
     *
     * 0 tức là không cấu hinh
     */
    int excelMaxRow() default 0;

    boolean isResourceFolder() default false;
    String path() default "";
}
