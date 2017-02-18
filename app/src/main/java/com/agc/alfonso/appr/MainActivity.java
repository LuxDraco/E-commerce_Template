package com.agc.alfonso.appr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dd.processbutton.iml.ActionProcessButton;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import com.joanzapata.iconify.widget.IconButton;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Iconify
                .with(new FontAwesomeModule())
                .with(new EntypoModule())
                .with(new TypiconsModule())
                .with(new MaterialModule())
                .with(new MaterialCommunityModule())
                .with(new MeteoconsModule())
                .with(new WeathericonsModule())
                .with(new SimpleLineIconsModule())
                .with(new IoniconsModule());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Buscar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbar.setNavigationIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_shopping_cart)
                .colorRes(R.color.white)
                .actionBarSize());
        setTitle("E-commerce");


        //Coloca los elementos a la lista 1
        Spinner spinner1 = (Spinner) findViewById(R.id.lista_1);
        spinner1.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista1, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setSelection(0);

        //Coloca los elementos a la lista 2
        Spinner spinner2 = (Spinner) findViewById(R.id.lista_2);
        spinner2.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.lista2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setSelection(0);

        //Coloca los elementos a la lista 3
        Spinner spinner3 = (Spinner) findViewById(R.id.lista_3);
        spinner3.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.lista3, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setSelection(0);



        /* Botones agregados al final para inicio y registro de sesion

        //Progress Button
        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btnRegistrar);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setProgress(1);

        //Cambio de Layout a Registrar
        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                //finish();
            }
        });

        final ActionProcessButton btn2 = (ActionProcessButton) findViewById(R.id.btn2);
        btn2.setMode(ActionProcessButton.Mode.ENDLESS);
        btn2.setProgress(1);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registrar.class);
                startActivity(intent);
                //finish();
            }
        });

        */



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        menu.findItem(R.id.action_login).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_sign_in)
                        .colorRes(R.color.white)
                        .actionBarSize()
        );

        menu.findItem(R.id.action_regist).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_user_plus)
                        .colorRes(R.color.white)
                        .actionBarSize()
        );




        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {

    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.action_login)
        {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }

        else if(id ==  R.id.action_regist)
        {
            Intent intent = new Intent(MainActivity.this, Registrar.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
