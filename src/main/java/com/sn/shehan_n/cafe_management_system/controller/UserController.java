package com.sn.shehan_n.cafe_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
@RequestMapping(path = "/user")
public interface UserController {
    @PostMapping(path = "/signUp")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMapping);

    @PostMapping(path = "/signUp2")
    public ResponseEntity<String> signUp2(@RequestBody(required = true) Map<String,String> requestMapping);
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMapping);

}
