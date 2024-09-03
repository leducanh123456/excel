package org.example.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.process.ProjectExcelProcess;
import org.example.collection.ProjectExcelCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

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
            ProjectExcelProcess projectExcelData = new ProjectExcelProcess(validator);
            ProjectExcelCollection projectExcelCollection = projectExcelData.getListFromExcel(workbook);
            if(projectExcelCollection.excelIsError()){
                System.out.println("dang cp loi");
            } else {
                System.out.println("thành công");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
