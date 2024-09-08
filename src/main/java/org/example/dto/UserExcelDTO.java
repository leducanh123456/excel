package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.example.antation.*;
import org.example.collection.UserExcelCollection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ExcelMapping(startRow = 4)
@ExcelCollectionClass(colectionClass = UserExcelCollection.class)
public class UserExcelDTO extends ExcelDTO<UserExcelDTO> {

    @NotEmpty(message = "aString không được để null hoặc empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "aString chỉ bao gồm số và chữ")
    @ExcelColum
    @ExcelPrimary(message = "aString ối dồi ôi phải là duy nhất")
    @TitleExcel(title = {"aString\nối dồi ôi"}, rowNum = {0}, colNum = {0})
    public String aString;

    @NotNull(message = "aInterger không được null")
    @ExcelColum(colNum = 1)
    @TitleExcel(title = {"aInteger"}, rowNum = {0}, colNum = {1})
    public Integer aInteger;

    @NotNull(message = "aBoolean không được null")
    @ExcelColum(colNum = 2)
    @TitleExcel(title = {"aBoolean"}, rowNum = {0}, colNum = {2})
    public Boolean aBoolean;

    @NotNull(message = "aBigDecimal không được null")
    @ExcelColum(colNum = 3)
    @TitleExcel(title = {"aBigDecimal"}, rowNum = {0}, colNum = {3})
    public BigDecimal aBigDecimal;

    //nhóm cao nhất
    @NotNull(message = "aLocalDate không được null")
    @ExcelColum(colNum = 4)
    @TitleExcel(title = {"Tổng hợp 1", "aLocalDate"}, rowNum = {0, 1}, colNum = {4, 4})
    public LocalDate aLocalDate;

    // 1 nhóm nữa
    @NotNull(message = "aTerger không được null")
    @ExcelColum(colNum = 5)
    @TitleExcel(title = {"Tổng hợp 1", "Tổng hợp 2", "aLocalDateTime"}, rowNum = {0, 1, 2}, colNum = {4, 5, 5})
    public LocalDateTime aLocalDateTime;

    //1 nhóm
    @NotNull(message = "aDouble không được null")
    @ExcelColum(colNum = 6)
    @TitleExcel(title = {"Tổng hợp 1", "Tổng hợp 2", "Tổng hợp 3", "aDouble"}, rowNum = {0, 1, 2, 3}, colNum = {4, 5, 6, 6})
    public Double aDouble;

    @NotNull(message = "aFloat không được null")
    @ExcelColum(colNum = 7)
    @TitleExcel(title = {"Tổng hợp 1", "Tổng hợp 2", "Tổng hợp 3", "aFloat"}, rowNum = {0, 1, 2, 3}, colNum = {4, 5, 6, 7})
    public Float aFloat;
}
