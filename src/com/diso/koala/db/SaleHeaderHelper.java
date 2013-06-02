package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SaleHeaderHelper {

    KoalaDataBase kdb;
    Context context;

    public SaleHeaderHelper(Context context) {
        this.context = context;
        kdb = new KoalaDataBase(context);
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
                pos++;
            } while(c.moveToNext());
        }

        return saleHeaders;
    }

    public SaleHeader[] SelectAll(){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes FROM SalesHeaders";
        return Select(sql);
    }

    public SaleHeader SelectById(int id){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes FROM SalesHeaders WHERE id = %d";
        SaleHeader[] saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.length > 0){ return saleHeaders[0]; }
        else{return null;}
    }

    public SaleHeader SelectByIdWithDetails(int id){
        String sql = "SELECT id, id_salesHeaders, id_products, product_name, product_price FROM SalesHeaders WHERE id_salesHeaders = %d";
        SaleHeader[] saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.length > 0){
            SaleDetailHelper saleDetailHelper = new SaleDetailHelper(context);
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
        String sql = "INSERT INTO SalesHeaders VALUES (NULL, %d, '%s', %d, %d)";
        ExecuteNonQuery(String.format(sql, saleHeader.getId_customers(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_paymentTypes()));
        return kdb.GetLastId("SalesHeaders");
    }

    public void Update(SaleHeader saleHeader){
        String sql = "UPDATE SalesHeaders SET id_customers = %d, customer_name = '%s', total = %d, id_paymentTypes = %d WHERE id = %d";
        ExecuteNonQuery(String.format(sql ,saleHeader.getId_customers(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_paymentTypes(), saleHeader.getId()));
    }
    //endregion
}
