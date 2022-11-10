package com.db.cib.nace.controller;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.CustomResponse;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.service.NaceDataService;
import com.db.cib.nace.utils.CommonUtil;
import com.db.cib.nace.utils.DataModelMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@Api(value = "Nace Data Management Service")
@RequestMapping("/api")
public class NaceDataController {

    @Autowired
    private NaceDataService naceDataService;

    @ApiOperation(value = "upload NACE Data Csv File to store data in DB")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "uploaded File is invalid to be processed"),
            @ApiResponse(code = 404, message = "uploaded CSV file don't have valid data to process."),
            @ApiResponse(code = 200, message = "The CSV file is uploaded successfully !", response = NaceDataEntity.class)})
    @PostMapping(path = "/nace-data-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> persistNaceDetails(@RequestPart(required = true, name = "file") MultipartFile file) throws Exception {
        log.info(" Received request to process uploaded NACE data file {} and store data in database ", file.getOriginalFilename());
        List<NaceDataEntity> result = naceDataService.saveNaceData(file);
        CustomResponse response = CommonUtil.buildResponse("The CSV file " + file.getOriginalFilename() + " is uploaded successfully and added " + (null != result ? result.size() : 0) + " records to database !"
                , HttpStatus.OK.toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "get NACE Data from database for a given OrderId")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Nace Data not found in database for a given OrderId."),
            @ApiResponse(code = 200, message = "NACE Data fetched successfully from database for a given OrderId", response = NaceDataEntity.class)})
    @GetMapping(path = "/nace-data/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNaceDataForOrderId(@PathVariable(name = "orderId") String orderId) throws InvalidInputException {
        log.info(" Received request to provide NACE data for OrderId {} ", orderId);
        NaceDataEntity naceData = naceDataService.getNaceDataByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(DataModelMapper.mapEntityToDto(naceData));
    }

    @ApiOperation(value = "get all available NACE Data records from database")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Nace Data not available in database"),
            @ApiResponse(code = 200, message = "NACE Data fetched successfully from database", response = NaceDataEntity.class)})
    @GetMapping(path = "/nace-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NaceDataDto>> getAllNaceData() {
        log.info(" Received request to provide all available NACE data in DB");
        List<NaceDataEntity> allNaceData = naceDataService.getAllNaceData();
        log.info(" Returning total {} records for NACE data from Database ", (null != allNaceData ? allNaceData.size() : 0));
        return ResponseEntity.status(HttpStatus.OK).body(DataModelMapper.mapEntitiesToDTOs(allNaceData));
    }
}
