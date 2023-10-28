package com.sn.shehan_n.cafe_management_system.controllerImpl;

import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.controller.UserController;
import com.sn.shehan_n.cafe_management_system.service.UserService;
import com.sn.shehan_n.cafe_management_system.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserControllerImpl implements UserController {
    @Autowired
    private UserService userService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestData) {
        try {
            return userService.signUp(requestData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //return new ResponseEntity<String>("{\"Message \":\"Error Occured In signup\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        return Utils.getResponseEntity(Constants.ERROR_OCCURED_IN_SIGNUP, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> signUp2(Map<String, String> requestData) {
        try {
            return userService.signUp(requestData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //return new ResponseEntity<String>("{\"Message \":\"Error Occured In signup\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        return Utils.getResponseEntity(Constants.ERROR_OCCURED_IN_SIGNUP, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(Map<String, String> requestData) {
        try {
            return userService.login(requestData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //return new ResponseEntity<String>("{\"Message \":\"Error Occured In signup\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        return Utils.getResponseEntity(Constants.ERROR_OCCURED_IN_LOGIN, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            return userService.getAllUsers();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestData) {
        try{
            return userService.update(requestData);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.CANNOT_UPDATE_STATUS,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try{
            return userService.checkToken();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.UNABLE_TO_VALIDATE,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestdata) {
        try {
            return userService.changePassword(requestdata);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.UNABLE_TO_CHANGE_PASSWORD,HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
