package com.sn.shehan_n.cafe_management_system.auth.service;

import com.sn.shehan_n.cafe_management_system.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{


    @Autowired
    UserRepository userRepository;

    private com.sn.shehan_n.cafe_management_system.entity.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("inside loadUserByUsername {}", username);
        userDetail = userRepository.findByEmailID(username);
        if (!Objects.isNull(userDetail))
            return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        else
            throw new UsernameNotFoundException("User Not Found");
    }

    public com.sn.shehan_n.cafe_management_system.entity.User getUserDetails() {
        return userDetail;
    }
}
