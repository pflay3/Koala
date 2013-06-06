package com.diso.koala.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.*;
import com.diso.koala.R;

public class SaleDetailHelper{

    KoalaDataBase kdb;

    public SaleDetailHelper(Context context) {
        kdb = new KoalaDataBase(context);
    }

    //region SQL-Methods
    private SaleDetail[] Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        SaleDetail[] saleDetails = new SaleDetail[c.getCount()];
        int pos = 0;

        if (c.moveToFirst()) {
            do {
                saleDetails[pos] = new SaleDetail(c.getInt(0), c.getInt(1));
                pos++;
            } while(c.moveToNext());
        }

        return saleDetails;
    }

    public SaleDetail[] SelectAll(){
        String sql = "SELECT id, id_salesHeaders, id_products, product_name, product_price FROM SalesDetails";
        return Select(sql);
    }

    public SaleDetail SelectById(int id){
        String sql = "SELECT id, id_salesHeaders, id_products, product_name, product_price FROM SalesDetails WHERE id = %d";
        SaleDetail[] saleDetails = Select(String.format(sql, id));
        if(saleDetails.length > 0){ return saleDetails[0]; }
        else{return null;}
    }

    public SaleDetail[] SelectByHeaderId(int id){
        String sql = "SELECT id, id_salesHeaders, id_products, product_name, product_price FROM SalesDetails WHERE id_salesHeaders = %d";
        return Select(String.format(sql, id));
    }

    public int Insert(SaleDetail saleDetail){
        String sql = "INSERT INTO SalesDetails VALUES (NULL, %d, %d, '%s', %f)";
        kdb.ExecuteNonQuery(String.format(sql, saleDetail.getId_salesHeaders(), saleDetail.getId_products(), saleDetail.getProduct_name(), saleDetail.getProduct_price()));
        return kdb.GetLastId("SalesDetails");
    }

    public void Update(SaleDetail saleDetail){
        String sql = "UPDATE SalesDetails SET id_salesHeaders = %d, id_products = %d, product_name = '%s', product_price = %f WHERE id = %d";
        kdb.ExecuteNonQuery(String.format(sql ,saleDetail.getId_salesHeaders(), saleDetail.getId_products(), saleDetail.getProduct_name(), saleDetail.getProduct_price(), saleDetail.getId()));
    }
    //endregion
}
