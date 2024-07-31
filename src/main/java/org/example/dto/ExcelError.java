package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelError {
    //dòng lỗi
    private Integer rowNum;
    //message lỗi
    private String message;
}