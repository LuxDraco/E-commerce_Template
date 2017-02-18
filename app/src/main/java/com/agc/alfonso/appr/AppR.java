package com.agc.alfonso.appr;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Alfonso on 14/09/2016.
 */


public class AppR extends Application
{

    private ArrayList<Autoparte> autopartes1 = new ArrayList<Autoparte>();;
    private ArrayList<Object> data_firebase1 = new ArrayList<Object>();;


    @Override
    public void onCreate()
    {
        super.onCreate();

        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();

            String[] strings = getResources().getStringArray(R.array.query_suggestions);
            ArrayList<String> sug = new ArrayList<String>(Arrays.asList(strings));

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference obj_referencia = reference.child("sugerencias");
            obj_referencia.setValue(sug);

            obj_referencia = reference.child("Partes");

            /*ArrayList<Autoparte> ap1 = new ArrayList<>(Autopartes.getAutopartes());

            for(int o = 0; o< ap1.size(); ++o){
                obj_referencia.push().setValue(ap1.get(o));
            }*/



        }


        //*****************************************************************************************

        llamar_partes_firebase();

        //*************************************************************************************


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.agc.alfonso.appr",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        //Inicializa SDK Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    public void llamar_partes_firebase(){

        autopartes1 = new ArrayList<Autoparte>();
        data_firebase1 = new ArrayList<Object>();
        DatabaseReference reference1;
        DatabaseReference obj_referencia1;
        Log.e("TAG", "Se llamo al array de partes");

        reference1 = FirebaseDatabase.getInstance().getReference();
        obj_referencia1 = reference1.child("Partes");

        //autopartes = new ArrayList<>();



        obj_referencia1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                Log.e("Count " ,""+ dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    data_firebase1.add(postSnapshot.getValue());
                    Autoparte app = postSnapshot.getValue(Autoparte.class);
                    app.setID(postSnapshot.getKey());
                    autopartes1.add(app);

                    Log.d("Clase : ", postSnapshot.getValue(Autoparte.class).getClass().getName());
                }

/*
                Autoparte[] autopartes_a = data_firebase1.toArray(new Autoparte[data_firebase1.size()]);

                for(int i=0; i<autopartes_a.length; ++i){
                    autopartes1.add(autopartes_a[i]);
                }*/

                Log.e("Clase 2  ", data_firebase1.get(0).getClass().getName());

                Autopartes.SetAutopartesArray(autopartes1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        });
    }

}

