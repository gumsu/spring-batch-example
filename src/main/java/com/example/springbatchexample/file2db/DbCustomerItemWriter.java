package com.example.springbatchexample.file2db;

import com.example.springbatchexample.Customer;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class DbCustomerItemWriter implements ItemWriter<Customer> {

    private final SqlSessionTemplate sqlSessionTemplate;

    public DbCustomerItemWriter(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public void write(Chunk<? extends Customer> customers) throws Exception {
        for (Customer customer : customers) {
            try {
                sqlSessionTemplate.insert("com.example.springbatchexample.CustomerRepository.insertCustomer", customer);
            } catch (Exception e) {
                // 구체적인 오류 메시지 출력
                System.err.println("Error inserting customer: " + customer);
                e.printStackTrace();
                throw e; // 다시 던져서 오류를 처리할 수 있도록 함
            }
        }
//        for (Customer customer : customers) {
//            sqlSessionTemplate.insert("com.example.springbatchexample.CustomerRepository.insertCustomer", customer);
//        }
    }
}
