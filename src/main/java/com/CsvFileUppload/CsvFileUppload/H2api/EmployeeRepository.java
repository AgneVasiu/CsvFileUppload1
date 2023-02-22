package com.CsvFileUppload.CsvFileUppload.H2api;

import com.CsvFileUppload.CsvFileUppload.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findEmployeeByName(String name);
}
