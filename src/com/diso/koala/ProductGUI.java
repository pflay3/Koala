package com.diso.koala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.diso.koala.db.*;

public class ProductGUI extends Activity {

    //region Var
    ProductHelper productHelper;
    EditText txtProductName = null;
    EditText txtProductPrice = null;
    EditText txtProductBarCode = null;
    EditText txtProductDescription = null;
    boolean booEdit = false;
    Product product;
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        ValidateIfEdit();
        Events();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnAddProduct);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { AddEditProduct(); }
                }
        );
    }

    void AddEditProduct(){
        GetEditText();

        if (!txtProductName.getText().toString().trim().equals("")
                && !txtProductPrice.getText().toString().trim().equals("")){
            ChangeVisibilityErrorMessage(false);
            ValidateProductHelper();

            if(booEdit){
                productHelper.Update(txtProductName.getText().toString()
                                        , Integer.parseInt(txtProductPrice.getText().toString())
                                        , txtProductBarCode.getText().toString()
                                        , txtProductDescription.getText().toString()
                                        , product.getId());
            }
            else{
                productHelper.Insert(txtProductName.getText().toString()
                                        , Integer.parseInt(txtProductPrice.getText().toString())
                                        , txtProductBarCode.getText().toString()
                                        , txtProductDescription.getText().toString());
                ResetControls();
            }
        }
        else{ChangeVisibilityErrorMessage(true);}
    }

    void ChangeVisibilityErrorMessage(boolean val){
        final TextView lblErrorMessage = (TextView)findViewById(R.id.lblProductError);
        if(val){lblErrorMessage.setVisibility(View.VISIBLE);}
        else{lblErrorMessage.setVisibility(View.INVISIBLE);}
    }

    void ResetControls(){
        GetEditText();
        txtProductName.setText("");
        txtProductPrice.setText("");
        txtProductBarCode.setText("");
        txtProductDescription.setText("");
    }

    void GetEditText(){
        if(txtProductName == null){
            txtProductName = (EditText)findViewById(R.id.txtProductName);
            txtProductPrice = (EditText)findViewById(R.id.txtProductPrice);
            txtProductBarCode = (EditText)findViewById(R.id.txtProductBarCode);
            txtProductDescription = (EditText)findViewById(R.id.txtProductDescription);
        }
    }

    void ValidateIfEdit(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            booEdit = true;
            ValidateProductHelper();
            product = productHelper.SelectById(bundle.getInt("id"));
            SetInitialValues();
        }
        else{booEdit = false;}
    }

    void ValidateProductHelper(){
        if(productHelper == null){
            productHelper = new ProductHelper(this);
        }
    }

    void SetInitialValues(){
        GetEditText();
        txtProductName.setText(product.getName());
        txtProductPrice.setText(Integer.toString(product.getPrice()));
        txtProductBarCode.setText(product.getBarcode());
        txtProductDescription.setText(product.getDescription());
    }
}