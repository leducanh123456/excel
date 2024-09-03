package org.example.collection;

import lombok.Data;
import org.example.dto.ExcelError;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelCollection<T> {
    private List<T> data = new ArrayList<>();

    private List<ExcelError> excelErrors = new ArrayList<>();

    private List<ExcelError> excelFileErrors = new ArrayList<>();

    public Boolean excelIsError() {
        return !this.getExcelErrors().isEmpty() || !this.getExcelFileErrors().isEmpty();
    }

    public List<ExcelError> getAllError() {
        this.getExcelErrors().addAll(this.getExcelFileErrors());
        return this.getExcelErrors();
    }
}
