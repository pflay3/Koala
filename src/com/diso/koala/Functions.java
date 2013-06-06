package com.diso.koala;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.diso.koala.db.PaymentType;

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
     * Get a list the payment types into array adapter
     * @param context The current context.
     * @param paymentTypes Vector of Payment Types
     * @return Array Adapter into array adapter
     */
    public static ArrayAdapter<String> GetPaymentTypes(Context context, PaymentType[] paymentTypes){
        final String[] paymentAdapter = new String[paymentTypes.length];

        for (int i = 0; i < paymentTypes.length; i++){
            paymentAdapter[i] = paymentTypes[i].getDescription();
        }

        ArrayAdapter<String> data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, paymentAdapter);
        data.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        return data;
    }
}
