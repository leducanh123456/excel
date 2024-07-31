package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ExcelDTO {
    private Integer rowNumber;
    private Integer contentNumber;
    private Set<String> cellNotCheck;
    private Set<String> cellInValidType;
    private Boolean isFirstRowMerge;
    private Boolean isFirstColMerge;
}
