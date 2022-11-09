package com.db.cib.nace.utils;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.service.FileReaderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class UtilService {

    @Autowired
    private FileReaderService<NaceDataDto> service;

    public List<NaceDataEntity> readNaceDataFromCsvFile(MultipartFile file) throws Exception {
        try {
            if (!Objects.isNull(file) && !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".csv")) {
                log.error("Please upload a valid CSV file {}", file.getOriginalFilename());
                throw new InvalidInputException("Please upload a valid CSV file !");
            }
            List<NaceDataDto> naceDataDTOs = service.readFileData(file, NaceDataDto.class);
            return DataTransformer.convertDTOsToEntities(naceDataDTOs);
        } catch (Exception e) {
            log.error("Exception occurred while reading data from CSV file and populating to objects ", e);
            throw e;
        }
    }
}
