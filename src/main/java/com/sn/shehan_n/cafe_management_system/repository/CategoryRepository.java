package com.sn.shehan_n.cafe_management_system.repository;

import com.sn.shehan_n.cafe_management_system.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

    List<Category> getAllCategory();

    List<Category> getAllCategories();
}
