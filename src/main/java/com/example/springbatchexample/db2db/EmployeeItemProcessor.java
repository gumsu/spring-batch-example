package com.example.springbatchexample.db2db;

import com.example.springbatchexample.Employee;
import com.example.springbatchexample.Person;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Person> {

    @Override
    public Person process(Employee employee) throws Exception {
        Person person = new Person();
        person.setEmployeeNumber(employee.getEmployeeNumber());
        person.setEmployeeName(employee.getLastName());
        person.setEmail(employee.getEmail() + "@gmail.com");
        return person;
    }
}
