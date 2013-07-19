package com.diso.koala.guis;

import android.app.Activity;
import android.content.Intent;
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

public class SaleListGUI extends Activity {

    //region Var
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
    //endregion

    //region Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_list);
        GetFields();
        Events();
        paymentTypeHelper = new PaymentTypeHelper(this);
        LoadPaymentTypes();
        saleHeaderHelper = new SaleHeaderHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK){
            ShowSalesByCustomer(data.getExtras());
        }
    }
    //endregion

    //region GUI

    void ShowSalesByCustomer(Bundle bundle){
        ResetGUI();
        SetCustomer( bundle );
        GetSalesByCustomer();
        CalculateTotal();
        SetTotal();
        SetSales();
    }

    void ShowSalesByPaymentType(){
        if(customer != null){
            CleanSales();
            GetSalesByCustomer();
            CalculateTotal();
            SetTotal();
            SetSales();
        }
    }

    void SetCustomer(Bundle bundle){
        customer = new Customer(bundle.getInt("id"), bundle.getString("name"));
        lblCustomer.setText(getString(R.string.text_customer) + " " + customer.getName());
    }

    void GetSalesByCustomer( ){
        ArrayList<QueryFilter> queryFilters = new ArrayList<QueryFilter>();
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setField( "id_customer" );
        queryFilter.setValue( Integer.toString(customer.getId_customer()) );
        queryFilters.add( queryFilter );

        final int idPaymentType = GetIdPaymentTypes();
        if(idPaymentType != 0){
            queryFilter = new QueryFilter();
            queryFilter.setField( "id_payment_type" );
            queryFilter.setValue( Integer.toString(GetIdPaymentTypes()) );
            queryFilters.add( queryFilter );
        }

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

    void ResetGUI(){
        // Customer
        customer = null;
        lblCustomer.setText(getString(R.string.text_customer));

        // Sales
        CleanSales();

        // Payment Type
        //cmbPaymentType.setSelection(0);

        // Total
        totalSale = 0;
        SetTotal();
    }

    void CleanSales(){
        if(saleAdapter != null){
            saleAdapter.clear();
            saleAdapter.notifyDataSetChanged();
        }
    }

    //endregion

    void Events(){
        StartActivity((Button)findViewById(R.id.btnAddCustomer), CustomerListGUI.class);

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

                    }
                }
        );
    }

    void StartActivity(Button b, final Class<?> cls){
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
}