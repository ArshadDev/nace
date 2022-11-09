package com.db.cib.nace.repository;

import com.db.cib.nace.entity.NaceDataEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestNaceDataRepository {

    @Autowired
    private NaceDataRepository naceDataRepository;

    private List<NaceDataEntity> data;

    @BeforeAll
    public void setUp() {
        data = new ArrayList<>();
        data.add(NaceDataEntity.builder().code("A").description("FORESTRY AND FISHING").id(1)
                .itemAlsoIncludes("This section also includes First Order").itemIncludes("This section includes New Order").itemExcludes("This section excludes second Order")
                .order("398481").build());

        data.add(NaceDataEntity.builder().code("B").description("FORESTRY AND FISHING DESC").id(2)
                .itemAlsoIncludes("This section also includes Second Order").itemIncludes("This section includes New Order").itemExcludes("This section excludes second Order")
                .order("398482").build());
    }

    @Test
    public void testSaveAll() {
        naceDataRepository.saveAll(data);
        Optional<NaceDataEntity> order = naceDataRepository.findByOrder("398481");
        assertNotNull(order.get());
        assertEquals(order.get().getDescription(), "FORESTRY AND FISHING");
    }

    @Test
    public void testFindAll() {

        naceDataRepository.saveAll(data);
        List<NaceDataEntity> all = naceDataRepository.findAll();
        assertNotNull(all);
        assertEquals(all.size(), 2);
    }
}