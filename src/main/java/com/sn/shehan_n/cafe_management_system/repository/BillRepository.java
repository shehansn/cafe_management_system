package com.sn.shehan_n.cafe_management_system.repository;

import com.sn.shehan_n.cafe_management_system.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Integer> {

    List<Bill> getAllBills();

    //@Query("select b FROM Bill b WHERE b.email=:username")
//    List<Bill> getAllByUserName(@Param("username") String username);
    List<Bill> getAllByUserID(@Param("userId") String userId);



}
