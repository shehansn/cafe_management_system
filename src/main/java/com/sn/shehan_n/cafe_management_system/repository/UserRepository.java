package com.sn.shehan_n.cafe_management_system.repository;

import com.sn.shehan_n.cafe_management_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    public User findByEmailID(@Param("email") String email);

}
