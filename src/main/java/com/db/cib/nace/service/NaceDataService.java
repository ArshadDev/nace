package com.db.cib.nace.service;

import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.NaceDataNotFoundException;
import com.db.cib.nace.repository.NaceDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NaceDataService {

    @Autowired
    private NaceDataRepository naceDataRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<NaceDataEntity> saveNaceData(List<NaceDataEntity> naceData) {
        log.info(" Storing {} NACE data records to database ", naceData.size());
        return naceDataRepository.saveAll(naceData);
    }

    public NaceDataEntity getNaceDataByOrderId(String orderId) {
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
}
