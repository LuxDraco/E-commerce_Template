package com.agc.alfonso.appr;

/**
 * Created by Alfonso on 20/10/2016.
 */

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Mas_vendidos extends AppCompatActivity {
    public RecyclerView recyclerView;
    public LinearLayoutManager linearLayout;

    private DatabaseReference reference;
    private DatabaseReference obj_referencia;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mas_vendidos);

        setupRecyclerView(); // Preparar recycler view

        setupWindowAnimations(); // Añadir animaciones

        reference = FirebaseDatabase.getInstance().getReference();
        obj_referencia = reference.child("sugerencias");

        obj_referencia.orderByKey().startAt("wrench").endAt("wrench").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d("Encontrado", dataSnapshot.child("nombre").getValue().toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private void setupRecyclerView() {
        linearLayout = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.reciclador);
        recyclerView.setLayoutManager(linearLayout);
        AutoparteAdapter adapter = new AutoparteAdapter(this, Autopartes.getAutopartes());
        recyclerView.setAdapter(adapter);
    }

    private void setupWindowAnimations() {
        getWindow().setReenterTransition(new Explode());
        getWindow().setExitTransition(new Explode().setDuration(500));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_shopping_cart) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}