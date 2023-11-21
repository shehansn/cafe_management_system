package com.sn.shehan_n.cafe_management_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/dashboard")

public interface DashboardController {

    @GetMapping(path = "/details")
    ResponseEntity<Map<String,Object>> getCount();
}
