package com.agc.alfonso.appr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Alfonso on 14/09/2016.
 */
public class Registrar extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registra);


        Button hasaccount = (Button) findViewById(R.id.hasAccount);
        hasaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registrar.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


    }//Final On Create
}
