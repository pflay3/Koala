package com.diso.koala.guis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.diso.koala.Functions;
import com.diso.koala.R;
import com.diso.koala.db.helpers.PaymentTypeHelper;
import com.diso.koala.db.helpers.SaleHeaderHelper;

import java.util.Calendar;
import java.util.Date;

public class DateFilterGUI extends Activity {

    // region var
    DatePicker dpDateStart, dpDateEnd;
    Date startDate, endDate;
    Button btnOk, btnCancel;
    //endregion

    //region Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_filter);
        GetFields();
        Events();
        ValidateInitialValues();
    }
    //endregion

    void GetFields(){
        if(dpDateStart == null){
            dpDateStart = (DatePicker)findViewById(R.id.dpDateStart);
            dpDateEnd = (DatePicker)findViewById(R.id.dpDateEnd);
            btnOk = (Button)findViewById(R.id.btnOk);
            btnCancel = (Button)findViewById(R.id.btnCancel);
        }
    }

    void SetInitialValues(){
        final Calendar calendar = Calendar.getInstance();
        if(startDate != null){ calendar.setTime(startDate); }

        dpDateStart.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SetEndDate(year, monthOfYear, dayOfMonth);
            }
        });

        if(endDate != null){ calendar.setTime(endDate); }
        SetEndDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    void SetEndDate(int year, int monthOfYear, int dayOfMonth){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(year, monthOfYear, dayOfMonth);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(dpDateEnd.getYear(), dpDateEnd.getMonth(), dpDateEnd.getDayOfMonth());

        if (calendarStart.compareTo(calendarEnd) == 1){
            dpDateEnd.updateDate(year, monthOfYear, dayOfMonth);
        }
    }

    String GetDataValue(DatePicker dp){
        String month = dp.getMonth() + 1 < 10 ? "0" + Integer.toString(dp.getMonth() + 1) : Integer.toString(dp.getMonth() + 1);
        String day = dp.getDayOfMonth() < 10 ? "0" + Integer.toString(dp.getDayOfMonth()) : Integer.toString(dp.getDayOfMonth());
        return String.format("%d-%s-%s", dp.getYear(), month, day);
    }

    void Events(){

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("dateStart", GetDataValue(dpDateStart));
                SetEndDate(dpDateStart.getYear(), dpDateStart.getMonth(), dpDateStart.getDayOfMonth());
                bundle.putString("dateEnd", GetDataValue(dpDateEnd));
                intent.putExtras(bundle);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }

    void ValidateInitialValues(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            startDate = Functions.GetDate(bundle.getString("dateStart") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            endDate = Functions.GetDate(bundle.getString("dateEnd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }

        SetInitialValues();
    }
}
