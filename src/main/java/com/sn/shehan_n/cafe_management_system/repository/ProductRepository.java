package com.sn.shehan_n.cafe_management_system.repository;

import com.sn.shehan_n.cafe_management_system.entity.Product;
import com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<ProductWrapper> getAllProducts();
}
