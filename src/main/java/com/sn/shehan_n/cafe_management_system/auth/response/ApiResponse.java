package com.sn.shehan_n.cafe_management_system.auth.response;

import org.springframework.http.HttpMessage;

public class ApiResponse {
    private boolean success;
    private String message;

    private HttpMessage httpMessage;

    public ApiResponse(boolean success, String message,HttpMessage httpMessage) {
        this.success = success;
        this.message = message;
        this.httpMessage=httpMessage;
    }
}
