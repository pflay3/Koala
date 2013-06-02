package com.diso.koala;

public class Functions {

    public static String GetFloatValue(float value){
        if(value == (int) value){ return String.format("%d", (int)value); }
        else{ return String.format("%s", value); }
    }

    public static String GetFloatValueWithTwoDecimals(float value){
        if(value == (int) value){ return String.format("%d", (int)value); }
        else{ return String.format("%.2f", value); }
    }
}
