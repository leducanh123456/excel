package org.example.antation;

import org.example.composite.ExcelCollection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CollectionExcelClass {
    Class<? extends ExcelCollection> CompositeClass() default ExcelCollection.class;
}
