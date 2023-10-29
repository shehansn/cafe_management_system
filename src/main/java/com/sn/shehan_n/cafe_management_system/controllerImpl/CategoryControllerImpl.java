package com.sn.shehan_n.cafe_management_system.controllerImpl;

import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.controller.CategoryController;
import com.sn.shehan_n.cafe_management_system.entity.Category;
import com.sn.shehan_n.cafe_management_system.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    public CategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestData) {
        try{
            return categoryService.addNewCategory(requestData);
        }catch (Exception ex){
            ex.printStackTrace();

        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            return categoryService.getAllCategories();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestdata) {
        try {
            return categoryService.updateCategory(requestdata);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
