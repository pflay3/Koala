package com.diso.koala.guis;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.Functions;
import com.diso.koala.R;
import com.diso.koala.adapters.SaleAdapter;
import com.diso.koala.db.*;
import com.diso.koala.db.entities.Customer;
import com.diso.koala.db.entities.PaymentType;
import com.diso.koala.db.entities.SaleHeader;
import com.diso.koala.db.helpers.PaymentTypeHelper;
import com.diso.koala.db.helpers.SaleHeaderHelper;
import com.diso.koala.interfaces.OnPaymentTypeChangeListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SaleListGUI extends Activity {

    //region Var
    ActionFilter actionFilter;
    TextView lblCustomer, lblTotal;
    Spinner cmbPaymentType;
    int positionProductForDelete = 0;
    PaymentTypeHelper paymentTypeHelper;
    SaleHeaderHelper saleHeaderHelper;

    float totalSale = 0;
    Customer customer;
    PaymentType[] paymentTypes;
    ArrayList<SaleHeader> saleHeaders;
    SaleAdapter saleAdapter;

    Date startDate, endDate;
    //endregion

    //region Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_list);
        SetInitialDates();
        GetFields();
        Events();
        paymentTypeHelper = new PaymentTypeHelper(this);
        LoadPaymentTypes();
        saleHeaderHelper = new SaleHeaderHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK){
            if(actionFilter == ActionFilter.CUSTOMER){
                ShowSalesByCustomer(data.getExtras());
            }
            else{
                Bundle bundle = data.getExtras();
                startDate = Functions.GetDate(bundle.getString("dateStart") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
                endDate = Functions.GetDate(bundle.getString("dateEnd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                ShowSalesByDateFilter();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    //endregion

    //region GUI

    void ShowSalesByCustomer(Bundle bundle){
        customer = new Customer(bundle.getInt("id"), bundle.getString("name"));
        SetCustomer( );
        CleanSales();
        GetSales();
        CalculateTotal();
        SetTotal();
        SetSales();
    }

    void ShowSalesByPaymentType(){
        SetCustomer( );
        CleanSales();
        GetSales();
        CalculateTotal();
        SetTotal();
        SetSales();
    }

    void ShowSalesByDateFilter(){
        SetCustomer( );
        CleanSales();
        GetSales();
        CalculateTotal();
        SetTotal();
        SetSales();
    }

    void SetCustomer(){
        if(customer == null){lblCustomer.setText(getString(R.string.text_customer));}
        else{lblCustomer.setText(getString(R.string.text_customer) + " " + customer.getName());}
    }

    void GetSales( ){
        ArrayList<QueryFilter> queryFilters = new ArrayList<QueryFilter>();
        QueryFilter queryFilter = GetFilterDate();
        queryFilters.add(queryFilter);

        queryFilter = GetFilterCustomer();
        if(queryFilter != null){ queryFilters.add( queryFilter ); }

        queryFilter = GetFilterPaymentType();
        if(queryFilter != null){ queryFilters.add( queryFilter ); }

        saleHeaders = saleHeaderHelper.SelectByFilter(queryFilters);
    }

    void SetSales(){
        if(saleAdapter == null){
            saleAdapter = new SaleAdapter(this, saleHeaders);
            ListView lstProducts = (ListView)findViewById(R.id.lstSaleList);
            lstProducts.setAdapter(saleAdapter);

            saleAdapter.setOnPaymentTypeChangeListener(new OnPaymentTypeChangeListener() {
                @Override
                public void OnPaymentTypeChange(int position, boolean isChecked) {
                    UpdatePaymentType(position, isChecked);
                }
            });
        }
        else if(saleAdapter.getCount() == 0){
            saleAdapter.addAll(saleHeaders);
        }

        saleAdapter.notifyDataSetChanged();
    }

    void CalculateTotal(){
        totalSale = 0;
        for (SaleHeader saleHeader: saleHeaders){
            totalSale += saleHeader.getTotal();
        }
    }

    void SetTotal(){
        lblTotal.setText(getString(R.string.currency_symbol) + Functions.GetFloatValueWithTwoDecimals(totalSale));
    }

    void LoadPaymentTypes(){
        paymentTypes = paymentTypeHelper.SelectAll();
        cmbPaymentType.setAdapter( Functions.GetPaymentTypesWithExtra(this, paymentTypes) );
    }

    void GetFields(){
        if(lblCustomer == null){
            lblCustomer = (TextView)findViewById(R.id.lblCustomer);
            lblTotal = (TextView)findViewById(R.id.lblTotal);
            cmbPaymentType = (Spinner)findViewById(R.id.cmbPaymentType);
        }
    }

    void CleanSales(){
        if(saleAdapter != null){
            saleAdapter.clear();
            saleAdapter.notifyDataSetChanged();
        }
    }

    //endregion

    //region Filters

    QueryFilter GetFilterCustomer(){
        if(customer != null){
            QueryFilter queryFilter = new QueryFilter();
            queryFilter.setField( "id_customer" );
            queryFilter.setValue( Integer.toString(customer.getId_customer()) );
            return queryFilter;
        }

        return null;
    }

    QueryFilter GetFilterPaymentType(){
        final int idPaymentType = GetIdPaymentTypes();
        if(idPaymentType != 0){
            QueryFilter queryFilter = new QueryFilter();
            queryFilter.setField( "id_payment_type" );
            queryFilter.setValue( Integer.toString(GetIdPaymentTypes()) );
            return queryFilter;
        }

        return null;
    }

    QueryFilter GetFilterDate(){
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setField( "date_sale" );
        queryFilter.setValue(Functions.GetDate(startDate, "yyyy-MM-dd HH:mm:ss"));
        queryFilter.setValue2(Functions.GetDate(endDate, "yyyy-MM-dd HH:mm:ss"));
        queryFilter.setFilterType( QueryFilter.FilterOption.BETWEEN );
        return queryFilter;
    }

    //endregion

    void Events(){
        StartActivity((Button)findViewById(R.id.btnAddCustomer), CustomerListGUI.class);

        final Button btnSelectDates = (Button)findViewById(R.id.btnSelectDates);
        btnSelectDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionFilter = ActionFilter.DATES;
                Intent intent = new Intent(SaleListGUI.this, DateFilterGUI.class);

                Bundle bundle = new Bundle();
                bundle.putString("dateStart", Functions.GetDate(startDate, "yyyy-MM-dd"));
                bundle.putString("dateEnd", Functions.GetDate(endDate, "yyyy-MM-dd"));
                intent.putExtras(bundle);

                startActivityForResult(intent, 0);
            }
        });

        final Spinner cmbPaymentType = (Spinner)findViewById(R.id.cmbPaymentType);
        cmbPaymentType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShowSalesByPaymentType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        final ListView lstSaleList = (ListView)findViewById(R.id.lstSaleList);
        lstSaleList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        StartActivitySaleDetails(position);
                    }
                }
        );
    }

    void StartActivity(Button b, final Class<?> cls){
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        actionFilter = ActionFilter.CUSTOMER;
                        Intent intent = new Intent(SaleListGUI.this, cls);

                        Bundle bundle = new Bundle();
                        bundle.putString("action", "getId");
                        intent.putExtras(bundle);

                        startActivityForResult(intent, 0);
                    }
                }
        );
    }

    int GetIdPaymentTypes(){
        if (cmbPaymentType.getSelectedItemPosition() == 0){ return 0; }
        return paymentTypes[ cmbPaymentType.getSelectedItemPosition() - 1 ].getId_payment_type();
    }

    void UpdatePaymentType(int position, boolean isChecked){
        SaleHeader saleHeader = saleHeaders.get(position);
        int newPaymentType = isChecked ? 1 : 2;

        if(saleHeader.getId_payment_type() != newPaymentType){
            saleHeader.setId_payment_type(newPaymentType);
            saleHeaderHelper.UpdatePaymentTypeById(saleHeader);
        }
    }

    void StartActivitySaleDetails(int position){
        Intent intent = new Intent(SaleListGUI.this, SaleViewGUI.class);

        Bundle bundle = new Bundle();
        bundle.putInt("id", saleAdapter.getItem(position).getId_sale_header());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    void SetInitialDates(){
        final Calendar calendar = Calendar.getInstance();
        String month = calendar.get(Calendar.MONTH) + 1 < 10 ? "0" + Integer.toString(calendar.get(Calendar.MONTH) + 1) : Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String day = calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) : Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        startDate = Functions.GetDate( String.format("%d-%s-%s 00:00:00", calendar.get(Calendar.YEAR), month, day), "yyyy-MM-dd HH:mm:ss" );
        endDate = Functions.GetDate( String.format("%d-%s-%s 23:59:59", calendar.get(Calendar.YEAR), month, day), "yyyy-MM-dd HH:mm:ss" );
    }

    enum ActionFilter{
        CUSTOMER, DATES
    }
}