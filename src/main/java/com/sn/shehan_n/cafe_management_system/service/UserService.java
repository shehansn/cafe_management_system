package com.sn.shehan_n.cafe_management_system.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestData);

    ResponseEntity<String> login(Map<String, String> requestData);
}
