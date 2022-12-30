package net.javaguides.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    //Junit test for create employee
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocation)->invocation.getArgument(0));

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(employee.getLastname())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Junit test for get all employees
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception{

        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstname("gopi").lastname("chand").email("gopichand@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstname("harish").lastname("kumar").email("harishkumar@gmail.com").build());
        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);


        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listOfEmployees.size())));
    }

    //Junit test for GET employee by Id REST API //+ve case as given correct employee Id and employee object will be retunred
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or behaviour we test
        ResultActions response  = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(employee.getLastname())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Junit test for GET employee by Id REST API //-ve case as we give invalid employee Id and optional empty will be returned
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws  Exception{

        //given - precondition or setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or behaviour we test
        ResultActions response  = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //Junit test for update employee REST API for positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstname("ravi")
                .lastname("kumar")
                .email("ravi@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocation)->invocation.getArgument(0));

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname",is(updatedEmployee.getFirstname())))
                .andExpect(jsonPath("$.lastname",is(updatedEmployee.getLastname())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    //Junit test for update employee REST API for negative scenario ie employeeId is invalid
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        //given - precondition or setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .firstname("ravi")
                .lastname("kumar")
                .email("ravi@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).willAnswer((invocation)->invocation.getArgument(0));

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Junit test for delete employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        long employeeId = 1L;
        //given - precondition or setup
        willDoNothing().given(employeeService).employeeDelete(employeeId);

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
