package org.example.process;

import org.example.collection.ProjectExcelCollection;
import org.example.dto.ProjectExcelDTO;
import org.springframework.validation.Validator;


public class ProjectExcelProcess extends ExcelProcess<ProjectExcelDTO, ProjectExcelCollection> {
    private static final Class<ProjectExcelDTO> projectExcelDTOClass = ProjectExcelDTO.class;

    public ProjectExcelProcess(Validator validator) {
        super(projectExcelDTOClass, validator);
    }
}
