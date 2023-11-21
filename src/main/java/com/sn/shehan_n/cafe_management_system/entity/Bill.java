package com.sn.shehan_n.cafe_management_system.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "Bill.getAllBills",query = "select b from Bill b order by b.id desc ")
//@NamedQuery(name = "Bill.getAllByUserName",query = "select b from Bill b where b.email=:username order by b.id desc ")
@NamedQuery(name = "Bill.getAllByUserID",query = "select b from Bill b where b.createdBy=:userId order by b.id desc ")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "bill")
public class Bill implements Serializable {

    @Serial
    private static final Long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "contactNo")
    private String contactNo;

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @Column(name = "total")
    private Double total;

    @Column(name = "productDetails",columnDefinition = "json")
    private String productDetails;

    @Column(name = "createdBy")
    private String createdBy;

//    @ManyToOne
//    @JoinColumn(name = "createdBy")
//    private User createdBy;


}
