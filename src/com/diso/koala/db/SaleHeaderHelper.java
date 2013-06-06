package com.diso.koala.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class SaleHeaderHelper {

    KoalaDataBase kdb;
    Context context;
    SaleDetailHelper saleDetailHelper;

    public SaleHeaderHelper(Context context) {
        this.context = context;
        kdb = new KoalaDataBase(context);
        saleDetailHelper = new SaleDetailHelper(context);
    }

    //region SQL-Methods
    private ArrayList<SaleHeader> Select(String sqlSelect){
        SQLiteDatabase db = kdb.getReadableDatabase();
        Cursor c = db.rawQuery(sqlSelect, null);
        ArrayList<SaleHeader> saleHeaders = new ArrayList<SaleHeader>();
        //int pos = 0;

        if (c.moveToFirst()) {
            do {
                SaleHeader saleHeader = new SaleHeader(c.getInt(0), c.getInt(1));
                saleHeader.setCustomer_name(c.getString(2));
                saleHeader.setTotal(c.getFloat(3));
                saleHeader.setId_paymentTypes(c.getInt(4));
                saleHeader.setDate_sale( GetDate(c.getString(5) ));
                saleHeaders.add(saleHeader);
                //pos++;
            } while(c.moveToNext());
        }

        return saleHeaders;
    }

    public ArrayList<SaleHeader> SelectAll(){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders";
        return Select(sql);
    }

    public SaleHeader SelectById(int id){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders WHERE id = %d";
        ArrayList<SaleHeader> saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.size() > 0){ return saleHeaders.get(0); }
        else{return null;}
    }

    public SaleHeader SelectByIdWithDetails(int id){
        String sql = "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders WHERE id = %d";
        ArrayList<SaleHeader> saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.size() > 0){
            SaleDetail[] saleDetails = saleDetailHelper.SelectByHeaderId(saleHeaders.get(0).getId());
            saleHeaders.get(0).addDetails(saleDetails);
            return saleHeaders.get(0);
        }
        else{return null;}
    }

    public ArrayList<SaleHeader> SelectWithDetails(ArrayList<QueryFilter> queryFilter){
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append( "SELECT id, id_customers, customer_name, total, id_paymentTypes, date_sale FROM SalesHeaders WHERE " );
        sbQuery.append( QueryBuilder.GetQueryFilter( queryFilter ) );

        ArrayList<SaleHeader> saleHeaders = Select(sbQuery.toString());
        for (SaleHeader saleHeader: saleHeaders){
            SaleDetail[] saleDetails = saleDetailHelper.SelectByHeaderId(saleHeader.getId());
            saleHeader.addDetails(saleDetails);
        }

        return saleHeaders;
    }

    public int Insert(SaleHeader saleHeader){
        String sql = "INSERT INTO SalesHeaders VALUES (NULL, %d, '%s', %f, %d, '%s')";
        kdb.ExecuteNonQuery(String.format(sql, saleHeader.getId_customers(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_paymentTypes(), GetDate(saleHeader.getDate_sale())));
        return kdb.GetLastId("SalesHeaders");
    }

    public int InsertWithDetails(SaleHeader saleHeader){
        saleHeader.setId(this.Insert(saleHeader));
        for ( SaleDetail saleDetail: saleHeader.getDetails() ){
            saleDetail.setId_salesHeaders(saleHeader.getId());
            saleDetailHelper.Insert( saleDetail );
        }

        return saleHeader.getId();
    }

    public void Update(SaleHeader saleHeader){
        String sql = "UPDATE SalesHeaders SET id_customers = %d, customer_name = '%s', total = %f, id_paymentTypes = %d, date_sale = '%s' WHERE id = %d";
        kdb.ExecuteNonQuery(String.format(sql, saleHeader.getId_customers(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_paymentTypes(), GetDate(saleHeader.getDate_sale()), saleHeader.getId()));
    }
    //endregion

    String GetDate(Date date){
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        return ft.format( date );
    }

    Date GetDate(String date) {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        try {
            return ft.parse( date );
        } catch (ParseException e) {
            return new Date();
        }
    }
}
