package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.sn.shehan_n.cafe_management_system.auth.filter.TokenAuthenticationFilter;
import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.entity.Category;
import com.sn.shehan_n.cafe_management_system.entity.Product;
import com.sn.shehan_n.cafe_management_system.repository.ProductRepository;
import com.sn.shehan_n.cafe_management_system.service.ProductService;
import com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final ProductRepository productRepository;

    public ProductServiceImpl(TokenAuthenticationFilter tokenAuthenticationFilter, ProductRepository productRepository) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestData) {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {
                if (validateProductMap(requestData, false)) {
                    productRepository.save(getProductFromMap(requestData, false));
                    return Utils.getResponseEntity("PRODUCT ADDED SUCCESSFULLY", HttpStatus.OK);
                }
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN ADDING NEW PRODUCT ", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try {
            return new ResponseEntity<>(productRepository.getAllProducts(),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestData) {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {
                if (validateProductMap(requestData, true)) {
                    Optional<Product> productObj = productRepository.findById(Integer.parseInt(requestData.get("id")));
                    if(!productObj.isEmpty()){
                        Product product=getProductFromMap(requestData, true);
                        product.setStatus(productObj.get().getStatus());
                        productRepository.save(product);
                        return Utils.getResponseEntity("PRODUCT UPDATED SUCCESSFULLY", HttpStatus.OK);
                    }
                    else{
                        return Utils.getResponseEntity("PRODUCT DOESNT EXISTS", HttpStatus.OK);
                    }

                }
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN UPDATING PRODUCT ", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {
                Optional<Product> productObj = productRepository.findById(id);
                if(!productObj.isEmpty()){
                    productRepository.deleteById(id);
                    return Utils.getResponseEntity("PRODUCT DELETED SUCCESSFULLY", HttpStatus.OK);
                }else{
                    return Utils.getResponseEntity("PRODUCT DOESNT EXISTS", HttpStatus.OK);
                }

            }else{
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN DELETING PRODUCT ", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestData) {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {
                Optional<Product> productObj = productRepository.findById(Integer.parseInt(requestData.get("id")));
                if(!productObj.isEmpty()){
                    productRepository.updateProductStatus(Integer.parseInt(requestData.get("id")),requestData.get("status"));
                    return Utils.getResponseEntity("PRODUCT STATUS UPDATED SUCCESSFULLY", HttpStatus.OK);
                }
                else{
                    return Utils.getResponseEntity("PRODUCT DOESNT EXISTS", HttpStatus.OK);
                }
            }else{
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity("ERROR WHEN UPDATING PRODUCT STATUS", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategoryId(Integer id) {
        try {
            return new ResponseEntity<>(productRepository.getProductByCategoryId(id),HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductById(Integer id) {
        try {
//            return new ResponseEntity<>(productRepository.getProductById(id),HttpStatus.OK);
            return new ResponseEntity<>(productRepository.getProductById(id),HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Product getProductFromMap(Map<String, String> requestData, boolean isUpdate) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestData.get("categoryId")));

        Product product = new Product();
        if (isUpdate) {
            product.setId(Integer.parseInt(requestData.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestData.get("name"));
        product.setDescription(requestData.get("description"));
        product.setPrice(Integer.parseInt(requestData.get("price")));
        return product;
    }

    //use for add and update product
    private boolean validateProductMap(Map<String, String> requestData, boolean validateId) {
        if (requestData.containsKey("name") && requestData.containsKey("description")) {
            if (requestData.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }
}
