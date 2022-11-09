package com.db.cib.nace.controller;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.service.NaceDataService;
import com.db.cib.nace.utils.DataTransformer;
import com.db.cib.nace.utils.UtilService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@Api(value = "Nace Data Management Service")
@RequestMapping("/api")
public class NaceDataController {

    @Autowired
    private NaceDataService naceDataService;

    @Autowired
    private UtilService utilservice;

    @PostMapping(path = "/nace-data-file", consumes = "multipart/form-data")
    public ResponseEntity<Object> persistNaceDetails(@RequestPart(required = true, name = "file") MultipartFile file) throws Exception {
        if (Objects.isNull(file)) {
            throw new InvalidInputException("Please upload a valid CSV file");
        }
        log.info(" Received request to process uploaded NACE data file {} and store data in database ", file.getOriginalFilename());
        List<NaceDataEntity> naceData = utilservice.readNaceDataFromCsvFile(file);
        List<NaceDataEntity> result = naceDataService.saveNaceData(naceData);
        return ResponseEntity.status(HttpStatus.OK).body("The CSV file " + file.getOriginalFilename() + " is uploaded successfully and added " + (null != result ? result.size() : 0) + " records to database !");
    }

    @GetMapping(path = "/nace-data/{orderId}")
    public ResponseEntity<Object> getNaceDataForOrderId(@PathVariable(name = "orderId") String orderId) throws InvalidInputException {
        if (StringUtils.isBlank(orderId)) {
            throw new InvalidInputException("Please provide valid orderId");
        }
        log.info(" Received request to provide NACE data for OrderId {} ", orderId);
        NaceDataEntity naceData = naceDataService.getNaceDataByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(DataTransformer.convertEntityToDto(naceData));
    }

    @GetMapping(path = "/nace-data")
    public ResponseEntity<List<NaceDataDto>> getAllNaceData() {
        log.info(" Received request to provide all available NACE data in DB");
        List<NaceDataEntity> allNaceData = naceDataService.getAllNaceData();
        log.info(" Returning total {} records for NACE data from Database ", (null != allNaceData ? allNaceData.size() : 0));
        return ResponseEntity.status(HttpStatus.OK).body(DataTransformer.convertEntitiesToDTOs(allNaceData));
    }
}
