package com.diso.koala.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.*;
import com.diso.koala.R;

public class CustomerHelper{

    KoalaDataBase kdb;

    public CustomerHelper(Context context, String dbName) {
        kdb = new KoalaDataBase(context, dbName, null, R.integer.db_version);
    }

    //region SQL-Methods
    private Customer[] Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        Customer[] customers = new Customer[c.getCount()];
        int pos = 0;

        if (c.moveToFirst()) {
            do {
                customers[pos] = new Customer(c.getString(1));
                pos++;
            } while(c.moveToNext());
        }

        return customers;
    }

    public Customer[] SelectAll(){
        String sql = "SELECT id, name FROM Customer";
        return Select(sql);
    }

    public Customer[] SelectByName(String name){
        String sql = "SELECT id, name FROM Customer WHERE name like '%s'";
        return Select(String.format(sql, "%" + name + "%"));
    }

    private void ExecuteNonQuery(String sqlNonQuery){
        SQLiteDatabase db = kdb.getWritableDatabase();
        db.execSQL(sqlNonQuery);
    }

    public void Insert(String name){
        String sql = "INSERT INTO Customer VALUES (NULL, '%s')";
        ExecuteNonQuery(String.format(sql,name));
    }

    public void Update(String name, int id){
        String sql = "UPDATE Customer SET name = '%s' WHERE id = %d";
        ExecuteNonQuery(String.format(sql, name, id));
    }
    //endregion
}
