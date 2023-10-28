package com.sn.shehan_n.cafe_management_system.repository;

import com.sn.shehan_n.cafe_management_system.entity.User;
import com.sn.shehan_n.cafe_management_system.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    //user dao
    public User findByEmailID(@Param("email") String email);

    List<UserWrapper> getAllUsers();

    //@Query(value = "UPDATE u.status WHERE User u ")
    @Transactional
    @Modifying
    void updateStatus(@Param("status") String status, @Param("id") Integer id);

    @Query(value ="select u.email from User u where u.role='admin'" )
    List<String> getAllAdmins();


    User findByEmail(String email);
}
