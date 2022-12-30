package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.exceptions.ResourceNotFoundException;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import net.javaguides.springboottesting.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public  void setUp() {

         employee = Employee.builder()
                .id(1L)
                .firstname("gopichand")
                .lastname("yarra")
                .email("gopichandyarra.1@gmail.com")
                .build();

//        employeeRepository = Mockito.mock(EmployeeRepository.class); //removed as we used @mock
//        employeeService = new EmployeeServiceImpl(employeeRepository); //removed as we used @mock
    }

    //Junit test for saveEmployee method
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or behaviour we test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    //Junit test for saveEmployee method which throws exception
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of((employee)));
//        given(employeeRepository.save(employee)).willReturn(employee); // as this line wont execute as it is after if statement

        //when - action or behaviour we test
        assertThrows(ResourceNotFoundException.class, ()->{
            employeeService.saveEmployee(employee);
        });

        //then - verify the output
        verify(employeeRepository,never()).save(any(Employee.class));

    }

    //Junit test for get all employees method
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstname("tony")
                .lastname("stark")
                .email("tony.1@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or behaviour we test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    //Junit test for get all employees method (negative scenario)
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstname("tony")
                .lastname("stark")
                .email("tony.1@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.EMPTY_LIST);

        //when - action or behaviour we test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);

    }

    //Junit test for get employee by id method
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenEmployeeId() {

        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or behaviour we test
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    //Junit test for update employee
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstname("sai");
        employee.setEmail("saichand@gmail.com");

        //when - action or behaviour we test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then - verify the output
        assertThat(updatedEmployee.getFirstname()).isEqualTo("sai");
        assertThat(updatedEmployee.getEmail()).isEqualTo("saichand@gmail.com");
    }

    //Junit test for deleteEmployee method
    @Test
    public void givenEmployeeId_whenEmployeeDelete_thenNothing() {

        //given - precondition or setup
        willDoNothing().given(employeeRepository).deleteById(1L);

        //when - action or behaviour we test
        employeeService.employeeDelete(1L);

        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(1L);
    }

}
