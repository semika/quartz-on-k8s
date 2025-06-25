package com.semika.learn.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employee {

    private String id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
}
