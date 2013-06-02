package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaleHeaderHelper {

    KoalaDataBase kdb;
    Context context;
    SaleDetailHelper saleDetailHelper;

    public SaleHeaderHelper(Context context) {
        this.context = context;
        kdb = new KoalaDataBase(context);
        saleDetailHelper = new SaleDetailHelper(context);
    }

    //region SQL-Methods
    private SaleHeader[] Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        SaleHeader[] saleHeaders = new SaleHeader[c.getCount()];
        int pos = 0;

        if (c.moveToFirst()) {
            do {
                saleHeaders[pos] = new SaleHeader(c.getInt(0), c.getInt(1));
                saleHeaders[pos].setCustomer_name(c.getString(2));
                saleHeaders[pos].setTotal(c.getFloat(3));
                saleHeaders[pos].setId_paymentTypes(c.getInt(4));
                saleHeaders[pos].setDate_sale( GetDate(c.getString(5) ));
                pos++;
            } while(c.moveToNext());
        }

        return saleHeaders;
    }

    public SaleHeader[] SelectAll(){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders";
        return Select(sql);
    }

    public SaleHeader SelectById(int id){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders WHERE id = %d";
        SaleHeader[] saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.length > 0){ return saleHeaders[0]; }
        else{return null;}
    }

    public SaleHeader SelectByIdWithDetails(int id){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders WHERE id = %d";
        SaleHeader[] saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.length > 0){
            SaleDetail[] saleDetails = saleDetailHelper.SelectByHeaderId(saleHeaders[0].getId());
            saleHeaders[0].addDetails(saleDetails);
            return saleHeaders[0];
        }
        else{return null;}
    }

    private void ExecuteNonQuery(String sqlNonQuery){
        SQLiteDatabase db = kdb.getWritableDatabase();
        db.execSQL(sqlNonQuery);
    }

    public int Insert(SaleHeader saleHeader){
        String sql = "INSERT INTO SalesHeaders VALUES (NULL, %d, '%s', %f, %d, '%s')";
        ExecuteNonQuery(String.format(sql, saleHeader.getId_customers(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_paymentTypes(), GetDate(saleHeader.getDate_sale())));
        return kdb.GetLastId("SalesHeaders");
    }

    public int InsertWithDetails(SaleHeader saleHeader){
        saleHeader.setId(this.Insert(saleHeader));
        for ( SaleDetail saleDetail: saleHeader.getDetails() ){
            saleDetail.setId_salesHeaders(saleHeader.getId());
            saleDetailHelper.Insert( saleDetail );
        }

        return saleHeader.getId();
    }

    public void Update(SaleHeader saleHeader){
        String sql = "UPDATE SalesHeaders SET id_customers = %d, customer_name = '%s', total = %f, id_paymentTypes = %d, date_sale = '%s' WHERE id = %d";
        ExecuteNonQuery(String.format(sql ,saleHeader.getId_customers(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_paymentTypes(), GetDate(saleHeader.getDate_sale()), saleHeader.getId()));
    }
    //endregion

    String GetDate(Date date){
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        return ft.format( date );
    }

    Date GetDate(String date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        try {
            return ft.parse( date );
        } catch (ParseException e) {
            return new Date();
        }
    }
}
