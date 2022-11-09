package com.db.cib.nace.service;

import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.exception.InvalidInputException;
import com.db.cib.nace.repository.NaceDataRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
public class TestNaceDataService {

    @Autowired
    private NaceDataRepository naceDataRepository;

    private NaceDataService naceDataService;
    private List<NaceDataEntity> data;
    private MockMultipartFile mockMultipartFile;


    @BeforeAll
    public void setUp() throws IOException {
        naceDataService = new NaceDataService();
        naceDataRepository = mock(NaceDataRepository.class);

        FileReaderService<NaceDataDto> service = new CsvFileReaderService<>();
        ReflectionTestUtils.setField(naceDataService, "service", service);
        ReflectionTestUtils.setField(naceDataService, "naceDataRepository", naceDataRepository);

        InputStream is = getClass().getClassLoader().getResourceAsStream("NACE_REV2_Data_Test_File.csv");
        mockMultipartFile = new MockMultipartFile("file", "NACE_REV2_Data_Test_File.csv", "application/vnd.ms-excel", is);
        populateTestData();
    }


    @Test
    public void testSaveNaceData() throws Exception {

        //Set behavior
        when(naceDataRepository.saveAll(any())).thenReturn(data);

        //Make Actual call
        List<NaceDataEntity> naceData = naceDataService.saveNaceData(mockMultipartFile);

        //Verify result
        assertNotNull(naceData);
        assertEquals(2, naceData.size());
    }

    @Test
    public void testGetNaceDataByOrderId() throws InvalidInputException {

        //Test Data
        NaceDataEntity naceDataOne = data.get(0);

        //Set behavior
        when(naceDataRepository.findByOrder(naceDataOne.getOrder())).thenReturn(Optional.of(naceDataOne));

        //Make Actual call
        NaceDataEntity result = naceDataService.getNaceDataByOrderId(naceDataOne.getOrder());

        //Verify result
        assertNotNull(result);
        assertEquals(result.getOrder(), naceDataOne.getOrder());
    }

    @Test
    public void testGetAllNaceData() {

        //Set behavior
        when(naceDataRepository.findAll()).thenReturn(data);

        //Make Actual call
        List<NaceDataEntity> allNaceData = naceDataService.getAllNaceData();

        //Verify result
        assertNotNull(allNaceData);
        assertEquals(allNaceData.size(), 2);
    }

    @Test
    public void testReadNaceDataFromCsvFile() throws Exception {
        List<NaceDataEntity> naceData = naceDataService.readNaceDataFromCsvFile(mockMultipartFile);
        assertNotNull(naceData);
        assertEquals(naceData.size(), 2);
    }


    private void populateTestData() {
        data = new ArrayList<>();
        data.add(NaceDataEntity.builder().code("A").description("FORESTRY AND FISHING").id(1)
                .itemAlsoIncludes("This section also includes First Order").itemIncludes("This section includes New Order").itemExcludes("This section excludes second Order")
                .order("398481").build());

        data.add(NaceDataEntity.builder().code("B").description("FORESTRY AND FISHING DESC").id(2)
                .itemAlsoIncludes("This section also includes Second Order").itemIncludes("This section includes New Order").itemExcludes("This section excludes second Order")
                .order("398482").build());
    }
}