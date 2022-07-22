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

    @Override
    public String toString() {
        return "EmployeeOutputModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", hometown='" + hometown + '\'' +
                ", role='" + role + '\'' +
                ", department='" + department + '\'' +
                ", imgFileName='" + imgFileName + '\'' +
                ", skills='" + skills + '\'' +
                '}';
    }
}
