package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelError {
    private Integer rowNum;
    private Integer rowNumContent;
    private List<String> titleExcel;
    private Integer colNum;
    private String message;
}