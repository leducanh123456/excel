package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.collection.ExcelCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class ExcelDTO<T> {
    private ExcelCollection<T> excelCollection;
    private Integer rowNumber;
    private Integer contentNumber;
    private Set<String> cellNotCheck;
    private Set<String> cellInValidType;
    private List<String> titles;
    private List<ExcelError> errors = new ArrayList<>();

    public ExcelCollection<T> getExcelCollection() {
        return excelCollection;
    }

    public void setExcelCollection(ExcelCollection<T> excelCollection) {
        this.excelCollection = excelCollection;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getContentNumber() {
        return contentNumber;
    }

    public void setContentNumber(Integer contentNumber) {
        this.contentNumber = contentNumber;
    }

    public Set<String> getCellNotCheck() {
        return cellNotCheck;
    }

    public void setCellNotCheck(Set<String> cellNotCheck) {
        this.cellNotCheck = cellNotCheck;
    }

    public Set<String> getCellInValidType() {
        return cellInValidType;
    }

    public void setCellInValidType(Set<String> cellInValidType) {
        this.cellInValidType = cellInValidType;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<ExcelError> getErrors() {
        return errors;
    }

    public void setErrors(List<ExcelError> errors) {
        this.errors = errors;
    }
}
