package com.diso.koala.db.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.diso.koala.db.*;
import com.diso.koala.db.entities.SaleDetail;
import com.diso.koala.db.entities.SaleHeader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        if (c.moveToFirst()) {
            do {
                SaleHeader saleHeader = new SaleHeader(c.getInt(0), c.getInt(1));
                saleHeader.setCustomer_name(c.getString(2));
                saleHeader.setTotal(c.getFloat(3));
                saleHeader.setId_payment_type(c.getInt(4));
                saleHeader.setDate_sale( GetDate(c.getString(5) ));
                saleHeaders.add(saleHeader);
                //pos++;
            } while(c.moveToNext());
        }

        return saleHeaders;
    }

    public ArrayList<SaleHeader> SelectAll(){
        String sql = "SELECT id_sale_header, id_customer, customer_name, total, id_payment_type, date_sale FROM SalesHeaders";
        return Select(sql);
    }

    public SaleHeader SelectById(int id){
        String sql = "SELECT id_sale_header, id_customer, customer_name, total, id_payment_type, date_sale FROM SalesHeaders WHERE id_sale_header = %d";
        ArrayList<SaleHeader> saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.size() > 0){ return saleHeaders.get(0); }
        else{return null;}
    }

    public SaleHeader SelectByIdWithDetails(int id){
        String sql = "SELECT id_sale_header, id_customer, customer_name, total, id_payment_type, date_sale FROM SalesHeaders WHERE id_sale_header = %d";
        ArrayList<SaleHeader> saleHeaders = Select(String.format(sql, id));
        if(saleHeaders.size() > 0){
            SaleDetail[] saleDetails = saleDetailHelper.SelectByHeaderId(saleHeaders.get(0).getId_sale_header());
            saleHeaders.get(0).addDetails(saleDetails);
            return saleHeaders.get(0);
        }
        else{return null;}
    }

    public ArrayList<SaleHeader> SelectByFilter(ArrayList<QueryFilter> queryFilter){
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append( "SELECT id_sale_header, id_customer, customer_name, total, id_payment_type, date_sale FROM SalesHeaders WHERE " );
        sbQuery.append( QueryBuilder.GetQueryFilter(queryFilter) );
        sbQuery.append( " ORDER BY date_sale DESC" );

        ArrayList<SaleHeader> saleHeaders = Select(sbQuery.toString());
        return saleHeaders;
    }

    public ArrayList<SaleHeader> SelectByFilterWithDetails(ArrayList<QueryFilter> queryFilter){
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append( "SELECT id_sale_header, id_customer, customer_name, total, id_payment_type, date_sale FROM SalesHeaders WHERE " );
        sbQuery.append( QueryBuilder.GetQueryFilter(queryFilter) );
        sbQuery.append( " ORDER BY date_sale DESC" );

        ArrayList<SaleHeader> saleHeaders = Select(sbQuery.toString());
        for (SaleHeader saleHeader: saleHeaders){
            SaleDetail[] saleDetails = saleDetailHelper.SelectByHeaderId(saleHeader.getId_sale_header());
            saleHeader.addDetails(saleDetails);
        }

        return saleHeaders;
    }

    public int Insert(SaleHeader saleHeader){
        String sql = "INSERT INTO SalesHeaders VALUES (NULL, %d, '%s', %f, %d, '%s')";
        kdb.ExecuteNonQuery(String.format(sql, saleHeader.getId_customer(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_payment_type(), GetDate(saleHeader.getDate_sale())));
        return kdb.GetLastId("SalesHeaders");
    }

    public int InsertWithDetails(SaleHeader saleHeader){
        saleHeader.setId_sale_header(this.Insert(saleHeader));
        for ( SaleDetail saleDetail: saleHeader.getDetails() ){
            saleDetail.setId_sale_header(saleHeader.getId_sale_header());
            saleDetailHelper.Insert( saleDetail );
        }

        return saleHeader.getId_sale_header();
    }

    public void UpdateById(SaleHeader saleHeader){
        String sql = "UPDATE SalesHeaders SET id_customer = %d, customer_name = '%s', total = %f, id_payment_type = %d, date_sale = '%s' WHERE id_sale_header = %d";
        kdb.ExecuteNonQuery(String.format(sql, saleHeader.getId_customer(), saleHeader.getCustomer_name(), saleHeader.getTotal(), saleHeader.getId_payment_type(), GetDate(saleHeader.getDate_sale()), saleHeader.getId_sale_header()));
    }

    public void UpdatePaymentTypeById(SaleHeader saleHeader){
        String sql = "UPDATE SalesHeaders SET id_payment_type = %d WHERE id_sale_header = %d";
        kdb.ExecuteNonQuery(String.format(sql, saleHeader.getId_payment_type(), saleHeader.getId_sale_header()));
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
