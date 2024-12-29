package com.banque.users_microservice.controllers;

import com.banque.users_microservice.controller.EmployeeController;
import com.banque.users_microservice.entity.Employee;
import com.banque.users_microservice.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private Employee employee;
    private UUID employeeId;

    @BeforeEach
    void setUp() {
        employeeId = UUID.randomUUID();
        employee = new Employee();
        employee.setId(employeeId);
        employee.setUsername("testuser");
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setPassword("password");
        employee.setAge(30);
        employee.setCity("TestCity");
        employee.setDepartment("TestDepartment");
        employee.setNationality("TestNationality");
        employee.setTelephoneNumber("123456789");
        employee.setDateOfBirthday(new Date());
    }

    @Test
    void createEmployee() {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);
        ResponseEntity<Employee> response = employeeController.createEmployee(employee);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }


    @Test
    void getEmployeeById_ExistingEmployee() {
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(employee));
        ResponseEntity<Employee> response = employeeController.getEmployeeById(employeeId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    void getEmployeeById_NonExistingEmployee() {
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.empty());
        ResponseEntity<Employee> response = employeeController.getEmployeeById(employeeId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllEmployees() {
        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);
        List<Employee> result = employeeController.getAllEmployees();
        assertEquals(employees, result);
    }

    @Test
    void updateEmployee_ExistingEmployee() {
        when(employeeService.updateEmployee(employeeId, employee)).thenReturn(employee);
        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeId, employee);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).updateEmployee(employeeId, employee);
    }


    @Test
    void updateEmployee_NonExistingEmployee() {
        when(employeeService.updateEmployee(employeeId, employee)).thenReturn(null);
        ResponseEntity<Employee> response = employeeController.updateEmployee(employeeId, employee);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(employeeService, times(1)).updateEmployee(employeeId, employee);
    }

    @Test
    void deleteEmployee(){
        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }
}
