package com.diso.koala.guis;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.diso.koala.Functions;
import com.diso.koala.R;
import com.diso.koala.adapters.ProductAdapter;
import com.diso.koala.db.entities.PaymentType;
import com.diso.koala.db.entities.Product;
import com.diso.koala.db.entities.SaleDetail;
import com.diso.koala.db.entities.SaleHeader;
import com.diso.koala.db.helpers.PaymentTypeHelper;
import com.diso.koala.db.helpers.SaleHeaderHelper;

import java.util.ArrayList;

public class SaleViewGUI extends Activity {

    //region Var
    SaleHeader saleHeader;
    SaleHeaderHelper saleHeaderHelper;
    TextView lblCustomer, lblDate, lblTotal;
    ProductAdapter productAdapter;
    //endregion

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_view);
        GetFields();
        saleHeaderHelper = new SaleHeaderHelper(this);
        ShowSale();
    }

    void GetFields(){
        if(lblCustomer == null){
            lblCustomer = (TextView)findViewById(R.id.lblCustomer);
            lblDate = (TextView)findViewById(R.id.lblDate);
            lblTotal = (TextView)findViewById(R.id.lblTotal);
        }
    }

    void ShowSale(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            saleHeader = saleHeaderHelper.SelectByIdWithDetails(bundle.getInt("id"));
            SetCustomer();
            SetDate();
            SetTotal();
            SetProducts();
        }
    }

    // region Set
    void SetCustomer(){
        PaymentTypeHelper paymentTypeHelper = new PaymentTypeHelper(this);
        PaymentType paymentType = paymentTypeHelper.SelectById(saleHeader.getId_payment_type());

        if(paymentType != null){
            lblCustomer.setText(getString(R.string.text_customer) + " " + saleHeader.getCustomer_name() + " (" + paymentType.getDescription() + ")");
        }
        else{
            lblCustomer.setText(getString(R.string.text_customer) + " " + saleHeader.getCustomer_name());
        }
    }

    void SetDate(){
        lblDate.setText(getString(R.string.text_date_sale) + " " + Functions.GetDate(saleHeader.getDate_sale(), "yyyy-MM-dd"));
    }

    void SetTotal(){
        lblTotal.setText(getString(R.string.currency_symbol) + Functions.GetFloatValueWithTwoDecimals(saleHeader.getTotal()));
    }

    void SetProducts(){

        if(productAdapter == null){
            ArrayList<Product> products = new ArrayList<Product>();
            productAdapter = new ProductAdapter(this, products);
            ListView lstProducts = (ListView)findViewById(R.id.lstProductList);
            lstProducts.setAdapter(productAdapter);
        }

        for(SaleDetail saleDetail : saleHeader.getDetails()){
            Product product = new Product(saleDetail.getId_product(),saleDetail.getProduct_name());
            product.setPrice(saleDetail.getProduct_price());

            productAdapter.add(product);
            productAdapter.notifyDataSetChanged();
        }
    }
    //endregion
}