package com.sn.shehan_n.cafe_management_system.service;

import com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String, String> requestData);

    ResponseEntity<List<ProductWrapper>> getAllProducts();

    ResponseEntity<String> updateProduct(Map<String, String> requestData);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestData);

    ResponseEntity<List<ProductWrapper>> getProductByCategoryId(Integer id);

    ResponseEntity<List<ProductWrapper>> getProductById(Integer id);
}
