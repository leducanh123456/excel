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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<ExcelError> getExcelErrors() {
        return excelErrors;
    }

    public void setExcelErrors(List<ExcelError> excelErrors) {
        this.excelErrors = excelErrors;
    }

    public List<ExcelError> getExcelFileErrors() {
        return excelFileErrors;
    }

    public void setExcelFileErrors(List<ExcelError> excelFileErrors) {
        this.excelFileErrors = excelFileErrors;
    }

    public Boolean excelIsError() {
        return !this.getExcelErrors().isEmpty() || !this.getExcelFileErrors().isEmpty();
    }

    public List<ExcelError> getAllError() {
        this.getExcelErrors().addAll(this.getExcelFileErrors());
        return this.getExcelErrors();
    }

}
