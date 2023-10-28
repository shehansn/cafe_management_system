package com.sn.shehan_n.cafe_management_system.service;

import com.sn.shehan_n.cafe_management_system.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> signUp(Map<String, String> requestData);

    ResponseEntity<String> login(Map<String, String> requestData);

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<String> update(Map<String, String> requestData);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestdata);
}
