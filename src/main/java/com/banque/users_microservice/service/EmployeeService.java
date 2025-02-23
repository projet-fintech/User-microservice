package com.banque.users_microservice.service;


import com.banque.users_microservice.entity.Employee;
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserEventProducer userEventProducer;


    public EmployeeService(EmployeeRepository employeeRepository, UserEventProducer userEventProducer) {
        this.employeeRepository = employeeRepository;
        this.userEventProducer = userEventProducer;
    }

    public Employee createEmployee(Employee employee) {
        Employee newEmployee = new Employee();
        newEmployee.setUsername(employee.getUsername());
        newEmployee.setFirstName(employee.getFirstName());
        newEmployee.setLastName(employee.getLastName());
        newEmployee.setDateOfBirthday(employee.getDateOfBirthday());
        newEmployee.setAge(employee.getAge());
        newEmployee.setCity(employee.getCity());
        newEmployee.setDepartment(employee.getDepartment());
        newEmployee.setNationality(employee.getNationality());
        newEmployee.setTelephoneNumber(employee.getTelephoneNumber());
        newEmployee.setPassword(employee.getPassword());
        employeeRepository.save(newEmployee);
        userEventProducer.sendEmployeeEvent("CREATED",newEmployee);

        return newEmployee;
    }

    public Optional<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Employee updateEmployee(UUID id, Employee employee){
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if (existingEmployee != null) {
            existingEmployee.setUsername(employee.getUsername());
            existingEmployee.setFirstName(employee.getFirstName());
            existingEmployee.setLastName(employee.getLastName());
            existingEmployee.setDateOfBirthday(employee.getDateOfBirthday());
            existingEmployee.setAge(employee.getAge());
            existingEmployee.setCity(employee.getCity());
            existingEmployee.setDepartment(employee.getDepartment());
            existingEmployee.setNationality(employee.getNationality());
            existingEmployee.setTelephoneNumber(employee.getTelephoneNumber());
            existingEmployee.setPassword(employee.getPassword());
            employeeRepository.save(existingEmployee);
            userEventProducer.sendEmployeeEvent("UPDATED",existingEmployee);

            return existingEmployee;
        }
        return null;
    }

    public void deleteEmployee(UUID id) {
        employeeRepository.deleteById(id);
        userEventProducer.sendEmployeeEvent("DELETED", new Employee(id));
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
