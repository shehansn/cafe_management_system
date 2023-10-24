package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.sn.shehan_n.cafe_management_system.auth.service.CustomUserDetailsService;
import com.sn.shehan_n.cafe_management_system.auth.util.JwtUtil;
import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.entity.User;
import com.sn.shehan_n.cafe_management_system.repository.UserRepository;
import com.sn.shehan_n.cafe_management_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    private UserRepository userRepository;//userDao
//
//    @Autowired
//    AuthenticationManager authenticationManager;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           CustomUserDetailsService customUserDetailsService,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestData) {
        log.info("Inside Signup {}", requestData);
        try {
            if (validateSignUpData(requestData)) {
                User user = userRepository.findByEmailID(requestData.get("email"));
                if (Objects.isNull(user)) {
                    log.info("Email not Found");
                    userRepository.save(getUserFromRequestData(requestData));
                    return Utils.getResponseEntity("Successfully Registered", HttpStatus.OK);

                } else {
                    log.info("Email Already Exists");
                    return Utils.getResponseEntity("Email Already Exists", HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestData) {
        log.info("Inside Login {}");
        try {
            if (validateLoginData(requestData)) {
                String email = requestData.get("email");
                String password = requestData.get("password");

                User user = userRepository.findByEmailID(requestData.get("email"));

                log.info(email, password, user);

                Authentication auth = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestData.get("email"), requestData.get("password"))
                );

                if (auth.isAuthenticated()) {
                    if (customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {

                        return new ResponseEntity<String>("{\"token\":\" " +
                                jwtUtil.generateToken(customUserDetailsService.getUserDetails().getEmail(),
                                        customUserDetailsService.getUserDetails().getRole()) + "\"}", HttpStatus.OK);

                    } else {
                        return Utils.getResponseEntity(Constants.NOT_APPROVED,HttpStatus.LOCKED);
//                        return new ResponseEntity<String>("{ \"message \" : \" " + "Wait for Admin Approvals." + " \" }", HttpStatus.BAD_REQUEST);

                    }
                }
//                User user= userRepository.findByEmailID(requestData.get("email"));
//                if(Objects.isNull(user)){
//                    log.info("Email not Found");
//                    //userRepository.save(getUserFromRequestData(requestData));
//                    return Utils.getResponseEntity("Email Not Found", HttpStatus.BAD_REQUEST);
//
//                }else{
//                    log.info("Email Exists");
//                    return Utils.getResponseEntity("Email Exists",HttpStatus.OK);
//                }
            } else {
                return Utils.getResponseEntity(Constants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("exception in authentication {}", ex);
            ex.printStackTrace();

        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        //        try {
//            Authentication auth = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(requestData.get("email"), requestData.get("password"))
//            );
//            log.info(String.valueOf(auth));
//
//            if (auth.isAuthenticated()) {
//                if (customUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
//
//                    return new ResponseEntity<String>("{\"token\":\" " +
//                            jwtUtil.generateToken(customUserDetailsService.getUserDetails().getEmail(),
//                                    customUserDetailsService.getUserDetails().getRole()) + "\"}", HttpStatus.OK);
//
//                } else {
//                    return new ResponseEntity<String>("{ \"message \" : \" " + "Wait for Admin Approvals." + " \" }", HttpStatus.BAD_REQUEST);
//
//                }
//            }
//
//        } catch (Exception ex) {
//            log.info("exception in authentication {}", ex);
//            ex.printStackTrace();
//        }
//        return Utils.getResponseEntity("Authentication failed",HttpStatus.BAD_REQUEST);

    }


    private boolean validateSignUpData(Map<String, String> requestData) {
        if (requestData.containsKey("name") && requestData.containsKey("contactNumber") && requestData.containsKey("email") && requestData.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateLoginData(Map<String, String> requestData) {
        if (requestData.containsKey("email") && requestData.containsKey("password")) {
            return true;
        } else {
            return false;
        }
    }

    private User getUserFromRequestData(Map<String, String> requestData) {
        User user = new User();
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
