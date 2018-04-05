package com.mycompany.app.model;

public class RandomTestEntity {

    private String test_string;
    private int test_int;

    public RandomTestEntity(String test_string, int test_int){
        this.test_string = test_string;
        this.test_int = test_int;
    }

    public String getTest_string() {
        return test_string;
    }

    public void setTest_string(String test_string) {
        this.test_string = test_string;
    }

    public Integer getTest_int() {
        return test_int;
    }

    public void setCode(Integer test_int) {
        this.test_int = test_int;
    }
}
