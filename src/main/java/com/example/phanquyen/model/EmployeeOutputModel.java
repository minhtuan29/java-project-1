package com.example.phanquyen.model;

import lombok.Data;

@Data
public class EmployeeOutputModel {
    Long id;
    String name;
    Integer age;
    String phone;
    String hometown;
    String role;
    String department;
    String imgFileName;
    String skills;
}
