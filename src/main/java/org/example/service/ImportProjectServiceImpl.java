package org.example.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.collection.ProjectExcelCollection;
import org.example.dto.ExcelError;
import org.example.dto.ProjectExcelDTO;
import org.example.process.ExcelProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
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
            ExcelProcess<ProjectExcelDTO, ProjectExcelCollection> excelProcess = new ExcelProcess<>(ProjectExcelDTO.class, validator);
            ProjectExcelCollection projectExcelCollection = excelProcess.getListFromExcel(workbook.getSheetAt(0));
            List<ProjectExcelDTO> projectExcelDTOS = projectExcelCollection.getData();
            if (projectExcelCollection.excelIsError()) {
                List<ExcelError> excelErrors = projectExcelCollection.getAllError();
                System.out.println("dang cp loi");
            } else {
                System.out.println("thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
