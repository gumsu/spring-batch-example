package com.example.springbatchexample;

import java.util.List;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.ItemReader;

public class CustomerItemReader implements ItemReader<Customer> {
    private final SqlSessionFactory sqlSessionFactory;
    private int currentIndex = 0;
    private List<Customer> datas;

    public CustomerItemReader(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        loadData();
    }

    private void loadData() {
        try (var session = sqlSessionFactory.openSession()) {
            datas = session.selectList("com.example.springbatchexample.CustomerRepository.selectCustomer");
        }
    }

    @Override
    public Customer read() {
        if (currentIndex < datas.size()) {
            return datas.get(currentIndex++);
        } else {
            return null; // 더 이상 데이터가 없을 때 null 반환
        }
    }
}
