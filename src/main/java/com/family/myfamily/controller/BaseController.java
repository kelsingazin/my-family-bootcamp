package com.family.myfamily.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected ResponseEntity<?> buildResponse(Object data, HttpStatus httpStatus) {
        return new ResponseEntity<>(data, httpStatus);
    }

    protected ResponseEntity<?> buildResponse(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus);
    }

    protected ResponseEntity<?> buildSuccessResponse(Object data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
