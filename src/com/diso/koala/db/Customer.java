package com.diso.koala.db;

public class Customer {
    private String name;

    public Customer(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
