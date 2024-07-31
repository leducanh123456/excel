package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.antation.ExcelColum;
import org.example.antation.ExcelPath;
import org.example.antation.ExcelPrimary;
import org.example.antation.ExcelRowStart;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
@ExcelRowStart(startRow = 4)
@ExcelPath(path = "test.xlsx")
public class UserDTO extends ExcelDTO {

    @NotEmpty(message = "aString không được để null hoặc empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "aString chỉ bao gồm số và chữ")
    @ExcelColum(colNum = 0)
    @ExcelPrimary
    private String aString;

    @NotNull(message = "aInterger không được null")
    @ExcelColum(colNum = 1)
    private Integer aInteger;

    @NotNull(message = "aBoolean không được null")
    @ExcelColum(colNum = 2)
    private Boolean aBoolean;

    @NotNull(message = "aBigDecimal không được null")
    @ExcelColum(colNum = 3)
    private BigDecimal aBigDecimal;

    //nhóm cao nhất
    @NotNull(message = "aLocalDate không được null")
    @ExcelColum(colNum = 4)
    private LocalDate aLocalDate;

    // 1 nhóm nữa
    @NotNull(message = "aTerger không được null")
    @ExcelColum(colNum = 5)
    private LocalDateTime aLocalDateTime;

    //1 nhóm
    @NotNull(message = "aDouble không được null")
    @ExcelColum(colNum = 6)
    private Double aDouble;

    @NotNull(message = "aFloat không được null")
    @ExcelColum(colNum = 7)
    private Float aFloat;
}
