package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.schedule.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PetService petService;


    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertDTOToSchedule(scheduleDTO);
        schedule = scheduleService.save(schedule);
        return convertScheduleToDTO(schedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAll();
        return convertScheduleListToDTOList(schedules);
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.findByPetId(petId);
        return convertScheduleListToDTOList(schedules);
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.findByEmployeeId(employeeId);
        return convertScheduleListToDTOList(schedules);
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.findByEmployeeId(customerId);
        return convertScheduleListToDTOList(schedules);
    }


    /**
     * DTO converters only beyond this line
     *
     */

    private Schedule convertDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        if (scheduleDTO.getEmployeeIds() != null) {
            List<Employee> employeeList = userService.findAllEmployeesByIdList(scheduleDTO.getEmployeeIds());
            schedule.setEmployees(employeeList);
        }

        if (scheduleDTO.getPetIds() != null) {
            List<Pet> petList = petService.findAllByIds(scheduleDTO.getPetIds());
            schedule.setPets(petList);
        }
        return schedule;
    }

    private ScheduleDTO convertScheduleToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> employeeIds = schedule.getEmployees().stream().map(Employee::getId).collect(Collectors.toList());
        List<Long> petIds = schedule.getPets().stream().map(Pet::getId).collect(Collectors.toList());
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        return scheduleDTO;
    }

    private List<ScheduleDTO> convertScheduleListToDTOList(List<Schedule> schedules) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        for (Schedule schedule : schedules) {
            scheduleDTOs.add(convertScheduleToDTO(schedule));
        }
        return scheduleDTOs;
    }
}
