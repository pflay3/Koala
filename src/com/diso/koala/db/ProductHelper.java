package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.diso.koala.R;

public class ProductHelper {

    KoalaDataBase kdb;

    public ProductHelper(Context context) {
        kdb = new KoalaDataBase(context);
    }

    //region SQL-Methods
    private Product[] Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        Product[] products = new Product[c.getCount()];
        int pos = 0;

        if (c.moveToFirst()) {
            do {
                products[pos] = new Product(c.getString(1));
                products[pos].setPrice(c.getInt(2));
                products[pos].setBarcode(c.getString(3));
                products[pos].setDescription(c.getString(4));
                pos++;
            } while(c.moveToNext());
        }

        return products;
    }

    public Product[] SelectAll(){
        String sql = "SELECT id, name, price, barcode, description FROM Products";
        return Select(sql);
    }

    public Product[] SelectByName(String name){
        String sql = "SELECT id, name, price, barcode, description FROM Products WHERE name like '%s%s%s'";
        return Select(String.format(sql, "%", name, "%"));
    }

    private void ExecuteNonQuery(String sqlNonQuery){
        SQLiteDatabase db = kdb.getWritableDatabase();
        db.execSQL(sqlNonQuery);
    }

    public void Insert(String name, int price, String barcode, String description){
        String sql = "INSERT INTO Products VALUES (NULL, '%s', %d, '%s', '%s')";
        ExecuteNonQuery(String.format(sql, name, price, barcode, description));
    }

    public void Update(String name, int price, String barcode, String description, int id){
        String sql = "UPDATE Products SET name = '%s', price = %d, barcode = '%s', description = '%s' WHERE id = %d";
        ExecuteNonQuery(String.format(sql, name, price, barcode, description, id));
    }
    //endregion
}
