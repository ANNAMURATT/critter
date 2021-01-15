package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByPetId(Long petId) {
        return scheduleRepository.findByPetsId(petId);
    }

    public List<Schedule> findByEmployeeId(Long employeeId) {
        return scheduleRepository.findByEmployeesId(employeeId);
    }

    public List<Schedule> findByCustomerId(Long customerId) {
        return scheduleRepository.findByPetsOwnerId(customerId);
    }
}
