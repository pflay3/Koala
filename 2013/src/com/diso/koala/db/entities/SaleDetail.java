package com.diso.koala.db.entities;

public class SaleDetail {
    private int id_sale_detail;
    private int id_sale_header;
    private int id_product;
    private String product_name;
    private float product_price;

    public SaleDetail(int id, int id_sale_header){
        this.id_sale_detail = id;
        this.id_sale_header = id_sale_header;
    }

    public int getId_sale_detail() {
        return id_sale_detail;
    }

    public void setId_sale_detail(int id) {
        this.id_sale_detail = id;
    }

    public int getId_sale_header() {
        return id_sale_header;
    }

    public void setId_sale_header(int id_sale_header) {
        this.id_sale_header = id_sale_header;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float price) {
        this.product_price = price;
    }
}
