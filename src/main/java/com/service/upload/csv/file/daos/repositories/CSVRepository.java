package com.service.upload.csv.file.daos.repositories;

import com.service.upload.csv.file.daos.models.CSVModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSVRepository extends JpaRepository<CSVModel, Long> {

}
