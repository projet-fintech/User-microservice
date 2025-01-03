package com.banque.users_microservice.controller;


import com.banque.users_microservice.entity.Employee;
import com.banque.users_microservice.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class EmployeeControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Employee employee;
    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setUsername("testuser");
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setDateOfBirthday(new Date());
        employee.setAge(30);
        employee.setCity("TestCity");
        employee.setDepartment("TestDepartment");
        employee.setNationality("TestNation");
        employee.setTelephoneNumber("123-456-7890");
        employee.setPassword("password");
    }

    @Test
    void createEmployee_should_return_created_employee() throws Exception{

        mockMvc.perform(post("/users/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId().toString()));

    }

    @Test
    void getEmployeeById_should_return_employee_when_exist() throws Exception {
        Employee savedEmployee = employeeRepository.save(employee);
        mockMvc.perform(get("/users/employees/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedEmployee.getId().toString()));
    }
    @Test
    void getEmployeeById_should_return_not_found_when_not_exist() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/users/employees/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateEmployee_should_return_updated_employee() throws Exception {
        Employee savedEmployee = employeeRepository.save(employee);
        Employee updatedEmployee = new Employee();
        updatedEmployee.setUsername("testuser2");
        updatedEmployee.setFirstName("Test2");
        updatedEmployee.setLastName("User2");
        updatedEmployee.setDateOfBirthday(new Date());
        updatedEmployee.setAge(32);
        updatedEmployee.setCity("TestCity2");
        updatedEmployee.setDepartment("TestDepartment2");
        updatedEmployee.setNationality("TestNation2");
        updatedEmployee.setTelephoneNumber("123-456-7892");
        mockMvc.perform(put("/users/employees/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updatedEmployee.getUsername()));

    }

    @Test
    void updateEmployee_should_return_not_found_when_not_exist() throws Exception {
        UUID id = UUID.randomUUID();
        Employee updatedEmployee = new Employee();
        updatedEmployee.setUsername("testuser2");
        updatedEmployee.setFirstName("Test2");
        updatedEmployee.setLastName("User2");
        updatedEmployee.setDateOfBirthday(new Date());
        updatedEmployee.setAge(32);
        updatedEmployee.setCity("TestCity2");
        updatedEmployee.setDepartment("TestDepartment2");
        updatedEmployee.setNationality("TestNation2");
        updatedEmployee.setTelephoneNumber("123-456-7892");

        mockMvc.perform(put("/users/employees/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isNotFound());

    }


    @Test
    void deleteEmployee_should_return_no_content() throws Exception{
        Employee savedEmployee = employeeRepository.save(employee);
        mockMvc.perform(delete("/users/employees/" + savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}