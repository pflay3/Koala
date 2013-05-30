package com.diso.koala.db;

public class SaleDetail {
    private int id;
    private int id_salesHeaders;
    private int id_products;
    private String product_name;
    private int product_price;

    public SaleDetail(int id, int id_salesHeaders){
        this.id = id;
        this.id_salesHeaders = id_salesHeaders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_salesHeaders() {
        return id_salesHeaders;
    }

    public void setId_salesHeaders(int id_salesHeaders) {
        this.id_salesHeaders = id_salesHeaders;
    }

    public int getId_products() {
        return id_products;
    }

    public void setId_products(int id_products) {
        this.id_products = id_products;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int price) {
        this.product_price = price;
    }
}
