package com.service.upload.csv.file.services;

import com.service.upload.csv.file.daos.models.CSVModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    void save(MultipartFile file);
    List<CSVModel> getAllTutorials();
}
