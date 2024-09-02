package org.example.composite;

import lombok.Data;
import org.example.dto.ExcelError;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelCollection<T> {
    private final List<T> data;

    private List<ExcelError> excelErrors = new ArrayList<>();

    private List<ExcelError> excelFileErrors = new ArrayList<>();

    public ExcelCollection(List<T> data) {
        this.data = data;
    }
}
