package com.diso.koala.guis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.diso.koala.R;

import java.util.Locale;

public class Home extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Locale.setDefault(new Locale("en"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Events();
    }

    void Events(){
        StartActivity((Button)findViewById(R.id.btnSale), SaleGUI.class);
        StartActivity((Button)findViewById(R.id.btnProductList), ProductListGUI.class);
        StartActivity((Button)findViewById(R.id.btnCustomerList), CustomerListGUI.class);
        StartActivity((Button)findViewById(R.id.btnSaleList), SaleListGUI.class);
        StartActivity((Button)findViewById(R.id.btnAbout), AboutGUI.class);
    }

    void StartActivity(Button b, final Class<?> cls){
        b.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, cls);
                    startActivity(intent);
                }
            }
        );
    }
}
