package net.javaguides.springboottesting.repository;

import net.javaguides.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    //define custom query using JPQl with index parameters
    @Query("Select e from Employee e where e.firstname = ?1 and e.lastname = ?2")
    Employee findByJPQL(String firstname, String lastname);

    //define custom query using JPQl with named parameters
    //in JPQL we use class name and class related variables as below
    @Query("Select e from Employee e where e.firstname = :firstname and e.lastname = :lastname")
    Employee findByJPQLNamedParams(@Param("firstname") String firstname, @Param("lastname") String lastname);

    //define custom query using native SQL with index params
    //in native SQL we use column names defined while creating table as shown below
    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByNativeSQL(String firstname, String lastname);

    //define custom query using native SQL with named params
    //in native SQL we use column names defined while creating table as shown below
    @Query(value = "select * from employees e where e.first_name = :firstname and e.last_name = :lastname", nativeQuery = true)
    Employee findByNativeSQLNamed(@Param("firstname") String firstname, @Param("lastname") String lastname);
}
