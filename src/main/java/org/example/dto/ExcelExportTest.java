package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.antation.ExcelExport;
import org.example.antation.ExcelExportColum;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ExcelExport(rowStart = 10)
@Data
public class ExcelExportTest {

    @ExcelExportColum(colum = 0)
    private String columStr;

    @ExcelExportColum(colum = 1)
    private Integer columInteger;

    @ExcelExportColum(colum = 2)
    private Long columLong;

    @ExcelExportColum(colum = 3)
    private Date columDate;

    @ExcelExportColum(colum = 4)
    private LocalDateTime columLocalDateTime;
}
