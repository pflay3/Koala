package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.diso.koala.R;

public class KoalaDataBase extends SQLiteOpenHelper {

    //region SQL
    String sqlCreateCustomer = "CREATE TABLE Customers (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, identification TEXT)";
    String sqlDropCustomer = "DROP TABLE IF EXISTS Customers";

    String sqlCreateProduct = "CREATE TABLE Products (id INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, price REAL NOT NULL, barcode TEXT, description TEXT)";
    String sqlDropProduct = "DROP TABLE IF EXISTS Products";

    String sqlCreatePaymentType = "CREATE TABLE PaymentTypes (id INTEGER PRIMARY KEY NOT NULL, description TEXT NOT NULL)";
    String sqlDropPaymentType = "DROP TABLE IF EXISTS PaymentTypes";

    String sqlCreateSalesHeaders = "CREATE TABLE SalesHeaders (id INTEGER PRIMARY KEY NOT NULL, id_customers INTEGER NOT NULL, customer_name TEXT, total REAL NOT NULL, id_paymentTypes INTEGER NOT NULL)";
    String sqlDropSalesHeaders = "DROP TABLE IF EXISTS SalesHeaders";

    String sqlCreateSalesDetails = "CREATE TABLE SalesDetails (id INTEGER PRIMARY KEY NOT NULL, id_salesHeaders INTEGER NOT NULL, id_products INTEGER NOT NULL, product_name TEXT, product_price REAL)";
    String c = "DROP TABLE IF EXISTS SalesDetails";
    //endregion

    public KoalaDataBase(Context context) {
        super(context, "Koala", null, R.integer.db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        if(db.isReadOnly()){ db = this.getWritableDatabase(); }
        CreateTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion) {
        if(db.isReadOnly()){ db = this.getWritableDatabase(); }

        if (newVersion > previousVersion){
            db.execSQL(sqlDropCustomer);
            db.execSQL(sqlDropProduct);
            db.execSQL(sqlDropPaymentType);
            db.execSQL(sqlDropSalesHeaders);
            db.execSQL(sqlDropSalesHeaders);

            CreateTables(db);
        }
    }

    void CreateTables(SQLiteDatabase db){
        db.execSQL(sqlCreateCustomer);
        db.execSQL(sqlCreateProduct);
        db.execSQL(sqlCreatePaymentType);
        db.execSQL(sqlCreateSalesHeaders);
        db.execSQL(sqlCreateSalesDetails);
    }

    public int GetLastId(String table){
        String sql = "SELECT ROWID from %s order by ROWID DESC limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(String.format(sql, table), null);

        if (c != null && c.moveToFirst()) { return c.getInt(0); }
        return -1;
    }
}
