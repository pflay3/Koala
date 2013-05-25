package com.diso.koala.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.*;
import android.graphics.Region;

public class CustomerHelper extends SQLiteOpenHelper{

    String sqlCreate = "CREATE TABLE Customer (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL)";
    String sqlDrop = "DROP TABLE IF EXISTS Customer";

    public CustomerHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        if(db.isReadOnly()){ db = this.getWritableDatabase(); }
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion) {
        if (newVersion > previousVersion){
            db.execSQL(sqlDrop);
            db.execSQL(sqlCreate);
        }
    }

    //region SQL-Methods
    private Customer[] Select(String sqlSelect){
        SQLiteDatabase db = this.getReadableDatabase();
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
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlNonQuery);
    }

    public void Insert(String name){
        String sql = "INSERT INTO Customer VALUES (NULL, '%s')";
        ExecuteNonQuery(String.format(sql,name));
    }

    public void Update(int id){
        String sql = "UPDATE Customer SET name = '?' WHERE id = %d";
        ExecuteNonQuery(String.format(sql,id));
    }
    //endregion
}
