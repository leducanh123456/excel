package org.example.component;

import org.example.composite.ProjectExcelCollection;
import org.example.dto.ProjectExcelDTO;
import org.example.validate.ExcelData;
import org.springframework.validation.Validator;


public class ProjectExcelData extends ExcelData<ProjectExcelDTO, ProjectExcelCollection> {
    private static final Class<ProjectExcelDTO> projectExcelDTOClass = ProjectExcelDTO.class;
    
    public ProjectExcelData(Validator validator, ProjectExcelCollection projectExcelCollection) {
        super(projectExcelCollection, projectExcelDTOClass, validator);
    }
}
