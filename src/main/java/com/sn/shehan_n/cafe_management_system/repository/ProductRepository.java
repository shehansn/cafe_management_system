package com.sn.shehan_n.cafe_management_system.repository;

import com.sn.shehan_n.cafe_management_system.entity.Product;
import com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<ProductWrapper> getAllProducts();

    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("id") Integer id,@Param("status") String status);
}
