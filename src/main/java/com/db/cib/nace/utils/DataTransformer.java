package com.db.cib.nace.utils;


import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataTransformer {

    public static NaceDataDto convertEntityToDto(NaceDataEntity data) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data, new TypeReference<NaceDataDto>() {
        });
    }

    public static NaceDataEntity convertDtoToEntity(NaceDataDto data) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data, new TypeReference<NaceDataEntity>() {
        });
    }

    public static List<NaceDataDto> convertEntitiesToDTOs(List<NaceDataEntity> naceData) {
        return CollectionUtils.isEmpty(naceData)
                ? new ArrayList<>()
                : naceData.parallelStream().map(DataTransformer::convertEntityToDto).collect(Collectors.toList());
    }

    public static List<NaceDataEntity> convertDTOsToEntities(List<NaceDataDto> naceData) {
        return CollectionUtils.isEmpty(naceData)
                ? new ArrayList<>()
                : naceData.parallelStream().map(DataTransformer::convertDtoToEntity).collect(Collectors.toList());
    }
}
