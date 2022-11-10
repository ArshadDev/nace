package com.db.cib.nace.utils;

import com.db.cib.nace.entity.CustomResponse;

public class CommonUtil {

    public static CustomResponse buildResponse(String message, String code) {
        CustomResponse response = new CustomResponse();
        response.setResponseMessage(message);
        response.setResponseCode(code);
        return response;
    }
}
