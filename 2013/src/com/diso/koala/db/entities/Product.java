package com.diso.koala.db.entities;

public class Product {

    private int id_product;
    private String name;
    private float price;
    private String barcode;
    private String description;

    public Product(int id, String name){
        this.id_product = id;
        this.name = name;
    }

    //region Set-Methods
    public void setId_product(int id){
        this.id_product = id;
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
    public int getId_product(){
        return id_product;
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
