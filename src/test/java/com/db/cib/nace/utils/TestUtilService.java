package com.db.cib.nace.utils;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.service.CsvFileReaderService;
import com.db.cib.nace.service.FileReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtilService {


    private UtilService utilservice;

    private MockMultipartFile mockMultipartFile;

    @BeforeEach
    public void setUp() throws Exception {
        utilservice = new UtilService();
        FileReaderService<NaceDataDto> service = new CsvFileReaderService<>();
        ReflectionTestUtils.setField(utilservice, "service", service);

        InputStream is = getClass().getClassLoader().getResourceAsStream("NACE_REV2_Data_Test_File.csv");
        mockMultipartFile = new MockMultipartFile("file", "NACE_REV2_Data_Test_File.csv", "application/vnd.ms-excel", is);
    }


    @Test
    public void testReadNaceDataFromCsvFile() throws Exception {

        List<NaceDataEntity> naceData = utilservice.readNaceDataFromCsvFile(mockMultipartFile);

        assertNotNull(naceData);
        assertEquals(naceData.size(), 2);
    }
}
