package com.example.phanquyen.service;


import com.example.phanquyen.repository.entity.Employee;
import com.example.phanquyen.repository.jparepo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;

    public Page<Employee> findAllPageable(Pageable pageable) {
        return employeeRepo.findAll(pageable);
    }
}
