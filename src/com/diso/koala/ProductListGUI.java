package com.diso.koala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.diso.koala.db.Customer;
import com.diso.koala.db.CustomerHelper;
import com.diso.koala.db.Product;
import com.diso.koala.db.ProductHelper;

public class ProductListGUI extends Activity {
    ProductHelper productHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        Events();
        LoadAllProducts();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnSearchProduct);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { SearchByName(); }
                }
        );
    }

    void LoadAllProducts(){
        ValidateProductHelper();
        Product[] products = productHelper.SelectAll();
        ShowProducts(products);
    }

    void SearchByName(){
        ValidateProductHelper();
        final EditText txtProduct = (EditText)findViewById(R.id.txtProduct);
        if (!txtProduct.getText().toString().trim().equals("")){
            Product[] products = productHelper.SelectByName(txtProduct.getText().toString());
            ShowProducts(products);
        }
        else{LoadAllProducts();}
    }

    void ShowProducts(Product[] products){
        ProductAdapter adapter = new ProductAdapter(this, products);
        final ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
        lstProducts.setAdapter(adapter);
    }

    void ValidateProductHelper(){
        if(productHelper == null){
            productHelper = new ProductHelper(this);
        }
    }
}