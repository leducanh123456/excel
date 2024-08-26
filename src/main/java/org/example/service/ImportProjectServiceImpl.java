package org.example.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dto.ExcelError;
import org.example.dto.ProjectExcelDTO;
import org.example.util.ExcelUtil;
import org.example.util.ValidateExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(tempFile))) {
                Sheet sheet = workbook.getSheetAt(0);
//                ExcelUtil<ProjectExcelDTO> projectExcelDTOExcelUtil = new ExcelUtil<>();
//                List<ProjectExcelDTO> projectExcelDTOS = projectExcelDTOExcelUtil.getListObjectFromExcel(sheet, ProjectExcelDTO.class);
//                ProjectExcelDTO projectExcelDTO = projectExcelDTOS.get(0);
//                ValidateExcel<ProjectExcelDTO> projectExcelDTOValidateUtil = new ValidateExcel<>();
//                List<ExcelError> excelErrors = projectExcelDTOValidateUtil.checkPrimary(projectExcelDTOS, ProjectExcelDTO.class);
//                List<ExcelError> excelErrorsTest = projectExcelDTOValidateUtil.validateData(projectExcelDTOS, validator, ProjectExcelDTO.class);
                System.out.println("đọc thành công file");
            } finally {
                if (tempFile != null && Files.exists(tempFile)) {
                    try {
                        Files.delete(tempFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
