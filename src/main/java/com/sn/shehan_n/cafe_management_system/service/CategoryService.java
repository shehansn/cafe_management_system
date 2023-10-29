package com.sn.shehan_n.cafe_management_system.service;

import com.sn.shehan_n.cafe_management_system.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService
{
    ResponseEntity<String> addNewCategory(Map<String, String> requestData);

    ResponseEntity<List<Category>> getAllCategories();
    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> updateCategory(Map<String, String> requestdata);
}
