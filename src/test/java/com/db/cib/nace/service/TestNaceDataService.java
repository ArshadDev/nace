package com.db.cib.nace.service;

import com.db.cib.nace.entity.NaceDataEntity;
import com.db.cib.nace.repository.NaceDataRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
public class TestNaceDataService {

    @Autowired
    private NaceDataRepository naceDataRepository;

    private NaceDataService naceDataService;
    private List<NaceDataEntity> data;


    @BeforeAll
    public void setUp() {
        naceDataService = new NaceDataService();
        naceDataRepository = mock(NaceDataRepository.class);
        ReflectionTestUtils.setField(naceDataService, "naceDataRepository", naceDataRepository);
        populateTestData();
    }


    @Test
    public void testSaveNaceData() {

        //Set behavior
        when(naceDataRepository.saveAll(data)).thenReturn(data);

        //Make Actual call
        List<NaceDataEntity> naceData = naceDataService.saveNaceData(data);

        //Verify result
        assertNotNull(naceData);
        assertEquals(naceData.size(), 2);
    }

    @Test
    public void testGetNaceDataByOrderId() {

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