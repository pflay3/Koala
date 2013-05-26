package com.diso.koala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.db.*;

public class CustomerListGUI extends Activity {
    CustomerHelper customerHelper;
    Customer[] customers;
    boolean booEdit = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_list);
        ValidateAction();
        Events();
        LoadAllCustomers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchByName();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnSearchCustomer);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { SearchByName(); }
                }
        );

        final ListView lstCustomers = (ListView)findViewById(R.id.lstCustomers);
        lstCustomers.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (booEdit){ EditCustomer(position); }
                        else{ GetCustomer(position); }
                    }
                }
        );
    }

    void LoadAllCustomers(){
        ValidateCustomerHelper();
        customers = customerHelper.SelectAll();
        ShowCustomers();
    }

    void SearchByName(){
        ValidateCustomerHelper();
        final EditText txtCustomer = (EditText)findViewById(R.id.txtCustomer);
        if (!txtCustomer.getText().toString().trim().equals("")){
            customers = customerHelper.SelectByName(txtCustomer.getText().toString());
            ShowCustomers();
        }
        else{LoadAllCustomers();}
    }

    void ShowCustomers(){
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
            customerHelper = new CustomerHelper(this);
        }
    }

    void EditCustomer(int position){
        Intent intent = new Intent(CustomerListGUI.this, CustomerGUI.class);

        Bundle bundle = new Bundle();
        bundle.putInt("id", customers[position].getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    void GetCustomer(int position){
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putInt("id", customers[position].getId());
        bundle.putString("name", customers[position].getName());
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    void ValidateAction(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("action").equals("getId")){booEdit = false;}
        }
        else{booEdit = true;}
    }
}