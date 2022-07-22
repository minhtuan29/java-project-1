package com.example.phanquyen.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

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

    @Override
    public String toString() {
        return "EmployeeInputModel{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", hometownID=" + hometownID +
                ", roleID=" + roleID +
                ", departmentID=" + departmentID +
                ", imgFile=" + imgFile +
                ", skills=" + Arrays.toString(skills) +
                '}';
    }
}
