package com.service.upload.csv.file.services.impl;

import com.service.upload.csv.file.daos.models.CSVModel;
import com.service.upload.csv.file.daos.repositories.CSVRepository;
import com.service.upload.csv.file.helpers.CSVHelper;
import com.service.upload.csv.file.services.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Override
    public SseEmitter async(Long id) {
        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            CSVModel dataSet = updateReturnData(id);
            try {
                randomDelay();
                emitter.send(dataSet);
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        executor.shutdown();
        return emitter;
    }

    private CSVModel updateReturnData (Long id) {
        Optional<CSVModel> csvModel = csvRepository.findById(id);
        if(csvModel.isPresent()) {
            csvModel.get().setPublished(false);
            csvRepository.save(csvModel.get());
            return csvModel.get();
        }
        return null;
    }

    private void randomDelay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
