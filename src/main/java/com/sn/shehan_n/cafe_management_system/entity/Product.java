package com.sn.shehan_n.cafe_management_system.entity;

import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name="Product.getAllProducts",query = "select new com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p ")

@NamedQuery(name="Product.updateProductStatus",query = "update Product p set p.status=:status where p.id=:id")

@NamedQuery(name="Product.getProductByCategoryId",query = "select new com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p where p.category.id=:id and p.status='true' ")

@NamedQuery(name="Product.getProductById",query = "select new com.sn.shehan_n.cafe_management_system.wrapper.ProductWrapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p where p.id=:id and p.status='true' ")

@Getter
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable {

    private static final Long serialVersionUID=123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk",nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
