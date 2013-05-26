package com.diso.koala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.diso.koala.db.*;

public class CustomerGUI extends Activity {
    CustomerHelper customerHelper;
    KoalaDataBase koalaDataBase;
    boolean booEdit = false;
    Customer customer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        ValidateIfEdit();
        Events();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnAddCustomer);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { AddEditCustomer(); }
                }
        );
    }

    void AddEditCustomer(){
        final EditText txtCustomer = (EditText)findViewById(R.id.txtCustomer);

        if (!txtCustomer.getText().toString().trim().equals("")){
            ChangeVisibilityErrorMessage(false);
            ValidateCustomerHelper();

            if(booEdit){ customerHelper.Update(txtCustomer.getText().toString(), customer.getId()); }
            else{
                customerHelper.Insert(txtCustomer.getText().toString());
                txtCustomer.setText("");
            }
        }
        else{ChangeVisibilityErrorMessage(true);}
    }

    void ChangeVisibilityErrorMessage(boolean val){
        final TextView lblErrorMessage = (TextView)findViewById(R.id.lblCustomerError);
        if(val){lblErrorMessage.setVisibility(View.VISIBLE);}
        else{lblErrorMessage.setVisibility(View.INVISIBLE);}
    }

    void ValidateIfEdit(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            booEdit = true;
            ValidateCustomerHelper();
            customer = customerHelper.SelectById(bundle.getInt("id"));
            SetInitialValues();
        }
        else{booEdit = false;}
    }

    void ValidateCustomerHelper(){
        if(customerHelper == null){
            customerHelper = new CustomerHelper(this);
        }
    }

    void SetInitialValues(){
        final EditText txtCustomer = (EditText)findViewById(R.id.txtCustomer);
        txtCustomer.setText(customer.getName());
    }
}