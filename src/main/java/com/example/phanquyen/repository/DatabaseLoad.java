package com.example.phanquyen.repository;

import com.example.phanquyen.repository.entity.Department;
import com.example.phanquyen.repository.entity.Hometown;
import com.example.phanquyen.repository.entity.Role;
import com.example.phanquyen.repository.jparepo.DepartmentRepo;
import com.example.phanquyen.repository.jparepo.EmployeeRepo;
import com.example.phanquyen.repository.jparepo.HometownRepo;
import com.example.phanquyen.repository.jparepo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DatabaseLoad {
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    HometownRepo hometownRepo;

    @Autowired
    DepartmentRepo departmentRepo;


    public List<Hometown> hometownList;
    public List<Role> roleList;
    public List<Department> departmentList;
    public Map<String, Integer> hometownMapStrInt;

    @PostConstruct
    private void loadData(){
        this.departmentList = departmentRepo.findAll();
        this.hometownList = hometownRepo.findAll();
        this.roleList = roleRepo.findAll();

        this.hometownMapStrInt = new HashMap<>();
        for(var hometown : hometownList){
            this.hometownMapStrInt.put(hometown.getName(), hometown.getId());
        }
    }
}
