package org.example.antation;

import org.example.collection.ExcelCollection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCollectionClass {
    Class<? extends ExcelCollection> colectionClass() default ExcelCollection.class;
}
