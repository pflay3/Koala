package com.diso.koala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.diso.koala.db.CustomerHelper;
import com.diso.koala.db.ProductHelper;

public class ProductGUI extends Activity {
    ProductHelper productHelper;
    EditText txtProductName = null;
    EditText txtProductPrice = null;
    EditText txtProductBarCode = null;
    EditText txtProductDescription = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        Events();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnAddProduct);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { AddProduct(); }
                }
        );
    }

    void AddProduct(){
        if(txtProductName == null){GetEditText();}

        if (!txtProductName.getText().toString().trim().equals("")
                && !txtProductPrice.getText().toString().trim().equals("")){
            ChangeVisibilityErrorMessage(false);
            if(productHelper == null){
                productHelper = new ProductHelper(this, getString(R.string.db_name));
            }

            productHelper.Insert(txtProductName.getText().toString()
                                    , Integer.parseInt(txtProductPrice.getText().toString())
                                    , txtProductBarCode.getText().toString()
                                    , txtProductDescription.getText().toString());
            ResetControls();
        }
        else{ChangeVisibilityErrorMessage(true);}
    }

    void ChangeVisibilityErrorMessage(boolean val){
        final TextView lblErrorMessage = (TextView)findViewById(R.id.lblProductError);
        if(val){lblErrorMessage.setVisibility(View.VISIBLE);}
        else{lblErrorMessage.setVisibility(View.INVISIBLE);}
    }

    void ResetControls(){
        if(txtProductName == null){GetEditText();}
        txtProductName.setText("");
        txtProductPrice.setText("");
        txtProductBarCode.setText("");
        txtProductDescription.setText("");
    }

    void GetEditText(){
        txtProductName = (EditText)findViewById(R.id.txtProductName);
        txtProductPrice = (EditText)findViewById(R.id.txtProductPrice);
        txtProductBarCode = (EditText)findViewById(R.id.txtProductBarCode);
        txtProductDescription = (EditText)findViewById(R.id.txtProductDescription);
    }
}