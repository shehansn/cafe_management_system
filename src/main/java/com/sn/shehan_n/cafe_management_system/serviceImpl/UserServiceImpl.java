package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.entity.User;
import com.sn.shehan_n.cafe_management_system.repository.UserRepository;
import com.sn.shehan_n.cafe_management_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;//userDao

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestData) {
        log.info("Inside Signup {}",requestData);
        try{
            if(validateSignUpData(requestData)){
                User user= userRepository.findByEmailID(requestData.get("email"));
                if(Objects.isNull(user)){
                    log.info("Email not Found");
                    userRepository.save(getUserFromRequestData(requestData));
                    return Utils.getResponseEntity("Successfully Registered", HttpStatus.OK);

                }else{
                    log.info("Email Already Exists");
                    return Utils.getResponseEntity("Email Already Exists",HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestData) {
        log.info("Inside Login {}",requestData);
        try{
            if(validateLoginData(requestData)){
                User user= userRepository.findByEmailID(requestData.get("email"));
                if(Objects.isNull(user)){
                    log.info("Email not Found");
                    //userRepository.save(getUserFromRequestData(requestData));
                    return Utils.getResponseEntity("Email Not Found", HttpStatus.BAD_REQUEST);

                }else{
                    log.info("Email Exists");
                    return Utils.getResponseEntity("Email Exists",HttpStatus.OK);
                }
            }else{
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);

            }
        }catch (Exception ex){
            ex.printStackTrace();

        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private boolean validateSignUpData(Map<String,String > requestData){
        if (requestData.containsKey("name") && requestData.containsKey("contactNumber") && requestData.containsKey("email") && requestData.containsKey("password")){
            return true;
        }else {
            return false;
        }
    }

    private boolean validateLoginData(Map<String,String > requestData){
        if (requestData.containsKey("email") && requestData.containsKey("password")){
            return true;
        }else {
            return false;
        }
    }

    private User getUserFromRequestData(Map<String,String> requestData){
        User user=new User();
        user.setName(requestData.get("name"));
        user.setContactNumber(requestData.get("contactNumber"));
        user.setEmail(requestData.get("email"));
        user.setPassword(requestData.get("password"));
        user.setRole(requestData.get("role"));
        user.setStatus(requestData.get("status"));

        return user;
    }

}

//
//{
//        "name":"shehan",
//        "contactNumber":"07698541236",
//        "email":"shehan4@gmail.com",
//        "password":"1234",
//        "role":"user",
//        "status":"1"
//
//        }
