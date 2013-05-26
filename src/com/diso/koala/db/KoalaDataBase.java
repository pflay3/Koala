package com.diso.koala.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KoalaDataBase extends SQLiteOpenHelper {

    //region SQL
    String sqlCreateCustomer = "CREATE TABLE Customer (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL)";
    String sqlDropCustomer = "DROP TABLE IF EXISTS Customer";

    String sqlCreateProduct = "CREATE TABLE Product (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, price INTEGER NOT NULL, barcode TEXT, description TEXT)";
    String sqlDropProduct = "DROP TABLE IF EXISTS Product";
    //endregion

    public KoalaDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        if(db.isReadOnly()){ db = this.getWritableDatabase(); }
        db.execSQL(sqlCreateCustomer);
        db.execSQL(sqlCreateProduct);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion) {
        if (newVersion > previousVersion){
            db.execSQL(sqlDropCustomer);
            db.execSQL(sqlDropProduct);

            db.execSQL(sqlCreateCustomer);
            db.execSQL(sqlCreateProduct);
        }
    }
}