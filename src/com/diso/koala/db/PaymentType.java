package com.diso.koala.db;

public class PaymentType {
    private int id;
    private String description;

    public PaymentType(int id, String description){
        this.id = id;
        this.description = description;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
