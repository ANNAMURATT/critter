package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Employee;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.List;

@Repository
@Transactional
public interface EmployeeRepository extends UserRepository<Employee>{
    public List<Employee> findEmployeesByAvailableDays(DayOfWeek day);
}
