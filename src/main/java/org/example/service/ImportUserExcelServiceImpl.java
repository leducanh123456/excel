package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.collection.UserExcelCollection;
import org.example.dto.ExcelError;
import org.example.dto.UserExcelDTO;
import org.example.process.ExcelProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class ImportUserExcelServiceImpl implements ImportUserExcelService {

    @Autowired
    private Validator validator;

    @Override
    public void importExcel(MultipartFile multipartFile) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            multipartFile.transferTo(tempFile.toFile());
            Workbook workbook = new XSSFWorkbook(Files.newInputStream(tempFile));
            ExcelProcess<UserExcelDTO, UserExcelCollection> excelProcess = new ExcelProcess<>(UserExcelDTO.class, validator);
            Boolean checkHeader = excelProcess.checkHeaderExcel(workbook.getSheetAt(0));
            UserExcelCollection projectExcelCollection = excelProcess.getListFromExcel(workbook.getSheetAt(0));
            List<UserExcelDTO> projectExcelDTOS = projectExcelCollection.getData();
            if (projectExcelCollection.excelIsError()) {
                List<ExcelError> excelErrors = projectExcelCollection.getAllError();
                System.out.println("dang cp loi");
            } else {
                System.out.println("thành công");
            }
        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            String message = e.getMessage();
            log.error("ExcelProcess constructor : {}", message);
        }
    }
}
