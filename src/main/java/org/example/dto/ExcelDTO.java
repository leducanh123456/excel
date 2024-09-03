package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.collection.ExcelCollection;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDTO<T> {

    private ExcelCollection<T> excelCollection;
    private Integer rowNumber;
    private Integer contentNumber;
    private Set<String> cellNotCheck;
    private Set<String> cellInValidType;
    private List<String> titles;
    private List<ExcelError> errors;
}
