package com.diso.koala.db.entities;

public class Customer {

    private int id_customer;
    private String name;
    private String identification;

    public Customer(int id, String name){
        this.id_customer = id;
        this.name = name;
    }

    public void setId_customer(int id){
        this.id_customer = id;
    }

    public int getId_customer(){
        return id_customer;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
