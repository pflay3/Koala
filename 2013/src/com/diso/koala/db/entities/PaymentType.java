package com.diso.koala.db.entities;

public class PaymentType {
    private int id_payment_type;
    private String description;

    public PaymentType(int id, String description){
        this.id_payment_type = id;
        this.description = description;
    }

    public void setId_payment_type(int id){
        this.id_payment_type = id;
    }

    public int getId_payment_type(){
        return id_payment_type;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
