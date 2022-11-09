package com.db.cib.nace.service;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.exception.NaceDataNotFoundException;
import com.db.cib.nace.repository.NaceDataRepository;
import com.db.cib.nace.utils.DataModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class NaceDataService {

    @Autowired
    private NaceDataRepository naceDataRepository;

    @Autowired
    private FileReaderService<NaceDataDto> service;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<NaceDataEntity> saveNaceData(MultipartFile file) throws Exception {
        List<NaceDataEntity> naceData = readNaceDataFromCsvFile(file);
        log.info(" Storing {} NACE data records to database ", naceData.size());
        return naceDataRepository.saveAll(naceData);
    }

    public NaceDataEntity getNaceDataByOrderId(String orderId) throws InvalidInputException {
        if (StringUtils.isBlank(orderId)) {
            throw new InvalidInputException("Please provide valid orderId");
        }
        log.info(" Searching for NaceData by OrderId : " + orderId);
        Optional<NaceDataEntity> existingRecord = naceDataRepository.findByOrder(orderId);
        return existingRecord.orElseThrow(() -> new NaceDataNotFoundException("NACE data not found for OrderId " + orderId));
    }

    public List<NaceDataEntity> getAllNaceData() {
        log.info(" Fetching all Nace Data from database");
        List<NaceDataEntity> all = naceDataRepository.findAll();
        if (CollectionUtils.isNotEmpty(all)) {
            log.info(" Total {} records found for Nace Data in database", all.size());
            return all;
        } else {
            log.error(" No records found for Nace Data in database");
            throw new NaceDataNotFoundException("No records found for Nace Data in database");
        }
    }

    public List<NaceDataEntity> readNaceDataFromCsvFile(MultipartFile file) throws Exception {
        try {
            if (Objects.isNull(file) || !StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), ".csv")) {
                log.error("Please upload a valid CSV file {}", file.getOriginalFilename());
                throw new InvalidInputException("Please upload a valid CSV file !");
            }
            List<NaceDataDto> naceDataDTOs = service.readFileData(file, NaceDataDto.class);
            return DataModelMapper.mapDTOsToEntities(naceDataDTOs);
        } catch (Exception e) {
            log.error("Exception occurred while reading data from CSV file and populating to objects ", e);
            throw e;
        }
    }
}