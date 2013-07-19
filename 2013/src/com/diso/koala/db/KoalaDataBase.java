package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.diso.koala.R;

public class KoalaDataBase extends SQLiteOpenHelper {

    //region SQL
    String sqlCreateCustomer = "CREATE TABLE Customers (id_customer INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, identification TEXT)";
    String sqlDropCustomer = "DROP TABLE IF EXISTS Customers";

    String sqlCreateProduct = "CREATE TABLE Products (id_product INTEGER PRIMARY KEY NOT NULL, name TEXT NOT NULL, price REAL NOT NULL, barcode TEXT, description TEXT)";
    String sqlDropProduct = "DROP TABLE IF EXISTS Products";

    String sqlCreatePaymentType = "CREATE TABLE PaymentTypes (id_payment_type INTEGER PRIMARY KEY NOT NULL, description TEXT NOT NULL)";
    String sqlDropPaymentType = "DROP TABLE IF EXISTS PaymentTypes";

    String sqlCreateSalesHeaders = "CREATE TABLE SalesHeaders (id_sale_header INTEGER PRIMARY KEY NOT NULL, id_customer INTEGER NOT NULL, customer_name TEXT, total REAL NOT NULL, id_payment_type INTEGER NOT NULL, date_sale NUMERIC)";
    String sqlDropSalesHeaders = "DROP TABLE IF EXISTS SalesHeaders";

    String sqlCreateSalesDetails = "CREATE TABLE SalesDetails (id_sale_detail INTEGER PRIMARY KEY NOT NULL, id_sale_header INTEGER NOT NULL, id_product INTEGER NOT NULL, product_name TEXT, product_price REAL)";
    String sqlDropSalesDetails = "DROP TABLE IF EXISTS SalesDetails";
    //endregion

    public KoalaDataBase(Context context) {
        super(context, "Koala", null, R.integer.db_version);
    }

    //region Override
    @Override
    public void onCreate(SQLiteDatabase db){
        if(db.isReadOnly()){ db = this.getWritableDatabase(); }
        CreateTables(db);
        InsertDefaultPaymentTypes(db);
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
            db.execSQL(sqlDropSalesDetails);

            CreateTables(db);
        }
    }
    //endregion

    void CreateTables(SQLiteDatabase db){
        db.execSQL(sqlCreateCustomer);
        db.execSQL(sqlCreateProduct);
        db.execSQL(sqlCreatePaymentType);
        db.execSQL(sqlCreateSalesHeaders);
        db.execSQL(sqlCreateSalesDetails);
    }

    void InsertDefaultPaymentTypes(SQLiteDatabase db){
        db.execSQL( "INSERT INTO PaymentTypes VALUES( 1, 'Pagó' )" );
        db.execSQL( "INSERT INTO PaymentTypes VALUES( 2, 'Debe' )" );
    }

    public int GetLastId(String table){
        String sql = "SELECT ROWID from %s order by ROWID DESC limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(String.format(sql, table), null);

        if (c != null && c.moveToFirst()) { return c.getInt(0); }
        return -1;
    }

    public void ExecuteNonQuery(String sqlNonQuery){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sqlNonQuery);
    }
}
