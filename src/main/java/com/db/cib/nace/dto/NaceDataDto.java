package com.db.cib.nace.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaceDataDto {

    @CsvBindByName(column = "Order")
    private String order;

    @CsvBindByName(column = "Level")
    private String level;

    @CsvBindByName(column = "Code")
    private String code;

    @CsvBindByName(column = "Parent")
    private String parent;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "This item includes")
    private String itemIncludes;

    @CsvBindByName(column = "This item also includes")
    private String itemAlsoIncludes;

    @CsvBindByName(column = "Rulings")
    private String rulings;

    @CsvBindByName(column = "This item excludes")
    private String itemExcludes;

    @CsvBindByName(column = "Reference to ISIC Rev. 4")
    private String isicRef;
}
