package com.db.cib.nace.controller;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.service.CsvFileReaderService;
import com.db.cib.nace.service.FileReaderService;
import com.db.cib.nace.service.NaceDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestNaceDataController {

    @Autowired
    private NaceDataService naceDataService;

    @Autowired
    MockMvc mockMvc;
    private MockMultipartFile mockMultipartFile;


    @BeforeEach
    public void setUp() throws Exception {

        FileReaderService<NaceDataDto> service = new CsvFileReaderService<>();
        ReflectionTestUtils.setField(naceDataService, "service", service);
        InputStream is = getClass().getClassLoader().getResourceAsStream("NACE_REV2_Data_Test_File.csv");
        mockMultipartFile = new MockMultipartFile("file", "NACE_REV2_Data_Test_File.csv", "application/vnd.ms-excel", is);
    }


    @Test
    public void testUploadNaceDataFile_ValidCsvFile_Success() throws Exception {

        MvcResult apiResult = mockMvc.perform(multipart("/api/nace-data-file").file(mockMultipartFile))
                .andExpect(status().isOk()).andReturn();

        assertNotNull(apiResult.getResponse().getContentAsString());
        assertTrue(apiResult.getResponse().getContentAsString()
                .contains("The CSV file " + mockMultipartFile.getOriginalFilename() + " is uploaded successfully and added 2 records to database !"));
    }

    @Test
    public void testUploadNaceDataFile_NonCsvFile_Failure() throws Exception {

        InputStream is = getClass().getClassLoader().getResourceAsStream("Invalid_CSV_File.txt");
        mockMultipartFile = new MockMultipartFile("file", "Invalid_CSV_File.txt", "application/vnd.ms-excel", is);

        MvcResult apiResult = mockMvc.perform(multipart("/api/nace-data-file").file(mockMultipartFile))
                .andExpect(status().isBadRequest()).andReturn();

        assertNotNull(apiResult.getResponse().getContentAsString());
        assertTrue(apiResult.getResponse().getContentAsString().contains("Please upload a valid CSV file !"));

        Optional<Exception> exceptionOccurred = Optional.ofNullable(apiResult.getResolvedException());

        if (exceptionOccurred.isPresent()) {
            assertNotNull(exceptionOccurred.get());
            assertTrue(exceptionOccurred.get() instanceof InvalidInputException);
        }
    }

    @Test
    public void testGetNaceDataForOrderId_Failure() throws Exception {

        List<NaceDataEntity> naceData = naceDataService.readNaceDataFromCsvFile(mockMultipartFile);
        MvcResult apiResult = mockMvc.perform(get("/nace-data/{orderId}", naceData.get(0).getOrder()))
                .andExpect(status().isNotFound()).andReturn();

        assertNotNull(apiResult.getResponse().getContentAsString());
    }


}
