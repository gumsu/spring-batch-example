package com.example.springbatchexample;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CustomerRepository {
    List<Customer> selectCustomer();
}
