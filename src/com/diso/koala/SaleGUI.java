package com.diso.koala;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.db.Customer;
import com.diso.koala.db.Product;

import java.util.ArrayList;

public class SaleGUI extends Activity {

    //region Var
    AddAction addAction;
    int totalSale = 0;
    TextView lblCustomer, lblTotal;
    Customer customer;
    ProductAdapter adapter;
    int position = 0;
    //endregion

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale);
        GetFields();
        Events();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK){
            if(addAction == AddAction.CUSTOMER){SetCustomer(data.getExtras());}
            else if(addAction == AddAction.PRODUCT){SetProduct(data.getExtras());}
        }
    }

    void Events(){
        StartActivity((Button)findViewById(R.id.btnAddCustomer), CustomerListGUI.class);
        StartActivity((Button)findViewById(R.id.btnAddProduct), ProductListGUI.class);

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
        totalSale += bundle.getInt("price");
        lblTotal.setText(getString(R.string.currency_symbol) + Integer.toString(totalSale));

        Product product = new Product(bundle.getInt("id"),bundle.getString("name"));
        product.setPrice(bundle.getInt("price"));

        if(adapter==null){
            ArrayList<Product> products = new ArrayList<Product>();
            adapter = new ProductAdapter(this, products);
            ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
            lstProducts.setAdapter(adapter);
        }

        adapter.add(product);
        adapter.notifyDataSetChanged();
    }

    void DeleteProduct(){
        Product product = adapter.getItem(position);
        totalSale -= product.getPrice();
        lblTotal.setText(getString(R.string.currency_symbol) + Integer.toString(totalSale));

        adapter.remove(product);
        adapter.notifyDataSetChanged();
    }

    void AskForDeleteProduct(int position){
        this.position = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.product_delete_title))
                .setMessage(getString(R.string.product_delete_message))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getString(R.string.product_delete_yes), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which){DeleteProduct();}
                });
        builder.setNegativeButton(getString(R.string.product_delete_no), new DialogInterface.OnClickListener()
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
        }
    }

    enum AddAction{
        CUSTOMER, PRODUCT
    }
}