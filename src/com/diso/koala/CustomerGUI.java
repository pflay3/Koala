package com.diso.koala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.diso.koala.db.CustomerHelper;

public class CustomerGUI extends Activity {
    CustomerHelper customerHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        Events();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnAddCustomer);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { AddCustomer(); }
                }
        );
    }

    void AddCustomer(){
        final EditText txtCustomer = (EditText)findViewById(R.id.txtCustomer);

        if (!txtCustomer.getText().toString().trim().equals("")){
            ChangeVisibilityErrorMessage(false);
            if(customerHelper == null){
                customerHelper = new CustomerHelper(this, getString(R.string.db_name), null, R.integer.db_version);
            }

            customerHelper.Insert(txtCustomer.getText().toString());
            txtCustomer.setText("");
        }
        else{ChangeVisibilityErrorMessage(true);}
    }

    void ChangeVisibilityErrorMessage(boolean val){
        final TextView lblErrorMessage = (TextView)findViewById(R.id.lblCustomerError);
        if(val){lblErrorMessage.setVisibility(View.VISIBLE);}
        else{lblErrorMessage.setVisibility(View.INVISIBLE);}
    }
}