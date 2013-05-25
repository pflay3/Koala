package com.diso.koala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.diso.koala.db.*;

public class CustomerListGUI extends Activity {
    CustomerHelper customerHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);
        Events();
        LoadAllCustomers();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnSearchCustomer);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { SearchByName(); }
                }
        );
    }

    void LoadAllCustomers(){
        ValidateCustomerHelper();
        Customer[] customers = customerHelper.SelectAll();
        ShowCustomers(customers);
    }

    void SearchByName(){
        ValidateCustomerHelper();
        final EditText txtCustomer = (EditText)findViewById(R.id.txtCustomer);
        if (!txtCustomer.getText().toString().trim().equals("")){
            Customer[] customers = customerHelper.SelectByName(txtCustomer.getText().toString());
            ShowCustomers(customers);
        }
        else{LoadAllCustomers();}
    }

    void ShowCustomers(Customer[] customers){
        final String[] customersAdapter = new String[customers.length];

        for (int i = 0; i < customers.length; i++){
            customersAdapter[i] = customers[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, customersAdapter);
        final ListView lstCustomers = (ListView)findViewById(R.id.lstCustomers);
        lstCustomers.setAdapter(adapter);
    }

    void ValidateCustomerHelper(){
        if(customerHelper == null){
            customerHelper = new CustomerHelper(this, getString(R.string.db_name), null, R.integer.db_version);
        }
    }
}