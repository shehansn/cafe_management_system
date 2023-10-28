package com.sn.shehan_n.cafe_management_system.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@NamedQuery(name="User.findByEmailID",query = "select u from User u where u.email=:email")
@NamedQuery(name="User.getAllUsers",query = "select new com.sn.shehan_n.cafe_management_system.wrapper.UserWrapper(u.id,u.name,u.contactNumber,u.email,u.status) from User u where u.role='user'")
@NamedQuery(name="User.updateStatus",query = "update User u set u.status=:status where u.id=:id")
@NamedQuery(name="User.getAllAdmins",query = "select u.email from User u where u.role='admin'")


@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class User {

    private static final long serialVersionUID=1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;


//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
}
