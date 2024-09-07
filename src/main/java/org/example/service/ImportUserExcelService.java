package org.example.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportUserExcelService {
    void importExcel(MultipartFile multipartFile);
}
