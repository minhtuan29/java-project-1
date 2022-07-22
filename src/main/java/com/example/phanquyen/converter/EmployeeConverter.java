package com.example.phanquyen.converter;

import com.example.phanquyen.model.EmployeeInputModel;
import com.example.phanquyen.model.EmployeeOutputModel;
import com.example.phanquyen.repository.entity.Department;
import com.example.phanquyen.repository.entity.Employee;
import com.example.phanquyen.repository.entity.Hometown;
import com.example.phanquyen.repository.entity.Role;
import com.example.phanquyen.repository.jparepo.DepartmentRepo;
import com.example.phanquyen.repository.jparepo.HometownRepo;
import com.example.phanquyen.repository.jparepo.RoleRepo;
import com.example.phanquyen.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmployeeConverter {
    @Autowired
    HometownRepo hometownRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    @Autowired
    RoleRepo roleRepo;



    public Employee convertToEntity(EmployeeInputModel employeeInputModel){
        Employee employee = new Employee();

        employee.setName(employeeInputModel.getName());
        employee.setAge(employeeInputModel.getAge());
        employee.setPhone(employeeInputModel.getPhone());

        employee.setHometown(new Hometown());
        employee.getHometown().setId(employeeInputModel.getHometownID());

        employee.setDepartment(new Department());
        employee.getDepartment().setId(employeeInputModel.getDepartmentID());

        employee.setRole(new Role());
        employee.getRole().setId(employeeInputModel.getRoleID());

        if(employeeInputModel.getSkills() != null){
            employee.setSkills(   String.join(",", employeeInputModel.getSkills())  );
        }
        if(employeeInputModel.getImgFile().isEmpty()){
            employee.setImgFileName(null);
        }else{
            employee.setImgFileName(DateTimeUtils.getDateStrInFormat("yyMMddHHmmss-")+employeeInputModel.getImgFile().getOriginalFilename());
        }
        return employee;
    }

    public EmployeeOutputModel convertToEmployeeOuputModel(Employee employeeEntity){
         EmployeeOutputModel output = new EmployeeOutputModel();
         output.setId(employeeEntity.getId());
         output.setName(employeeEntity.getName());
         output.setAge(employeeEntity.getAge());
         output.setPhone(employeeEntity.getPhone());
         output.setDepartment(employeeEntity.getDepartment().getName());
         output.setHometown(employeeEntity.getHometown().getName());
         output.setRole(employeeEntity.getRole().getName());
         output.setImgFileName(employeeEntity.getImgFileName());
         output.setSkills(employeeEntity.getSkills());
         return output;
    }


}
