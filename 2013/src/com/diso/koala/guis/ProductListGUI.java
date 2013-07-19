package com.diso.koala.guis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.diso.koala.adapters.ProductAdapter;
import com.diso.koala.R;
import com.diso.koala.db.entities.Product;
import com.diso.koala.db.helpers.ProductHelper;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            Product product = new Product(bundle.getInt("id"),bundle.getString("name"));
            product.setPrice(bundle.getFloat("price"));
            if(!booEdit){GetProduct(product);}
        }
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

        final Button bAdd = (Button)findViewById(R.id.btnAddProduct);
        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListGUI.this, ProductGUI.class);
                if(booEdit){ startActivity(intent); }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("action", "getId");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
            }
        });

        final ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
        lstProducts.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (booEdit){ EditProduct(position); }
                        else{ GetProduct(products[position]); }
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
        bundle.putInt("id", products[position].getId_product());
        bundle.putString("action", "edit");
        intent.putExtras(bundle);

        startActivity(intent);
    }

    void GetProduct(Product product){
        Intent intent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putInt("id", product.getId_product());
        bundle.putString("name", product.getName());
        bundle.putFloat("price", product.getPrice());
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