package com.diso.koala.db.entities;

public class Product {

    private int id;
    private String name;
    private float price;
    private String barcode;
    private String description;

    public Product(int id, String name){
        this.id = id;
        this.name = name;
    }

    //region Set-Methods
    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPrice(float price){
        this.price = price;
    }

    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public void setDescription(String description){
        this.description = description;
    }
    //endregion

    //region Get-Methods
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public float getPrice(){
        return price;
    }

    public String getBarcode(){
        return barcode;
    }

    public String getDescription(){
        return description;
    }
    //endregion
}