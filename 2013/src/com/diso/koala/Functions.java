package com.diso.koala;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.diso.koala.db.entities.PaymentType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * Provide the Application's commons functions
 */
public class Functions {

    /**
     * Get a float number like String
     * @param value float number
     * @return String with the number
     */
    public static String GetFloatValue(float value){
        if(value == (int) value){ return String.format("%d", (int)value); }
        else{ return String.format("%s", value); }
    }

    /**
     * Get a float number like String with two decimals places
     * @param value float number
     * @return String with the number
     */
    public static String GetFloatValueWithTwoDecimals(float value){
        if(value == (int) value){ return String.format("%d", (int)value); }
        else{ return String.format("%.2f", value); }
    }

    /***
     * Get payment types' list into array adapter
     * @param context The current context.
     * @param paymentTypes Vector of Payment Types
     * @return Array Adapter into array adapter
     */
    public static ArrayAdapter<String> GetPaymentTypes(Context context, PaymentType[] paymentTypes){
        final String[] paymentAdapter = new String[paymentTypes.length];

        for (int i = 0; i < paymentTypes.length; i++){
            paymentAdapter[i] = paymentTypes[i].getDescription();
        }

        return GetPaymentTypes(context, paymentAdapter);
    }

    /***
     * Get payment types' list with extra value into array adapter
     * @param context The current context.
     * @param paymentTypes Vector of Payment Types
     * @return Array Adapter into array adapter
     */
    public static ArrayAdapter<String> GetPaymentTypesWithExtra(Context context, PaymentType[] paymentTypes){
        final String[] paymentAdapter = new String[paymentTypes.length + 1];

        paymentAdapter[0] = context.getResources().getString( R.string.payment_types_extra );
        for (int i = 1; i <= paymentTypes.length; i++){
            paymentAdapter[i] = paymentTypes[i - 1].getDescription();
        }

        return GetPaymentTypes(context, paymentAdapter);
    }

    /***
     * Get payment types' list into array adapter
     * @param context The current context.
     * @param paymentAdapter Vector of String with the values
     * @return Array Adapter into array adapter
     */
    private static ArrayAdapter<String> GetPaymentTypes(Context context, String[] paymentAdapter){
        ArrayAdapter<String> data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, paymentAdapter);
        data.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        return data;
    }

    /**
     * Convert Date in String
     * @param date Date
     * @param format Format of return
     * @return String with the date
     */
    public static String GetDate(Date date, String format){
        SimpleDateFormat ft = new SimpleDateFormat (format);
        return ft.format( date );
    }

    /**
     * Convert String in Date
     * @param date String with the date
     * @param format format of input date
     * @return Date
     */
    public static Date GetDate(String date, String format) {
        SimpleDateFormat ft = new SimpleDateFormat(format);
        try {
            return ft.parse( date );
        } catch (ParseException e) {
            return new Date();
        }
    }
}
