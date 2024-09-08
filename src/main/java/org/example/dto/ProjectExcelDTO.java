package org.example.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.antation.*;
import org.example.collection.ProjectExcelCollection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ExcelMapping(startRow = 1)
@ExcelCollectionClass(colectionClass = ProjectExcelCollection.class)
public class ProjectExcelDTO extends ExcelDTO<ProjectExcelDTO> {

    @NotEmpty(message = "sapCode không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "sapCode chỉ bao gồm số và chữ")
    @ExcelColum
    @ExcelPrimary
    @TitleExcel(title = {"sapCode"}, rowNum = {0}, colNum = {0})
    public String sapCode;

    @NotEmpty(message = "projectName không được để trống")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "sapCode chỉ bao gồm số và chữ")
    @ExcelColum(colNum = 1)
    @TitleExcel(title = {"projectName"}, rowNum = {0}, colNum = {1})
    public String projectName;

    @ExcelColum(colNum = 2)
    @TitleExcel(title = {"budget"}, rowNum = {0}, colNum = {2})
    @DecimalMin(value = "0.00", message = "Value must be at least 0.00")
    @DecimalMax(value = "1000.00", message = "Value must be no more than 1000.00")
    @ExcelPrimary
    public BigDecimal budget;

    @ExcelColum(colNum = 3)
    @TitleExcel(title = {"startDate"}, rowNum = {0}, colNum = {3})
    @ExcelPrimary
    public Date startDate;

    @ExcelColum(colNum = 4)
    @TitleExcel(title = {"endDate"}, rowNum = {0}, colNum = {4})
    public Date endDate;

    @ExcelColum(colNum = 5)
    @TitleExcel(title = {"total"}, rowNum = {0}, colNum = {5})
    public Integer total;

    @NotEmpty(message = "budget không được để trống")
    @ExcelColum(colNum = 6)
    @TitleExcel(title = {"code"}, rowNum = {0}, colNum = {6})
    public String code;

    //Policy
    @ExcelColum(colNum = 7)
    @TitleExcel(title = {"startDatePolicy"}, rowNum = {0}, colNum = {7})
    public Date startDatePolicy;

    @ExcelColum(colNum = 8)
    @TitleExcel(title = {"endDatePolicy"}, rowNum = {0}, colNum = {8})
    public Date endDatePolicy;

    @ExcelColum(colNum = 9)
    @TitleExcel(title = {"budgetPolicy"}, rowNum = {0}, colNum = {9})
    public Integer budgetPolicy;

    @ExcelColum(colNum = 10)
    @TitleExcel(title = {"codePolicy"}, rowNum = {0}, colNum = {10})
    @ExcelPrimary
    public String codePolicy;

    //Approval
    @ExcelColum(colNum = 11)
    @TitleExcel(title = {"startDateApproval"}, rowNum = {0}, colNum = {11})
    @ExcelPrimary
    public LocalDate startDateApproval;

    @ExcelColum(colNum = 12)
    @TitleExcel(title = {"endDateApproval"}, rowNum = {0}, colNum = {12})
    public LocalDate endDateApproval;

    @ExcelColum(colNum = 13)
    @TitleExcel(title = {"totalApproval"}, rowNum = {0}, colNum = {13})
    public Integer totalApproval;

    @NotEmpty(message = "budget không được để trống")
    @ExcelColum(colNum = 14)
    @TitleExcel(title = {"codeApproval"}, rowNum = {0}, colNum = {14})
    @ExcelPrimary
    public String codeApproval;

    @ValidateSingleError
    @SuppressWarnings({"squid:S1144", "unused"})
    public ExcelError validateDateApproval() {
        ExcelError excelError;
        if (startDateApproval != null && endDateApproval != null) {
            if (startDateApproval.isEqual(endDateApproval) || startDateApproval.isAfter(endDateApproval)) {
                excelError = new ExcelError();
                excelError.setTitleExcel(List.of("startDateApproval"));
                excelError.setMessage("start date approval khong hop le");
                excelError.setRowNum(this.getRowNumber());
                excelError.setRowNumContent(this.getContentNumber());
                excelError.setColNum(11);
                return excelError;
            }
        }
        return null;
    }


}
