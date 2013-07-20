package com.diso.koala.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.*;
import com.diso.koala.db.KoalaDataBase;
import com.diso.koala.db.entities.SaleDetail;

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
                saleDetails[pos].setId_product(c.getInt(2));
                saleDetails[pos].setProduct_name(c.getString(3));
                saleDetails[pos].setProduct_price(c.getFloat(4));
                pos++;
            } while(c.moveToNext());
        }

        return saleDetails;
    }

    public SaleDetail[] SelectAll(){
        String sql = "SELECT id_sale_detail, id_sale_header, id_product, product_name, product_price FROM SalesDetails";
        return Select(sql);
    }

    public SaleDetail SelectById(int id){
        String sql = "SELECT id_sale_detail, id_sale_header, id_product, product_name, product_price FROM SalesDetails WHERE id_sale_detail = %d";
        SaleDetail[] saleDetails = Select(String.format(sql, id));
        if(saleDetails.length > 0){ return saleDetails[0]; }
        else{return null;}
    }

    public SaleDetail[] SelectByHeaderId(int id){
        String sql = "SELECT id_sale_detail, id_sale_header, id_product, product_name, product_price FROM SalesDetails WHERE id_sale_header = %d";
        return Select(String.format(sql, id));
    }

    public int Insert(SaleDetail saleDetail){
        String sql = "INSERT INTO SalesDetails VALUES (NULL, %d, %d, '%s', %f)";
        kdb.ExecuteNonQuery(String.format(sql, saleDetail.getId_sale_header(), saleDetail.getId_product(), saleDetail.getProduct_name(), saleDetail.getProduct_price()));
        return kdb.GetLastId("SalesDetails");
    }

    public void UpdateById(SaleDetail saleDetail){
        String sql = "UPDATE SalesDetails SET id_sale_header = %d, id_product = %d, product_name = '%s', product_price = %f WHERE id_sale_detail = %d";
        kdb.ExecuteNonQuery(String.format(sql ,saleDetail.getId_sale_header(), saleDetail.getId_product(), saleDetail.getProduct_name(), saleDetail.getProduct_price(), saleDetail.getId_sale_detail()));
    }
    //endregion
}
