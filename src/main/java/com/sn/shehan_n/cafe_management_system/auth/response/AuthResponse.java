package com.sn.shehan_n.cafe_management_system.auth.response;

public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String message;

    public AuthResponse(String token,String message) {
        accessToken = token;
        this.message=message;
    }
}
