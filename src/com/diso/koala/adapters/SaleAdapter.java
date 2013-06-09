package com.diso.koala.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.diso.koala.Functions;
import com.diso.koala.R;
import com.diso.koala.db.entities.SaleHeader;
import com.diso.koala.interfaces.OnPaymentTypeChangeListener;

import java.util.ArrayList;

public class SaleAdapter extends ArrayAdapter<SaleHeader> {
    Activity context;
    private OnPaymentTypeChangeListener onPaymentTypeChangeListener;

    public SaleAdapter(Activity context, SaleHeader[] products) {
        super(context, R.layout.sale_list_item, products);
        this.context = context;
    }

    public SaleAdapter(Activity context, ArrayList<SaleHeader> products) {
        super(context, R.layout.sale_list_item, products);
        this.context = context;
    }

    public View getView(final int position, View contentView, ViewGroup parent){
        View item = contentView;
        SaleAdapterHolder saleAdapterHolder;

        if (item == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            item = layoutInflater.inflate(R.layout.sale_list_item, null);

            saleAdapterHolder = new SaleAdapterHolder();
            saleAdapterHolder.lblDate = (TextView)item.findViewById(R.id.lblDate);
            saleAdapterHolder.tbPaymentType = (ToggleButton)item.findViewById(R.id.tbPaymentType);
            saleAdapterHolder.lblSaleTotal = (TextView)item.findViewById(R.id.lblSaleTotal);

            item.setTag(saleAdapterHolder);
        }
        else{
            saleAdapterHolder = (SaleAdapterHolder)item.getTag();
        }

        SaleHeader saleHeader = (SaleHeader)getItem(position);
        saleAdapterHolder.lblDate.setText(Functions.GetDate(saleHeader.getDate_sale(), "yyyy-MM-dd"));
        saleAdapterHolder.lblSaleTotal.setText(Functions.GetFloatValueWithTwoDecimals(saleHeader.getTotal()));

        if (saleHeader.getId_paymentTypes() == 1){ saleAdapterHolder.tbPaymentType.setChecked(true); }
        else{ saleAdapterHolder.tbPaymentType.setChecked(false); }

        saleAdapterHolder.tbPaymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventPaymentType(position, ((ToggleButton)v).isChecked());
            }
        });

        return(item);
    }

    public void setOnPaymentTypeChangeListener(OnPaymentTypeChangeListener listener){
        onPaymentTypeChangeListener = listener;
    }

    void EventPaymentType(int position, boolean isChecked){
        onPaymentTypeChangeListener.OnPaymentTypeChange(position, isChecked);
    }

    static class SaleAdapterHolder{
        TextView lblDate;
        ToggleButton tbPaymentType;
        TextView lblSaleTotal;
    }
}

