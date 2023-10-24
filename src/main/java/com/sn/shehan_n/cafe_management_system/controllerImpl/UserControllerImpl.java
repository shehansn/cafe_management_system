package com.sn.shehan_n.cafe_management_system.controllerImpl;

import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.controller.UserController;
import com.sn.shehan_n.cafe_management_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
        return Utils.getResponseEntity("Error Occured In SignUp", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> signUp2(Map<String, String> requestData) {
        try {
            return userService.signUp(requestData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //return new ResponseEntity<String>("{\"Message \":\"Error Occured In signup\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        return Utils.getResponseEntity("Error Occured In SignUp", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> login(Map<String, String> requestData) {
        try {
            return userService.login(requestData);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //return new ResponseEntity<String>("{\"Message \":\"Error Occured In signup\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        return Utils.getResponseEntity("Error Occured In Login", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
