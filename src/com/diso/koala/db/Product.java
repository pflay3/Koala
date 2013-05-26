package com.diso.koala.db;

public class Product {
    private String name;
    private int price;
    private String barcode;
    private String description;

    public Product(String name){
        this.name = name;
    }

    //region Set-Methods
    public void setName(String name){
        this.name = name;
    }

    public void setPrice(int price){
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
    public String getName(){
        return name;
    }

    public int getPrice(){
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
