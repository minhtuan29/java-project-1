package com.example.phanquyen.repository.jparepo.Implement;

import com.example.phanquyen.model.EmployeeSearchInputModel;
import com.example.phanquyen.repository.entity.Employee;
import com.example.phanquyen.repository.jparepo.CustomizedEmployeeRepo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Repository
public class CustomizedEmployeeRepoImpl implements CustomizedEmployeeRepo {

    @PersistenceContext
    EntityManager entityManager;


    private static String get(String s){
        return "'"+s+"'";
    }

    private static String likeLeftRight(String s){
        return "'%"+s+"%'";
    }


    private static String getQuery(EmployeeSearchInputModel employeeSearchInputModel){
        StringBuilder queryBuilder = new StringBuilder( """
                select e from Employee e
                where 1 = 1
                """);

        // I dont choose Generalization the func below, otherwise it will be more complex for my project, not needed

        if(employeeSearchInputModel.getName() != null){
            // suppose that we have checked SQLinjection hack attack at Interceptor, now we just add this without valid
            queryBuilder.append("\n and e.name = " + get(employeeSearchInputModel.getName()));
        }

        if(employeeSearchInputModel.getMinAge() != null){
            queryBuilder.append("\n and e.age >= " + employeeSearchInputModel.getMinAge());
        }

        if(employeeSearchInputModel.getMaxAge() != null){
            queryBuilder.append("\n and e.age <= " + employeeSearchInputModel.getMaxAge());
        }

        if(employeeSearchInputModel.getPhone() != null){
            queryBuilder.append("\n and e.phone = " + get(employeeSearchInputModel.getPhone()));
        }

        if(employeeSearchInputModel.getDepartmentID() != null){
            queryBuilder.append("\n and e.departmentID = " + employeeSearchInputModel.getDepartmentID());
        }

        if(employeeSearchInputModel.getRoleID() != null){
            queryBuilder.append("\n and e.roleID = " + employeeSearchInputModel.getRoleID());
        }

        if(employeeSearchInputModel.getHometownID() != null){
            queryBuilder.append("\n and e.hometownID = " + employeeSearchInputModel.getHometownID());
        }

        if(employeeSearchInputModel.getSkills() != null){
            var skills = employeeSearchInputModel.getSkills();
            int SZ = skills.length;

            if(SZ == 1){
                queryBuilder.append("\n and e.skills LIKE " + likeLeftRight(skills[0]));
            }else {
                queryBuilder.append("\n and (");
                List<String> subQueryCondionList = new ArrayList<>();
                Arrays.stream(skills).forEach( e -> subQueryCondionList.add(" e.skills LIKE " + likeLeftRight(e)));
                queryBuilder.append(String.join(" or ", subQueryCondionList));
                queryBuilder.append(")");
            }
        }

        return queryBuilder.toString();
    }



    @Override
    public List<Employee> findByCondition(EmployeeSearchInputModel employeeSearchInputModel, Integer nRecordPerPage, Integer pageAt, Integer[] outputNumPages) {
        /*
            user : page  at 1 => database : page at 0
            user page 1: firstDBAt(0=0*10) nRecord(10)
            user page 2: firstDBAt(10=1*10) nRecord(10)
         */
        var queryAll = entityManager.createQuery(getQuery(employeeSearchInputModel), Employee.class);

        var queryPage = entityManager.createQuery(getQuery(employeeSearchInputModel), Employee.class);
        queryPage.setFirstResult(nRecordPerPage*pageAt);
        queryPage.setMaxResults(nRecordPerPage);
        outputNumPages[0] = (int) (Math.ceil( queryAll.getResultList().size() / nRecordPerPage));
        return queryPage.getResultList();
    }
}
