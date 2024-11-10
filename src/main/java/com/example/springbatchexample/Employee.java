package com.example.springbatchexample;

import lombok.Data;

@Data
public class Employee {

    private Long employeeNumber;
    private String lastName;
    private String firstName;
    private String extension;
    private String email;
    private Long reportsTo;
    private String jobTitle;
}
