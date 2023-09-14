package com.pudingyum.opcomic.utils;

import com.pudingyum.opcomic.domain.common.ApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class Response {
    private Response(){}

    public static <T> ResponseEntity<Object> build(String responseMessage, T data, List<String> errors, HttpStatusCode httpStatus){
        ApiResponse apiResponse = ApiResponse.<T>builder()
                .message(responseMessage)
                .data(data)
                .errors(errors)
                .build();

        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}

