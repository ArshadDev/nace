package com.db.cib.nace.utils;


import com.db.cib.nace.dto.NaceDataDto;
import com.db.cib.nace.entity.NaceDataEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataModelMapper {

    public static NaceDataDto mapEntityToDto(NaceDataEntity data) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data, new TypeReference<NaceDataDto>() {
        });
    }

    public static NaceDataEntity mapDtoToEntity(NaceDataDto data) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(data, new TypeReference<NaceDataEntity>() {
        });
    }

    public static List<NaceDataDto> mapEntitiesToDTOs(List<NaceDataEntity> naceData) {
        return CollectionUtils.isEmpty(naceData)
                ? new ArrayList<>()
                : naceData.parallelStream().map(DataModelMapper::mapEntityToDto).collect(Collectors.toList());
    }

    public static List<NaceDataEntity> mapDTOsToEntities(List<NaceDataDto> naceData) {
        return CollectionUtils.isEmpty(naceData)
                ? new ArrayList<>()
                : naceData.parallelStream().map(DataModelMapper::mapDtoToEntity).collect(Collectors.toList());
    }
}
