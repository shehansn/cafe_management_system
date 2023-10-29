package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.google.common.base.Strings;
import com.sn.shehan_n.cafe_management_system.auth.filter.TokenAuthenticationFilter;
import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.entity.Category;
import com.sn.shehan_n.cafe_management_system.repository.CategoryRepository;
import com.sn.shehan_n.cafe_management_system.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.categoryRepository = categoryRepository;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestData) {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {
                if (validateCategoryMap(requestData, false)) {
                    categoryRepository.save(getCategoryFromMap(requestData, false));
                    return Utils.getResponseEntity("CATEGORY ADDED SUCCESSFULLY", HttpStatus.OK);
                }

            } else {
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            //categoryRepository.save(requestData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN ADDING NEW CATEGORY ", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            if (tokenAuthenticationFilter.isAdmin() || tokenAuthenticationFilter.isUser()) {
                return new ResponseEntity<>(categoryRepository.getAllCategories(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (tokenAuthenticationFilter.isAdmin() || tokenAuthenticationFilter.isUser()) {
                if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                    return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
                }
                return new ResponseEntity<List<Category>>(categoryRepository.findAll(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestData) {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {
                if (validateCategoryMap(requestData, true)) {
                    Optional foundCatObj=categoryRepository.findById(Integer.parseInt(requestData.get("id")));
                    if(foundCatObj.isPresent()){
                        categoryRepository.save(getCategoryFromMap(requestData,true));
                        return Utils.getResponseEntity("CATEGORY UPDATED SUCCESSFULLY", HttpStatus.OK);
                    }
                    return Utils.getResponseEntity("CATEGORY ID DOESNT EXISTS", HttpStatus.BAD_REQUEST);
                }
                return Utils.getResponseEntity(Constants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            } else {
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN UPDATING CATEGORY ", HttpStatus.INTERNAL_SERVER_ERROR);

    }

//validateId validate when the updating
    private boolean validateCategoryMap(Map<String, String> requestData, boolean validateId) {
        if (requestData.containsKey("name")) {
            if (requestData.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestData, Boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestData.get("id")));
        }
        category.setName(requestData.get("name"));
        return category;
    }
}
