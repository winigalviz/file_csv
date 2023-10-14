package com.service.upload.csv.file.services.impl;

import com.service.upload.csv.file.daos.models.CSVModel;
import com.service.upload.csv.file.daos.repositories.CSVRepository;
import com.service.upload.csv.file.helpers.CSVHelper;
import com.service.upload.csv.file.services.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {
    @Autowired
    private CSVRepository csvRepository;

    @Override
    public void save(MultipartFile file) {
        try {
            List<CSVModel> csvModels = CSVHelper.csvToTutorials(file.getInputStream());
            csvRepository.saveAll(csvModels);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public List<CSVModel> getAllTutorials() {
        return csvRepository.findAll();
    }
}
