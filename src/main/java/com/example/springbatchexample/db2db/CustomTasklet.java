package com.example.springbatchexample.db2db;

import com.example.springbatchexample.Customer;
import com.example.springbatchexample.Employee;
import com.example.springbatchexample.Person;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomTasklet implements Tasklet {

    private final SqlSessionTemplate sqlSessionTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 1. 데이터 읽기
        List<Customer> customers = readCustomers();
        List<Employee> employees = readEmployees();

        // 2. 데이터 가공
        List<Person> persons = new ArrayList<>();
        for (Customer customer : customers) {
            Person person = new Person();
            person.setCustomerNumber(customer.getCustomerNumber());
            person.setCustomerName(customer.getCustomerName());
            person.setCountry(customer.getCountry());
            persons.add(person);
        }

        for (Employee employee : employees) {
            Person person = new Person();
            person.setEmployeeNumber(employee.getEmployeeNumber());
            person.setEmployeeName(employee.getLastName());
            person.setEmail(employee.getEmail() + "@gmail.com");
            persons.add(person);
        }

        // 3. 데이터 쓰기
        writePersons(persons);

        return RepeatStatus.FINISHED; // 작업 완료
    }

    private List<Customer> readCustomers() {
        return sqlSessionTemplate.selectList("com.example.springbatchexample.CustomerRepository.selectCustomer");
    }

    private List<Employee> readEmployees() {
        return sqlSessionTemplate.selectList("com.example.springbatchexample.EmployeeRepository.selectEmployee");
    }

    private void writePersons(List<Person> persons) {
        for (Person person : persons) {
            sqlSessionTemplate.insert("com.example.springbatchexample.PersonRepository.insertPerson", person);
        }
    }
}
