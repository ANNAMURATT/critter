package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exceptions.NotFoundException;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PetService petService;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertDTOToCustomer(customerDTO);
        userService.saveCustomer(customer);
        return convertCustomerToDTO(customer);

    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customerList = userService.findAllCustomers();
        return convertCustomerListToDTO(customerList);
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Customer customer = userService.findCustomerByPet(petId);
        return convertCustomerToDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertDTOToEmployee(employeeDTO);
        userService.saveEmployee(employee);
        return convertEmployeeToDTO(employee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = userService.findEmployeeById(employeeId);
        if (employee != null) {
            return convertEmployeeToDTO(employee);
        }
        throw new NotFoundException("No employee found with id: " + employeeId);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.setEmployeeAvailability(employeeId,daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = userService.findEmployeeBySkills(employeeDTO.getSkills(),employeeDTO.getDate());
        return convertEmployeeListToDTOList(employees);
    }



    private Customer convertDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    private Employee convertDTOToEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        return employee;
    }

    private CustomerDTO convertCustomerToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO, "pets");
        List<Long> petIds = new ArrayList<>();
        if (customer.getPets() != null) {
            for (Pet pet : customer.getPets()) {
                petIds.add(pet.getId());
            }
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private EmployeeDTO convertEmployeeToDTO(Employee employee){
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }

    private List<EmployeeDTO> convertEmployeeListToDTOList(List<Employee> employees) {
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee employee : employees) {
            employeeDTOs.add(convertEmployeeToDTO(employee));
        }
        return employeeDTOs;
    }

    private List<CustomerDTO> convertCustomerListToDTO(List<Customer> customers){
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer : customers){
            customerDTOS.add(convertCustomerToDTO(customer));
        }return customerDTOS;
    }
}
