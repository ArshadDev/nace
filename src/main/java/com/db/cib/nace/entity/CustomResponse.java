package com.db.cib.nace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomResponse implements Serializable {
    private String responseMessage;
    private String responseCode;
}
