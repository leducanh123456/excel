package org.example.component;

import org.example.dto.ProjectExcelDTO;
import org.example.validate.ExcelData;
import org.springframework.stereotype.Component;

@Component
public class ProjectExcelData extends ExcelData<ProjectExcelDTO> {
    public ProjectExcelData(Class<ProjectExcelDTO> aClass) {
        super(aClass);
    }
}
