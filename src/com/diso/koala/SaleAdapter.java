package com.diso.koala;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.diso.koala.db.Product;
import com.diso.koala.db.SaleHeader;

import java.util.ArrayList;

public class SaleAdapter extends ArrayAdapter<SaleHeader> {
    Activity context;

    SaleAdapter(Activity context, SaleHeader[] products) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
    }

    SaleAdapter(Activity context, ArrayList<SaleHeader> products) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
    }

    public View getView(int position, View contentView, ViewGroup parent){
        View item = contentView;
        SaleAdapterHolder saleAdapterHolder;

        if (item == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            item = layoutInflater.inflate(R.layout.sale_list_item, null);

            saleAdapterHolder = new SaleAdapterHolder();
            saleAdapterHolder.lblCustomerName = (TextView)item.findViewById(R.id.lblCustomerName);
            saleAdapterHolder.lblSaleTotal = (TextView)item.findViewById(R.id.lblSaleTotal);

            item.setTag(saleAdapterHolder);
        }
        else{
            saleAdapterHolder = (SaleAdapterHolder)item.getTag();
        }

        saleAdapterHolder.lblCustomerName.setText(((SaleHeader)getItem(position)).getCustomer_name());
        saleAdapterHolder.lblSaleTotal.setText(Functions.GetFloatValueWithTwoDecimals(((SaleHeader)getItem(position)).getTotal()));
        return(item);
    }

    static class SaleAdapterHolder{
        TextView lblCustomerName;
        TextView lblSaleTotal;
    }
}
