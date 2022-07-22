package com.example.phanquyen.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EmployeeInputModel {
    String name;
    Integer age;
    String phone;
    Integer hometownID;
    Integer roleID;
    Integer departmentID;
    MultipartFile imgFile; // maybe null
    String[] skills; // maybe null
}
