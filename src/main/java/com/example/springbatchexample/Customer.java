package com.example.springbatchexample;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Customer {

    private Long customerNumber;
    private String customerName;
    private String contactLastName;
    private String contactFirstName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Long salesRepEmployeeNumber;
    private BigDecimal creditLimit;
}
