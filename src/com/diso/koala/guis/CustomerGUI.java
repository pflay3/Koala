package com.diso.koala.guis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.diso.koala.R;
import com.diso.koala.db.entities.Customer;
import com.diso.koala.db.helpers.CustomerHelper;

public class CustomerGUI extends Activity {

    //region Var
    CustomerHelper customerHelper;
    boolean booEdit = false;
    boolean booReturn = false;
    Customer customer;
    EditText txtCustomer;
    EditText txtCustomerId;
    //@endregion

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer);
        ValidateAction();
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
        GetEditText();

        if (!txtCustomer.getText().toString().trim().equals("")){
            ChangeVisibilityErrorMessage(false);
            ValidateCustomerHelper();

            if ( customer == null ){ customer = new Customer(0, ""); }
            customer.setName(txtCustomer.getText().toString());
            customer.setIdentification(txtCustomerId.getText().toString());

            if(booEdit){ customerHelper.UpdateById(customer); }
            else{
                customer.setId( customerHelper.Insert(customer) );
                txtCustomer.setText("");
                txtCustomerId.setText("");

                if(booReturn){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", customer.getId());
                    bundle.putString("name", customer.getName());
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        }
        else{ChangeVisibilityErrorMessage(true);}
    }

    void ChangeVisibilityErrorMessage(boolean val){
        final TextView lblErrorMessage = (TextView)findViewById(R.id.lblCustomerError);
        if(val){lblErrorMessage.setVisibility(View.VISIBLE);}
        else{lblErrorMessage.setVisibility(View.INVISIBLE);}
    }

    void ValidateCustomerHelper(){
        if(customerHelper == null){
            customerHelper = new CustomerHelper(this);
        }
    }

    void SetInitialValues(){
        GetEditText();
        txtCustomer.setText(customer.getName());
        txtCustomerId.setText(customer.getIdentification());
    }

    void GetEditText(){
        if(txtCustomer == null){
            txtCustomer = (EditText)findViewById(R.id.txtCustomer);
            txtCustomerId = (EditText)findViewById(R.id.txtCustomerId);
        }
    }

    void ValidateAction(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("action").equals("edit")){
                booEdit = true;
                booReturn = false;

                ValidateCustomerHelper();
                customer = customerHelper.SelectById(bundle.getInt("id"));
                SetInitialValues();
            }
            else if(bundle.getString("action").equals("getId"))
            {
                booEdit = false;
                booReturn = true;
            }
        }
        else{
            booEdit = false;
            booReturn = false;
        }
    }
}