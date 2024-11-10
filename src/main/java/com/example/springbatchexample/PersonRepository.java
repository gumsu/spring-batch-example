package com.example.springbatchexample;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface PersonRepository {

    void insertPerson(Person person);
}
