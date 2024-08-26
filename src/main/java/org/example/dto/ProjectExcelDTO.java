package org.example.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.antation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ExcelClass
public class ProjectExcelDTO extends ExcelDTO {

    @NotEmpty(message = "sapCode không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "sapCode chỉ bao gồm số và chữ")
    @ExcelColum(colNum = 0)
    @ExcelPrimary
    @TitleExcel(title = {"sapCode"}, rowNum = {0}, colNum = {0})
    private String sapCode;

    @NotEmpty(message = "projectName không được để trống")
    @ExcelColum(colNum = 1)
    @TitleExcel(title = {"projectName"}, rowNum = {0}, colNum = {0})
    private String projectName;

    @ExcelColum(colNum = 2)
    @TitleExcel(title = {"budget"}, rowNum = {0}, colNum = {0})
    @DecimalMin(value = "0.00", inclusive = true, message = "Value must be at least 0.00")
    @DecimalMax(value = "1000.00", inclusive = true, message = "Value must be no more than 1000.00")
    @ExcelPrimary
    private BigDecimal budget;

    @ExcelColum(colNum = 3)
    @TitleExcel(title = {"startDate"}, rowNum = {0}, colNum = {0})
    @ExcelPrimary
    private Date startDate;

    @ExcelColum(colNum = 4)
    @TitleExcel(title = {"endDate"}, rowNum = {0}, colNum = {0})
    private Date endDate;

    @ExcelColum(colNum = 5)
    @TitleExcel(title = {"total"}, rowNum = {0}, colNum = {0})
    private Integer total;

    @NotEmpty(message = "budget không được để trống")
    @ExcelColum(colNum = 6)
    @TitleExcel(title = {"code"}, rowNum = {0}, colNum = {0})
    private String code;

    //Policy
    @ExcelColum(colNum = 7)
    @TitleExcel(title = {"startDatePolicy"}, rowNum = {0}, colNum = {0})
    private Date startDatePolicy;

    @ExcelColum(colNum = 8)
    @TitleExcel(title = {"endDatePolicy"}, rowNum = {0}, colNum = {0})
    private Date endDatePolicy;

    @ExcelColum(colNum = 9)
    @TitleExcel(title = {"budgetPolicy"}, rowNum = {0}, colNum = {0})
    private Integer budgetPolicy;

    @ExcelColum(colNum = 10)
    @TitleExcel(title = {"codePolicy"}, rowNum = {0}, colNum = {0})
    @ExcelPrimary
    private String codePolicy;

    //Approval
    @ExcelColum(colNum = 11)
    @TitleExcel(title = {"startDateApproval"}, rowNum = {0}, colNum = {0})
    @ExcelPrimary
    private LocalDate startDateApproval;

    @ExcelColum(colNum = 12)
    @TitleExcel(title = {"endDateApproval"}, rowNum = {0}, colNum = {0})
    private LocalDate endDateApproval;

    @ExcelColum(colNum = 13)
    @TitleExcel(title = {"totalApproval"}, rowNum = {0}, colNum = {0})
    private Integer totalApproval;

    @NotEmpty(message = "budget không được để trống")
    @ExcelColum(colNum = 14)
    @TitleExcel(title = {"codeApproval"}, rowNum = {0}, colNum = {0})
    @ExcelPrimary
    private String codeApproval;
}
