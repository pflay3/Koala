package com.diso.koala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Events();
    }

    void Events(){
        StartActivity((Button)findViewById(R.id.btnSale), SaleGUI.class);
        StartActivity((Button)findViewById(R.id.btnProductList), ProductListGUI.class);
        StartActivity((Button)findViewById(R.id.btnCustomerList), CustomerListGUI.class);
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
