package com.example.springbatchexample.db2db;

import com.example.springbatchexample.Customer;
import com.example.springbatchexample.Person;
import org.springframework.batch.item.ItemProcessor;

public class CustomerItemProcessor implements ItemProcessor<Customer, Person> {

    @Override
    public Person process(Customer customer) throws Exception {
        Person person = new Person();
        person.setCustomerNumber(customer.getCustomerNumber());
        person.setCustomerName(customer.getCustomerName());
        person.setCountry(customer.getCountry());
        return person;
    }
}
