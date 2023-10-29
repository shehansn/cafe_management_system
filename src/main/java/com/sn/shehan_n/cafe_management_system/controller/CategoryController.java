package com.sn.shehan_n.cafe_management_system.controller;

import com.sn.shehan_n.cafe_management_system.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryController {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> requestData);

    @GetMapping(path = "/getAll")
    ResponseEntity<List<Category>> getAllCategories();

    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestdata);
}
