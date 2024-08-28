package org.example.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.component.ProjectExcelData;
import org.example.dto.ProjectExcelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ImportProjectServiceImpl implements ImportProjectService {

    @Autowired
    private ProjectExcelData projectExcelData;

    @Override
    public void importExcel(MultipartFile multipartFile) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            multipartFile.transferTo(tempFile.toFile());
            Workbook workbook = new XSSFWorkbook(Files.newInputStream(tempFile));
            List<ProjectExcelDTO> projectExcelDTOS = projectExcelData.getListFromExcel(workbook);
            System.out.println("thành công");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
