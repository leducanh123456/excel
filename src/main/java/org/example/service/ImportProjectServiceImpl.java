package org.example.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.component.ProjectExcelData;
import org.example.composite.ProjectExcelCollection;
import org.example.dto.ProjectExcelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportProjectServiceImpl implements ImportProjectService {

    @Autowired
    private Validator validator;

    @Override
    public void importExcel(MultipartFile multipartFile) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            multipartFile.transferTo(tempFile.toFile());
            Workbook workbook = new XSSFWorkbook(Files.newInputStream(tempFile));
            List<ProjectExcelDTO> projectExcelDTOS = new ArrayList<>();
            ProjectExcelCollection projectExcelCollection = new ProjectExcelCollection(projectExcelDTOS);
            ProjectExcelData projectExcelData = new ProjectExcelData(validator, projectExcelCollection);
            projectExcelData.getListFromExcel(workbook);
            System.out.println("thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
