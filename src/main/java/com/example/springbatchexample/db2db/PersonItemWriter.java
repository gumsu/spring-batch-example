package com.example.springbatchexample.db2db;

import com.example.springbatchexample.Person;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class PersonItemWriter implements ItemWriter<Person> {

    private final SqlSessionTemplate sqlSessionTemplate;

    public PersonItemWriter(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public void write(Chunk<? extends Person> people) throws Exception {
        for (Person person : people) {
            try {
                sqlSessionTemplate.insert("com.example.springbatchexample.PeopleRepository.insertPeople", people);
            } catch (Exception e) {
                // 구체적인 오류 메시지 출력
                System.err.println("Error inserting person: " + person);
                e.printStackTrace();
                throw e; // 다시 던져서 오류를 처리할 수 있도록 함
            }
        }
//        for (Customer customer : customers) {
//            sqlSessionTemplate.insert("com.example.springbatchexample.CustomerRepository.insertCustomer", customer);
//        }
    }

}
