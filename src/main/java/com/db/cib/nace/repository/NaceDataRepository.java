package com.db.cib.nace.repository;

import com.db.cib.nace.entity.NaceDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NaceDataRepository extends JpaRepository<NaceDataEntity, Integer> {

    Optional<NaceDataEntity> findByOrder(String order);

}