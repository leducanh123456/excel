package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.collection.ExcelCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDTO<T> {
    public ExcelCollection<T> excelCollection;
    public Integer rowNumber;
    public Integer contentNumber;
    public Set<String> cellNotCheck;
    public Set<String> cellInValidType;
    public List<String> titles;
    public List<ExcelError> errors = new ArrayList<>();
}
