package com.diso.koala.db.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SaleHeader {
    private int id_sale_header;
    private int id_customer;
    private String customer_name;
    private float total;
    private int id_payment_type;
    private ArrayList<SaleDetail> details;
    private Date date_sale;

    public SaleHeader(int id, int id_customer){
        this.id_sale_header = id;
        this.id_customer = id_customer;
    }

    public int getId_sale_header() {
        return id_sale_header;
    }

    public void setId_sale_header(int id) {
        this.id_sale_header = id;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getId_payment_type() {
        return id_payment_type;
    }

    public void setId_payment_type(int id_payment_type) {
        this.id_payment_type = id_payment_type;
    }

    public Date getDate_sale() {
        return date_sale;
    }

    public void setDate_sale(Date date_sale) {
        this.date_sale = date_sale;
    }

    //region Details

    public void addDetail(SaleDetail saleDetail){
        if(details == null){ details = new ArrayList<SaleDetail>(); }
        details.add(saleDetail);
    }

    public void addDetails(SaleDetail[] saleDetails){
        if(details == null){ details = new ArrayList<SaleDetail>(); }
        details.addAll(Arrays.asList(saleDetails));
    }

    public void addDetails(ArrayList<SaleDetail> saleDetails){
        if(details == null){ details = new ArrayList<SaleDetail>(); }
        details.addAll(saleDetails);
    }

    public ArrayList<SaleDetail> getDetails(){
        return details;
    }

    //endregion
}
