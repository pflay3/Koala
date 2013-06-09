package com.diso.koala.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.*;
import com.diso.koala.db.entities.Customer;
import com.diso.koala.db.KoalaDataBase;

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

    public int Insert(Customer customer){
        String sql = "INSERT INTO Customers VALUES (NULL, '%s', '%s')";
        kdb.ExecuteNonQuery(String.format(sql, customer.getName(), customer.getIdentification()));
        return kdb.GetLastId("Customers");
    }

    public void UpdateById(Customer customer){
        String sql = "UPDATE Customers SET name = '%s', identification = '%s' WHERE id = %d";
        kdb.ExecuteNonQuery(String.format(sql, customer.getName(), customer.getIdentification(), customer.getId()));
    }
    //endregion
}
