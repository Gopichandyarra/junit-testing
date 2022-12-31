package net.javaguides.springboottesting.respository;

import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void employeeSetUp() {
        employee = Employee.builder()
                .firstname("gopichand")
                .lastname("yarra")
                .email("gopichandyarra.1@gmail.com")
                .build();
    }

    //Junit test for save employee operation
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("ramesh")
//                .lastname("mandadapu")
//                .email("rameshmandadapu123@gmail.com")
//                .build();

        //when - action or behaviour we test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    //Junit test for get all employee
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstname("ramesh")
                .lastname("mandadapu")
                .email("rameshmandadapu123@gmail.com")
                .build();

        Employee employee2 = Employee.builder()
                .firstname("gopichand")
                .lastname("yarra")
                .email("gopichandyarra.1@gmail.com")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or behaviour we test
        List<Employee> employeeList =employeeRepository.findAll();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(3); //cause we are saving another employee in @BeforeEach
    }

    //Junit test for get employee by Id
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    //Junit test for get employee by email operation
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        //then - verify the output
        assertThat(employeeDB).isNotNull();

    }

    //Junit test for update employee
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();

        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("chanducrazy9999@gmail.com");
        savedEmployee.setFirstname("gopi");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("chanducrazy9999@gmail.com");
        assertThat(updatedEmployee.getFirstname()).isEqualTo("gopi");

    }

    //Junit test for delete Employee
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then - verify the output
        assertThat(employeeOptional).isEmpty();

    }

    //Junit test for custom query using JPQL with index
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstname(),employee.getLastname());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    //Junit test for custom query using JPQL with named params
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(employee.getFirstname(),employee.getLastname());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }


    //Junit test for custom query using native SQL with index
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstname(),employee.getLastname());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    //Junit test for custom query using native SQL with named params
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {

        //given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstname("gopichand")
//                .lastname("yarra")
//                .email("gopichandyarra.1@gmail.com")
//                .build();
        employeeRepository.save(employee);

        //when - action or behaviour we test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstname(),employee.getLastname());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

}


