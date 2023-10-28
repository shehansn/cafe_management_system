package com.sn.shehan_n.cafe_management_system.serviceImpl;

import com.sn.shehan_n.cafe_management_system.auth.filter.TokenAuthenticationFilter;
import com.sn.shehan_n.cafe_management_system.auth.service.CustomUserDetailsService;
import com.sn.shehan_n.cafe_management_system.auth.util.JwtUtil;
import com.sn.shehan_n.cafe_management_system.commons.constants.Constants;
import com.sn.shehan_n.cafe_management_system.commons.util.Utils;
import com.sn.shehan_n.cafe_management_system.email.Utils.EmailUtils;
import com.sn.shehan_n.cafe_management_system.entity.User;
import com.sn.shehan_n.cafe_management_system.repository.UserRepository;
import com.sn.shehan_n.cafe_management_system.service.UserService;
import com.sn.shehan_n.cafe_management_system.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final EmailUtils emailUtils;

    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           CustomUserDetailsService customUserDetailsService,
                           JwtUtil jwtUtil, TokenAuthenticationFilter tokenAuthenticationFilter, EmailUtils emailUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.emailUtils = emailUtils;
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

                        return new ResponseEntity<String>("{ \"token\":\"" +
                                jwtUtil.generateToken(customUserDetailsService.getUserDetails().getEmail(),
                                        customUserDetailsService.getUserDetails().getRole()) + "\"}", HttpStatus.OK);

                    } else {
                        return Utils.getResponseEntity(Constants.NOT_APPROVED, HttpStatus.LOCKED);
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

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try {
            if (tokenAuthenticationFilter.isAdmin()) {

                return new ResponseEntity<>(userRepository.getAllUsers(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestData) {
        try {
            //tokenAuthenticationFilter.isUser() ||
            if (tokenAuthenticationFilter.isAdmin()) {
                Optional<User> foundUser = userRepository.findById(Integer.parseInt(requestData.get("id")));
                String foundStatus = String.valueOf(foundUser.get().getStatus());
                //!foundUser.isEmpty
                if (foundUser.isPresent()) {
                    userRepository.updateStatus(requestData.get("status"), Integer.parseInt(requestData.get("id")));
                    String userEmail = foundUser.get().getEmail();
                    String status = requestData.get("status");
                    List<String> listOfEmails = userRepository.getAllAdmins();
                    sendMailToAllAdmins(status, userEmail, listOfEmails);

                    return Utils.getResponseEntity("USER STATUS UPDATE SUCCESSFULLY", HttpStatus.OK);

                } else {
                    return Utils.getResponseEntity("USER ID DOESNT EXISTS", HttpStatus.OK);
                }
            } else {
                return Utils.getResponseEntity(Constants.UNAUTHERIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return Utils.getResponseEntity("true", HttpStatus.OK);
//        try {
//            return Utils.getResponseEntity("true", HttpStatus.OK);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestdata) {
        try {
            String currentUserEmail = tokenAuthenticationFilter.getCurrentUser();
            User userObj = userRepository.findByEmail(currentUserEmail);

            if (!userObj.equals(null)) {
                if (userObj.getPassword().equals(requestdata.get("oldPassword"))) {
                    userObj.setPassword(requestdata.get("newPassword"));
                    userRepository.save(userObj);
                    return Utils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                }
                return Utils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return Utils.getResponseEntity(Constants.UNABLE_TO_FIND_USER, HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Utils.getResponseEntity(Constants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private void sendMailToAllAdmins(String status, String userEmail, List<String> allAdmins) {
        log.info(status + userEmail + allAdmins);
        String currentUserEmail = tokenAuthenticationFilter.getCurrentUser();
        allAdmins.remove(currentUserEmail);
        log.info("after removing current user" + allAdmins);

        if (status != null && status.equalsIgnoreCase("true")) {

            emailUtils.sendSimpleMessage(currentUserEmail, "Account Approved", "USER : " + userEmail + "\n is Approved By ADMIN :" + currentUserEmail, allAdmins);

        } else {
            emailUtils.sendSimpleMessage(currentUserEmail, "Account Disabled", "USER : " + userEmail + "\n is Disabled. Send for ADMIN Approvals", allAdmins);

        }

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
