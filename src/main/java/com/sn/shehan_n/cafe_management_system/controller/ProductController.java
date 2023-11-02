package com.sn.shehan_n.cafe_management_system.controller;

import com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface ProductController {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> requestData);

    @GetMapping(path = "/getAll")
    ResponseEntity<List<ProductWrapper>> getAllProducts();

    @PostMapping(path = "/update")
    ResponseEntity<String> updateProduct(@RequestBody Map<String ,String> requestData);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PostMapping(path = "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String ,String> requestData);

    @GetMapping(path = "/getProductByCategoryId/{id}")
    ResponseEntity<List<ProductWrapper>> getProductByCategoryId(@PathVariable Integer id);

    @GetMapping(path = "/getProductById/{id}")
    ResponseEntity<List<ProductWrapper>> getProductById(@PathVariable Integer id);
}
