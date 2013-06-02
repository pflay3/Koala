package com.diso.koala.db;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.*;
import com.diso.koala.R;

public class CustomerHelper{

    KoalaDataBase kdb;

    public CustomerHelper(Context context) {
        kdb = new KoalaDataBase(context);
    }

    //region SQL-Methods
    private Customer[] Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        Customer[] customers = new Customer[c.getCount()];
        int pos = 0;

        if (c.moveToFirst()) {
            do {
                customers[pos] = new Customer(c.getInt(0), c.getString(1));
                customers[pos].setIdentification(c.getString(2));
                pos++;
            } while(c.moveToNext());
        }

        return customers;
    }

    public Customer[] SelectAll(){
        String sql = "SELECT id, name, identification FROM Customers";
        return Select(sql);
    }

    public Customer SelectById(int id){
        String sql = "SELECT id, name, identification FROM Customers WHERE id = %d";
        Customer[] customers = Select(String.format(sql, id));
        if(customers.length > 0){ return customers[0]; }
        else{return null;}
    }

    public Customer[] SelectByName(String name){
        String sql = "SELECT id, name, identification FROM Customers WHERE name like '%s'";
        return Select(String.format(sql, "%" + name + "%"));
    }

    private void ExecuteNonQuery(String sqlNonQuery){
        SQLiteDatabase db = kdb.getWritableDatabase();
        db.execSQL(sqlNonQuery);
    }

    public int Insert(Customer customer){
        String sql = "INSERT INTO Customers VALUES (NULL, '%s', '%s')";
        ExecuteNonQuery(String.format(sql, customer.getName(), customer.getIdentification()));
        return kdb.GetLastId("Customers");
    }

    public void Update(Customer customer){
        String sql = "UPDATE Customers SET name = '%s', identification = '%s' WHERE id = %d";
        ExecuteNonQuery(String.format(sql, customer.getName(), customer.getIdentification(), customer.getId()));
    }
    //endregion
}
