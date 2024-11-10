package com.example.springbatchexample.db2db;

import com.example.springbatchexample.Employee;
import java.util.List;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.item.ItemReader;

public class EmployeeItemReader implements ItemReader<Employee> {
    private final SqlSessionFactory sqlSessionFactory;
    private int currentIndex = 0;
    private List<Employee> datas;

    public EmployeeItemReader(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        loadData();
    }

    private void loadData() {
        try (var session = sqlSessionFactory.openSession()) {
            datas = session.selectList("com.example.springbatchexample.EmployeeRepository.selectEmployee");
        }
    }

    @Override
    public Employee read() {
        if (currentIndex < datas.size()) {
            return datas.get(currentIndex++);
        } else {
            return null; // 더 이상 데이터가 없을 때 null 반환
        }
    }
}
