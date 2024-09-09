package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
public class ExcelError {
    private Integer rowNum;
    private Integer rowNumContent;
    private List<String> titleExcel;
    private Integer colNum;
    private String message;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getRowNumContent() {
        return rowNumContent;
    }

    public void setRowNumContent(Integer rowNumContent) {
        this.rowNumContent = rowNumContent;
    }

    public List<String> getTitleExcel() {
        return titleExcel;
    }

    public void setTitleExcel(List<String> titleExcel) {
        this.titleExcel = titleExcel;
    }

    public Integer getColNum() {
        return colNum;
    }

    public void setColNum(Integer colNum) {
        this.colNum = colNum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}