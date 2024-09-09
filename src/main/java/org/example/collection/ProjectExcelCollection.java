package org.example.collection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.antation.ValidateListError;
import org.example.dto.ExcelError;
import org.example.dto.ProjectExcelDTO;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectExcelCollection extends ExcelCollection<ProjectExcelDTO> {
    @ValidateListError
    @SuppressWarnings({"squid:S1144", "unused"})
    public List<ExcelError> validateExcelFile() {
        List<ExcelError> excelErrors = new ArrayList<>();
        if (this.getData().get(1).getBudget() != null
                && this.getData().get(2).getBudget() != null
                && this.getData().get(1).getBudget().compareTo(this.getData().get(2).getBudget()) == 0) {
            ExcelError excelError = new ExcelError();
            excelError.setMessage("Tong so tien khong dap ung");
            ExcelError excelError2 = new ExcelError();
            excelError2.setMessage("Tong so tien khong dap ung");
            excelErrors.add(excelError);
            excelErrors.add(excelError2);
        }
        return excelErrors;
    }
}
