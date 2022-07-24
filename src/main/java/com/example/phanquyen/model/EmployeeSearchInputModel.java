package com.example.phanquyen.model;


import lombok.Data;

@Data
public class EmployeeSearchInputModel extends EmployeeInputModel{
    Integer minAge;
    Integer maxAge;
}
