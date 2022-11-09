package com.db.cib.nace.service;


import com.db.cib.nace.exception.InvalidInputException;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Service
@Slf4j
public class CsvFileReaderService<T> implements FileReaderService<T> {

    @Override
    public List<T> readFileData(MultipartFile file, Class<T> type) throws Exception {

        log.info("Reading the content of file {} for creating objects of type {}", file.getOriginalFilename(), type);

        try (Reader reader = new InputStreamReader(file.getInputStream());) {
            List<T> data = new CsvToBeanBuilder<T>(reader).withType(type).build().parse();
            if (null != data && data.size() > 0) {
                log.info("Total {} records found in the uploaded file ", data.size());
                return data;
            } else {
                log.error("No record found in uploaded CSV file {}", file.getOriginalFilename());
                throw new InvalidInputException("No record found in uploaded CSV file " + file.getOriginalFilename());
            }
        } catch (Exception e) {
            log.error("Exception occurred while reading data from uploaded CSV file {}", file.getOriginalFilename(), e);
            if (e.getCause() instanceof CsvRequiredFieldEmptyException) {
                throw new InvalidInputException("Uploaded CSV file is invalid");
            } else {
                throw e;
            }
        }
    }
}
