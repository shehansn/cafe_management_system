package com.sn.shehan_n.cafe_management_system.wrapper;

import com.sn.shehan_n.cafe_management_system.entity.Category;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class ProductWrapper {
     Integer id;
     String name;
     String description;
     Integer price;
     String status;
     Integer categoryId;
     String categoryName;

    public ProductWrapper() {
    }

    public ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

}
