package com.diso.koala.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.diso.koala.Functions;
import com.diso.koala.R;
import com.diso.koala.db.entities.Product;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context;

    public ProductAdapter(Activity context, Product[] products) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
    }

    public ProductAdapter(Activity context, ArrayList<Product> products) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
    }

    public View getView(int position, View contentView, ViewGroup parent){
        View item = contentView;
        ProductAdapterHolder productAdapterHolder;

        if (item == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            item = layoutInflater.inflate(R.layout.product_list_item, null);

            productAdapterHolder = new ProductAdapterHolder();
            productAdapterHolder.lblProductName = (TextView)item.findViewById(R.id.lblProductName);
            productAdapterHolder.lblProductPrice = (TextView)item.findViewById(R.id.lblProductPrice);

            item.setTag(productAdapterHolder);
        }
        else{
            productAdapterHolder = (ProductAdapterHolder)item.getTag();
        }

        productAdapterHolder.lblProductName.setText(((Product)getItem(position)).getName());
        productAdapterHolder.lblProductPrice.setText(Functions.GetFloatValueWithTwoDecimals(((Product) getItem(position)).getPrice()));
        return(item);
    }

    static class ProductAdapterHolder{
        TextView lblProductName;
        TextView lblProductPrice;
    }
}
