package com.banque.users_microservice.service;

import com.banque.users_microservice.entity.Employee;
import com.banque.users_microservice.producer.UserEventProducer;
import com.banque.users_microservice.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private EmployeeService employeeService;

    private SimpleDateFormat dateFormat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    void testCreateEmployee() throws ParseException {
        // Arrange
        Employee employee = new Employee();
        employee.setUsername("john.doe");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date
        employee.setAge(33);
        employee.setCity("New York");
        employee.setDepartment("IT");
        employee.setNationality("American");
        employee.setTelephoneNumber("1234567890");
        employee.setPassword("password");

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee createdEmployee = employeeService.createEmployee(employee);

        // Assert
        assertNotNull(createdEmployee);
        assertEquals("john.doe", createdEmployee.getUsername());
        assertEquals("John", createdEmployee.getFirstName());
        assertEquals("Doe", createdEmployee.getLastName());
        assertEquals(dateFormat.parse("1990-01-01"), createdEmployee.getDateOfBirthday()); // Vérification de la date
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(userEventProducer, times(1)).sendEmployeeEvent("CREATED", createdEmployee);
    }

    @Test
    void testGetEmployeeById() throws ParseException {
        // Arrange
        UUID id = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setId(id);
        employee.setUsername("john.doe");
        employee.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        // Act
        Optional<Employee> foundEmployee = employeeService.getEmployeeById(id);

        // Assert
        assertTrue(foundEmployee.isPresent());
        assertEquals(id, foundEmployee.get().getId());
        assertEquals("john.doe", foundEmployee.get().getUsername());
        assertEquals(dateFormat.parse("1990-01-01"), foundEmployee.get().getDateOfBirthday()); // Vérification de la date
        verify(employeeRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateEmployee() throws ParseException {
        // Arrange
        UUID id = UUID.randomUUID();
        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);
        existingEmployee.setUsername("john.doe");
        existingEmployee.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date

        Employee updatedEmployee = new Employee();
        updatedEmployee.setUsername("john.doe.updated");
        updatedEmployee.setFirstName("John Updated");
        updatedEmployee.setDateOfBirthday(dateFormat.parse("1995-05-05")); // Conversion en Date

        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        // Act
        Employee result = employeeService.updateEmployee(id, updatedEmployee);

        // Assert
        assertNotNull(result);
        assertEquals("john.doe.updated", result.getUsername());
        assertEquals("John Updated", result.getFirstName());
        assertEquals(dateFormat.parse("1995-05-05"), result.getDateOfBirthday()); // Vérification de la date
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(userEventProducer, times(1)).sendEmployeeEvent("UPDATED", result);
    }

    @Test
    void testUpdateEmployee_NotFound() throws ParseException {
        // Arrange
        UUID id = UUID.randomUUID();
        Employee updatedEmployee = new Employee();
        updatedEmployee.setUsername("john.doe.updated");
        updatedEmployee.setDateOfBirthday(dateFormat.parse("1995-05-05")); // Conversion en Date

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Employee result = employeeService.updateEmployee(id, updatedEmployee);

        // Assert
        assertNull(result);
        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, never()).save(any(Employee.class));
        verify(userEventProducer, never()).sendEmployeeEvent(anyString(), any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        employeeService.deleteEmployee(id);

        // Assert
        verify(employeeRepository, times(1)).deleteById(id);
        verify(userEventProducer, times(1)).sendEmployeeEvent(eq("DELETED"), argThat(employee -> id.equals(employee.getId())));
    }

    @Test
    void testGetAllEmployees() throws ParseException {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setUsername("john.doe");
        employee1.setDateOfBirthday(dateFormat.parse("1990-01-01")); // Conversion en Date

        Employee employee2 = new Employee();
        employee2.setUsername("jane.doe");
        employee2.setDateOfBirthday(dateFormat.parse("1995-05-05")); // Conversion en Date

        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(2, result.size());
        assertEquals(dateFormat.parse("1990-01-01"), result.get(0).getDateOfBirthday()); // Vérification de la date
        assertEquals(dateFormat.parse("1995-05-05"), result.get(1).getDateOfBirthday()); // Vérification de la date
        verify(employeeRepository, times(1)).findAll();
    }
}