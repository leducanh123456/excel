package org.example.service;

import org.springframework.web.multipart.MultipartFile;


public interface ImportProjectService {
    void importExcel(MultipartFile multipartFile);
}
