package com.diso.koala;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.db.*;

import java.util.ArrayList;
import java.util.Date;

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

    void SetCustomer(Bundle bundle){
        customer = new Customer(bundle.getInt("id"), bundle.getString("name"));
        lblCustomer.setText(getString(R.string.text_customer) + " " + customer.getName());
    }

    void GetSalesByCustomer( ){
        ArrayList<QueryFilter> queryFilters = new ArrayList<QueryFilter>();
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setField( "id_customers" );
        queryFilter.setValue( Integer.toString(customer.getId()) );
        queryFilters.add( queryFilter );

        queryFilter = new QueryFilter();
        queryFilter.setField( "id_paymentTypes" );
        queryFilter.setValue( Integer.toString(GetIdPaymentTypes()) );
        queryFilters.add( queryFilter );

        saleHeaders = saleHeaderHelper.SelectWithDetails( queryFilters );
    }

    void SetSales(){
        if(saleAdapter == null){
            saleAdapter = new SaleAdapter(this, saleHeaders);
            ListView lstProducts = (ListView)findViewById(R.id.lstSaleList);
            lstProducts.setAdapter(saleAdapter);
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
        cmbPaymentType.setAdapter( Functions.GetPaymentTypes( this, paymentTypes ) );
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
        if(saleAdapter != null){
            saleAdapter.clear();
            saleAdapter.notifyDataSetChanged();
        }

        // Payment Type
        //cmbPaymentType.setSelection(0);

        // Total
        totalSale = 0;
        SetTotal();
    }

    //endregion

    void Events(){
        StartActivity((Button)findViewById(R.id.btnAddCustomer), CustomerListGUI.class);

        final ListView lstProducts = (ListView)findViewById(R.id.lstSaleList);
        lstProducts.setOnItemClickListener(
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
        return paymentTypes[ cmbPaymentType.getSelectedItemPosition() ].getId();
    }
}