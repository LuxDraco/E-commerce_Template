package com.agc.alfonso.appr;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Alfonso on 19/10/2016.
 */

public class Fragment_mas_visitados extends Fragment
{

    public RecyclerView recyclerView;
    private View view;

    private DatabaseReference reference;
    private DatabaseReference obj_referencia;
    private ArrayList<Autoparte> mas_visitados;

    public Fragment_mas_visitados() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mas_vendidos, container, false);

        reference = FirebaseDatabase.getInstance().getReference();
        obj_referencia = reference.child("Partes");

        obj_referencia.orderByChild("nombre").limitToFirst(2).startAt("Wrench").endAt("Wrench").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.e("Encontrado", dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


        setupRecyclerView(); // Preparar recycler view

        setupWindowAnimations(); // Añadir animaciones

        return view;
    }


    private void setupRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.reciclador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mas_visitados = new ArrayList<>(Autopartes.getAutopartes());
        //sacar_mas_visitados();
        Log.d("Size array", String.valueOf(mas_visitados.size()));
        Log.e("Mas visitado", mas_visitados.get(0).getNombre());
        Log.e("No de visitas", String.valueOf(mas_visitados.get(0).getVisitas()));

        AutoparteAdapter adapter = new AutoparteAdapter(getActivity(), mas_visitados);
        //AutoparteAdapter adapter = new AutoparteAdapter(getActivity(), array_autopartes);
        recyclerView.setAdapter(adapter);
    }



    private void setupWindowAnimations() {
        getActivity().getWindow().setReenterTransition(new Explode());
        getActivity().getWindow().setExitTransition(new Explode().setDuration(500));
    }

    private void sacar_mas_visitados(){
        Autoparte[] array = mas_visitados.toArray(new Autoparte[mas_visitados.size()]);
        Arrays.sort(array);
        mas_visitados = new ArrayList<>(Arrays.asList(array));
    }



}
