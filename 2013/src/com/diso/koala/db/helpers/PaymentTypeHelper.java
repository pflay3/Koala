package com.diso.koala.db.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.diso.koala.db.KoalaDataBase;
import com.diso.koala.db.entities.PaymentType;

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
        String sql = "SELECT id_payment_type, description FROM PaymentTypes";
        return Select(sql);
    }

    public PaymentType SelectById(int id){
        String sql = "SELECT id_payment_type, description FROM PaymentTypes WHERE id_payment_type = %d";
        PaymentType[] paymentTypes = Select(String.format(sql, id));
        if(paymentTypes.length > 0){ return paymentTypes[0]; }
        else{return null;}
    }
    //endregion
}
