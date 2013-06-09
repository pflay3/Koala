package com.diso.koala.db.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class SaleHeader {
    private int id;
    private int id_customers;
    private String customer_name;
    private float total;
    private int id_paymentTypes;
    private ArrayList<SaleDetail> details;
    private Date date_sale;

    public SaleHeader(int id, int id_customers){
        this.id = id;
        this.id_customers = id_customers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_customers() {
        return id_customers;
    }

    public void setId_customers(int id_customers) {
        this.id_customers = id_customers;
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

    public int getId_paymentTypes() {
        return id_paymentTypes;
    }

    public void setId_paymentTypes(int id_paymentTypes) {
        this.id_paymentTypes = id_paymentTypes;
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
