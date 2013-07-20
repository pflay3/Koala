package com.diso.koala.guis;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.Functions;
import com.diso.koala.adapters.ProductAdapter;
import com.diso.koala.R;
import com.diso.koala.db.entities.*;
import com.diso.koala.db.helpers.PaymentTypeHelper;
import com.diso.koala.db.helpers.SaleHeaderHelper;

import java.util.ArrayList;
import java.util.Date;

public class SaleGUI extends Activity {

    //region Var
    AddAction addAction;
    TextView lblCustomer, lblTotal;
    Spinner cmbPaymentType;
    int positionProductForDelete = 0;
    PaymentTypeHelper paymentTypeHelper;
    SaleHeaderHelper saleHeaderHelper;

    float totalSale = 0;
    Customer customer;
    ProductAdapter productAdapter;
    PaymentType[] paymentTypes;
    //endregion

    //region Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale);
        GetFields();
        Events();
        paymentTypeHelper = new PaymentTypeHelper(this);
        LoadPaymentTypes();
        saleHeaderHelper = new SaleHeaderHelper(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK){
            if(addAction == AddAction.CUSTOMER){SetCustomer(data.getExtras());}
            else if(addAction == AddAction.PRODUCT){SetProduct(data.getExtras());}
        }
    }
    //endregion

    void Events(){
        StartActivity((Button)findViewById(R.id.btnAddCustomer), CustomerListGUI.class);
        StartActivity((Button)findViewById(R.id.btnAddProduct), ProductListGUI.class);

        final Button btnSaveSale = (Button)findViewById(R.id.btnSaveSale);
        btnSaveSale.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSale();
            }
        });

        final ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
        lstProducts.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AskForDeleteProduct(position);
                    }
                }
        );
    }

    void StartActivity(Button b, final Class<?> cls){
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((Button)v).getText().equals(getString(R.string.add_customer))){addAction = AddAction.CUSTOMER;}
                        else if (((Button)v).getText().equals(getString(R.string.add_product))){addAction = AddAction.PRODUCT;}

                        Intent intent = new Intent(SaleGUI.this, cls);

                        Bundle bundle = new Bundle();
                        bundle.putString("action", "getId");
                        intent.putExtras(bundle);

                        startActivityForResult(intent, 0);
                    }
                }
        );
    }

    void SetCustomer(Bundle bundle){
        customer = new Customer(bundle.getInt("id"), bundle.getString("name"));
        lblCustomer.setText(getString(R.string.text_customer) + " " + customer.getName());
    }

    void SetProduct(Bundle bundle){
        totalSale += bundle.getFloat("price");
        lblTotal.setText(getString(R.string.currency_symbol) + Functions.GetFloatValueWithTwoDecimals(totalSale));

        Product product = new Product(bundle.getInt("id"),bundle.getString("name"));
        product.setPrice(bundle.getFloat("price"));

        if(productAdapter == null){
            ArrayList<Product> products = new ArrayList<Product>();
            productAdapter = new ProductAdapter(this, products);
            ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
            lstProducts.setAdapter(productAdapter);
        }

        productAdapter.add(product);
        productAdapter.notifyDataSetChanged();
    }

    void DeleteProduct(){
        Product product = productAdapter.getItem(positionProductForDelete);
        totalSale -= product.getPrice();
        lblTotal.setText(getString(R.string.currency_symbol) + Functions.GetFloatValueWithTwoDecimals(totalSale));

        productAdapter.remove(product);
        productAdapter.notifyDataSetChanged();
    }

    void AskForDeleteProduct(int position){
        this.positionProductForDelete = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.product_delete_title))
                .setMessage(getString(R.string.product_delete_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which){DeleteProduct();}
                });
        builder.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which){dialog.dismiss();}
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void GetFields(){
        if(lblCustomer == null){
            lblCustomer = (TextView)findViewById(R.id.lblCustomer);
            lblTotal = (TextView)findViewById(R.id.lblTotal);
            cmbPaymentType = (Spinner)findViewById(R.id.cmbPaymentType);
        }
    }

    void LoadPaymentTypes(){
        paymentTypes = paymentTypeHelper.SelectAll();
        cmbPaymentType.setAdapter( Functions.GetPaymentTypes( this, paymentTypes ) );
    }

    int GetIdPaymentTypes(){
        return paymentTypes[ cmbPaymentType.getSelectedItemPosition() ].getId_payment_type();
    }

    void SaveSale(){

        if ( !ValidateSaveSale() ){
            ShowErrorSaveSale();
            return;
        }

        // Header
        SaleHeader saleHeader = new SaleHeader(0, customer.getId_customer());
        saleHeader.setCustomer_name(customer.getName());
        saleHeader.setTotal(totalSale);
        saleHeader.setId_payment_type(GetIdPaymentTypes());
        saleHeader.setDate_sale( new Date());

        //Details
        for( int i = 0; i < productAdapter.getCount(); i++ ){
            Product product = productAdapter.getItem(i);

            SaleDetail saleDetail = new SaleDetail(0, 0);
            saleDetail.setId_product(product.getId_product());
            saleDetail.setProduct_name( product.getName() );
            saleDetail.setProduct_price( product.getPrice() );
            saleHeader.addDetail( saleDetail );
        }

        saleHeaderHelper.InsertWithDetails( saleHeader );
        ResetGUI();
    }

    boolean ValidateSaveSale(){
        if ( customer == null ){ return false; }
        if ( productAdapter == null ){ return false; }
        if ( productAdapter.getCount() == 0 ){ return false; }

        return  true;
    }

    void ShowErrorSaveSale(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.save_sale_title))
                .setMessage(getString(R.string.save_sale_error_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.save_sale_ok), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which){dialog.dismiss();}
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    void ResetGUI(){
        // Customer
        customer = null;
        lblCustomer.setText(getString(R.string.text_customer));

        // Products
        productAdapter.clear();
        productAdapter.notifyDataSetChanged();

        // Payment Type
        cmbPaymentType.setSelection(0);

        // Total
        totalSale = 0;
        lblTotal.setText(getString(R.string.currency_symbol) + Functions.GetFloatValueWithTwoDecimals(totalSale));
    }

    enum AddAction{
        CUSTOMER, PRODUCT
    }
}