package com.example.phanquyen.repository.jparepo;

import com.example.phanquyen.repository.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
}
