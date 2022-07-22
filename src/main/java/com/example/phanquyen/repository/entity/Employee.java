package com.example.phanquyen.repository.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Employee {
    @Id @GeneratedValue
    Long id;

    String name;
    Integer age;
    String phone;

    @ManyToOne
    @JoinColumn(name= "hometown_id")
    Hometown hometown;

    @ManyToOne @JoinColumn(name="role_id")
    Role role;

    @ManyToOne @JoinColumn(name="department_id")
    Department department;

    String imgFileName; // maybe null
    String skills; // maybe null

}
