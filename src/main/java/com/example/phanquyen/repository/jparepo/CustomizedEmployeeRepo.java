package com.example.phanquyen.repository.jparepo;

import com.example.phanquyen.model.EmployeeSearchInputModel;
import com.example.phanquyen.repository.entity.Employee;

import java.util.List;

public interface CustomizedEmployeeRepo {
    List<Employee> findByCondition(EmployeeSearchInputModel employeeSearchInputModel, Integer nRecordPerPage, Integer pageAt, Integer[] outputNumPages);
}
