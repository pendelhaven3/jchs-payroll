package com.jchs.payrollapp.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jchs.payrollapp.model.Employee;
import com.jchs.payrollapp.model.Salary;

public interface SalaryRepository extends JpaRepository<Salary, Long> {

    @Query("select s from Salary s where s.employee = :employee and (s.effectiveDateTo is null or s.effectiveDateTo >= :date)")
    Salary findCurrentSalary(@Param("employee") Employee employee, @Param("date") Date currentDate);

}
