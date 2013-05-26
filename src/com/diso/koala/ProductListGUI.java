package com.diso.koala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.db.Customer;
import com.diso.koala.db.CustomerHelper;
import com.diso.koala.db.Product;
import com.diso.koala.db.ProductHelper;

public class ProductListGUI extends Activity {
    ProductHelper productHelper;
    Product[] products;
    boolean booEdit = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        ValidateAction();
        Events();
        LoadAllProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchByName();
    }

    void Events(){
        final Button b = (Button)findViewById(R.id.btnSearchProduct);

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { SearchByName(); }
                }
        );

        final ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
        lstProducts.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (booEdit){ EditProduct(position); }
                        else{ GetProduct(position); }
                    }
                }
        );
    }

    void LoadAllProducts(){
        ValidateProductHelper();
        products = productHelper.SelectAll();
        ShowProducts();
    }

    void SearchByName(){
        ValidateProductHelper();
        final EditText txtProduct = (EditText)findViewById(R.id.txtProduct);
        if (!txtProduct.getText().toString().trim().equals("")){
            products = productHelper.SelectByName(txtProduct.getText().toString());
            ShowProducts();
        }
        else{LoadAllProducts();}
    }

    void ShowProducts(){
        ProductAdapter adapter = new ProductAdapter(this, products);
        final ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
        lstProducts.setAdapter(adapter);
    }

    void ValidateProductHelper(){
        if(productHelper == null){
            productHelper = new ProductHelper(this);
        }
    }

    void EditProduct(int position){
        Intent intent = new Intent(ProductListGUI.this, ProductGUI.class);

        Bundle bundle = new Bundle();
        bundle.putInt("id", products[position].getId());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    void GetProduct(int position){
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putInt("id", products[position].getId());
        bundle.putString("name", products[position].getName());
        bundle.putInt("price", products[position].getPrice());
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