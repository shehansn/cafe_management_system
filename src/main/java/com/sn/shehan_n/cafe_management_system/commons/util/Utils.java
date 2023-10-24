package com.sn.shehan_n.cafe_management_system.commons.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utils {
    private Utils(){

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){

        return new ResponseEntity<String>("{\"Message \":\""+responseMessage+"\"}", httpStatus);
    }
}
