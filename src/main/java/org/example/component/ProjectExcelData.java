package org.example.component;

import org.example.dto.ProjectExcelDTO;
import org.example.validate.ExcelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

@Component
public class ProjectExcelData extends ExcelData<ProjectExcelDTO> {
    private static final Class<ProjectExcelDTO> projectExcelDTOClass = ProjectExcelDTO.class;

    @Autowired
    public ProjectExcelData(Validator validator) {
        super(projectExcelDTOClass, validator);
    }
}
