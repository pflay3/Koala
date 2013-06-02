package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PaymentTypeHelper {

    KoalaDataBase kdb;

    public PaymentTypeHelper(Context context) {
        kdb = new KoalaDataBase(context);
    }

    //region SQL-Methods
    private PaymentType[] Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        PaymentType[] paymentTypes = new PaymentType[c.getCount()];
        int pos = 0;

        if (c.moveToFirst()) {
            do {
                paymentTypes[pos] = new PaymentType(c.getInt(0), c.getString(1));
                pos++;
            } while(c.moveToNext());
        }

        return paymentTypes;
    }

    public PaymentType[] SelectAll(){
        String sql = "SELECT id, description FROM PaymentTypes";
        return Select(sql);
    }
    //endregion
}
