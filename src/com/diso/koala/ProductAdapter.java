package com.diso.koala;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.diso.koala.db.Product;

public class ProductAdapter extends ArrayAdapter {
    Activity context;
    Product[] products;

    ProductAdapter(Activity context, Product[] products) {
        super(context, R.layout.list_item_product, products);
        this.context = context;
        this.products = products;
    }

    public View getView(int position, View contentView, ViewGroup parent){

        View item = contentView;
        ProductAdapterHolder productAdapterHolder;

        if (item == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            item = layoutInflater.inflate(R.layout.list_item_product, null);

            productAdapterHolder = new ProductAdapterHolder();
            productAdapterHolder.lblProductName = (TextView)item.findViewById(R.id.lblProductName);
            productAdapterHolder.lblProductPrice = (TextView)item.findViewById(R.id.lblProductPrice);

            item.setTag(productAdapterHolder);
        }
        else{
            productAdapterHolder = (ProductAdapterHolder)item.getTag();
        }

        productAdapterHolder.lblProductName.setText(products[position].getName());
        productAdapterHolder.lblProductPrice.setText(Integer.toString(products[position].getPrice()));
        return(item);
    }

    static class ProductAdapterHolder{
        TextView lblProductName;
        TextView lblProductPrice;
    }
}
