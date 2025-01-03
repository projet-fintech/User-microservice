package com.banque.users_microservice.service;

import com.banque.users_microservice.entity.Employee;
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserEventProducer userEventProducer;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

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
    void createEmployee_should_return_created_employee(){
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee createdEmployee = employeeService.createEmployee(employee);
        assertNotNull(createdEmployee);
        assertEquals(employee.getId(),createdEmployee.getId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(userEventProducer, times(1)).sendEmployeeEvent(eq("CREATED"), any(Employee.class));
    }


    @Test
    void getEmployeeById_should_return_employee_when_exist() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        Optional<Employee> foundEmployee = employeeService.getEmployeeById(employee.getId());
        assertTrue(foundEmployee.isPresent());
        assertEquals(employee.getId(), foundEmployee.get().getId());
        verify(employeeRepository, times(1)).findById(employee.getId());
    }

    @Test
    void getEmployeeById_should_return_empty_when_not_exist() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(id);
        assertFalse(foundEmployee.isPresent());
        verify(employeeRepository, times(1)).findById(id);
    }


    @Test
    void getAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee,new Employee()));

        List<Employee> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void updateEmployee_should_return_updated_employee_when_exist() {
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        Employee newEmployee = new Employee();
        newEmployee.setUsername("testuser2");
        newEmployee.setFirstName("Test2");
        newEmployee.setLastName("User2");
        newEmployee.setDateOfBirthday(new Date());
        newEmployee.setAge(32);
        newEmployee.setCity("TestCity2");
        newEmployee.setDepartment("TestDepartment2");
        newEmployee.setNationality("TestNation2");
        newEmployee.setTelephoneNumber("123-456-7892");
        newEmployee.setPassword("password2");

        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);
        Employee updateEmployee = employeeService.updateEmployee(employee.getId(), newEmployee);
        assertNotNull(updateEmployee);
        assertEquals(newEmployee.getUsername(), updateEmployee.getUsername());
        verify(employeeRepository, times(1)).findById(employee.getId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(userEventProducer, times(1)).sendEmployeeEvent(eq("UPDATED"), any(Employee.class));
    }


    @Test
    void updateEmployee_should_return_null_when_not_exist() {
        UUID id = UUID.randomUUID();
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        Employee newEmployee = new Employee();
        newEmployee.setUsername("testuser2");
        newEmployee.setFirstName("Test2");
        newEmployee.setLastName("User2");
        newEmployee.setDateOfBirthday(new Date());
        newEmployee.setAge(32);
        newEmployee.setCity("TestCity2");
        newEmployee.setDepartment("TestDepartment2");
        newEmployee.setNationality("TestNation2");
        newEmployee.setTelephoneNumber("123-456-7892");
        newEmployee.setPassword("password2");

        Employee updateEmployee = employeeService.updateEmployee(id, newEmployee);
        assertNull(updateEmployee);
        verify(employeeRepository, times(1)).findById(id);
    }
    @Test
    void deleteEmployee() {
        doNothing().when(employeeRepository).deleteById(employee.getId());
        employeeService.deleteEmployee(employee.getId());
        verify(employeeRepository, times(1)).deleteById(employee.getId());
        verify(userEventProducer, times(1)).sendEmployeeEvent(eq("DELETED"), any(Employee.class));
    }
}
