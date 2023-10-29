package com.sn.shehan_n.cafe_management_system.entity;

import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.security.PrivateKey;

@Getter
//@NamedQuery(name="Category.getAllCategory",query = "select c from Category c")
@NamedQuery(name="Category.getAllCategories",query = "select c from Category c")
@NamedQuery(name="Category.getAllCategory",query = "select c from Category c where c.id in (select p.category from Product p where p.status='true' )")


@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
public class Category implements Serializable {

    @Serial
    private static final Long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
