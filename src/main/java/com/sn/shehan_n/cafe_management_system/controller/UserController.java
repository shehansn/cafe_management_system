package com.sn.shehan_n.cafe_management_system.controller;

import com.sn.shehan_n.cafe_management_system.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RequestMapping(path = "/user")
public interface UserController {
    @PostMapping(path = "/signUp")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMapping);

    @PostMapping(path = "/signUp2")
    public ResponseEntity<String> signUp2(@RequestBody(required = true) Map<String,String> requestMapping);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMapping);

    @GetMapping(path = "getAllUsers")
    public ResponseEntity<List<UserWrapper>> getAllUsers();

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestData);

    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestdata);
}
