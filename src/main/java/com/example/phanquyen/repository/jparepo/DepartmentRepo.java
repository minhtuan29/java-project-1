package com.example.phanquyen.repository.jparepo;

import com.example.phanquyen.repository.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepo extends JpaRepository<Department, Integer> {
}
