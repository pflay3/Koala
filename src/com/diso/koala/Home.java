package com.diso.koala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Events();
    }

    void Events(){
        final Button btnAgregar = (Button)findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, ProductGUI.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
