package net.javaguides.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITestContainer extends AbstractContainerBaseTest {

    /*
    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");//you can add .with methods to
    // set user defined password,databasename,usernames //by default all those values are test,test,test


    //registered this container into application context so that all integration test cases in same file can use this container
    //to extend this to other integration test cases in other files, we have to write these lines into every other file
    //or instead of creating mysql container in eac and every file, create an abstract class and move this code there
    //and extend that abstract class(singleton class) everywhere


    @DynamicPropertySource
    public static void dynamicPropertySource (DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);

    }
    */


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }

    //Junit integration test for create employee REST API
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("Gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();

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

    //Junit Integration test for get all employees
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception{

        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstname("gopi").lastname("chand").email("gopichand@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstname("harish").lastname("kumar").email("harishkumar@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listOfEmployees.size())));
    }

    //Junit Integration test for GET employee by Id REST API //+ve case as given correct employee Id and employee object will be retunred
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        ResultActions response  = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstname", is(employee.getFirstname())))
                .andExpect(jsonPath("$.lastname", is(employee.getLastname())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //Junit Integration test for GET employee by Id REST API //-ve case, pass invalid employee Id and optional empty will be returned
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws  Exception{

        //given - precondition or setup
        long employeeId = 1000L; //pass invalid employeeId
        Employee employee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        ResultActions response  = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //Junit Integration test for update employee REST API for positive scenario
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstname("ravi")
                .lastname("kumar")
                .email("ravi@gmail.com")
                .build();

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}",savedEmployee.getId())
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

    //Junit test for update employee REST API for negative scenario ie passed employeeId is invalid
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        //given - precondition or setup
        long employeeId = 1000L; //pass invalid employee Id

        Employee savedEmployee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstname("ravi")
                .lastname("kumar")
                .email("ravi@gmail.com")
                .build();


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

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstname("gopi")
                .lastname("chand")
                .email("gopichand@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        //when - action or behaviour we test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        //then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());

    }
}
