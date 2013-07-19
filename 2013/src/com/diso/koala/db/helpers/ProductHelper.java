package com.diso.koala.db.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.diso.koala.db.KoalaDataBase;
import com.diso.koala.db.entities.Product;

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
                products[pos] = new Product(c.getInt(0), c.getString(1));
                products[pos].setPrice(c.getFloat(2));
                products[pos].setBarcode(c.getString(3));
                products[pos].setDescription(c.getString(4));
                pos++;
            } while(c.moveToNext());
        }

        return products;
    }

    public Product[] SelectAll(){
        String sql = "SELECT id_product, name, price, barcode, description FROM Products";
        return Select(sql);
    }

    public Product SelectById(int id){
        String sql = "SELECT id_product, name, price, barcode, description FROM Products WHERE id_product = %d";
        Product[] products = Select(String.format(sql, id));
        if(products.length > 0){ return products[0]; }
        else{return null;}
    }

    public Product[] SelectByName(String name){
        String sql = "SELECT id_product, name, price, barcode, description FROM Products WHERE name like '%s%s%s'";
        return Select(String.format(sql, "%", name, "%"));
    }

    public int Insert(Product product){
        String sql = "INSERT INTO Products VALUES (NULL, '%s', %f, '%s', '%s')";
        kdb.ExecuteNonQuery(String.format(sql, product.getName(), product.getPrice(), product.getBarcode(), product.getDescription()));
        return kdb.GetLastId("Products");
    }

    public void UpdateById(Product product){
        String sql = "UPDATE Products SET name = '%s', price = %f, barcode = '%s', description = '%s' WHERE id_product = %d";
        kdb.ExecuteNonQuery(String.format(sql, product.getName(), product.getPrice(), product.getBarcode(), product.getDescription(), product.getId_product()));
    }
    //endregion
}
