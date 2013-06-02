package com.diso.koala;

import android.app.Activity;
import android.content.Intent;
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
    boolean booReturn = false;
    Product product;
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        ValidateAction();
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

            if ( product == null ){ product = new Product(0, ""); }
            product.setName(txtProductName.getText().toString());
            product.setPrice(Float.parseFloat(txtProductPrice.getText().toString()));
            product.setBarcode(txtProductBarCode.getText().toString());
            product.setDescription(txtProductDescription.getText().toString());

            if(booEdit){ productHelper.Update(product); }
            else{
                product.setId( productHelper.Insert(product) );
                ResetControls();

                if(booReturn){
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", product.getId());
                    bundle.putString("name", product.getName());
                    bundle.putFloat("price", product.getPrice());
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
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

    void ValidateProductHelper(){
        if(productHelper == null){
            productHelper = new ProductHelper(this);
        }
    }

    void SetInitialValues(){
        GetEditText();
        txtProductName.setText(product.getName());
        txtProductPrice.setText(Functions.GetFloatValue(product.getPrice()));
        txtProductBarCode.setText(product.getBarcode());
        txtProductDescription.setText(product.getDescription());
    }

    void ValidateAction(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            if(bundle.getString("action").equals("edit")){
                booEdit = true;
                booReturn = false;

                ValidateProductHelper();
                product = productHelper.SelectById(bundle.getInt("id"));
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