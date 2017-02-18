package com.agc.alfonso.appr;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alfonso on 18/10/2016.
 */

public class Autopartes
{
    private static ArrayList<Autoparte> autopartes;


    public static void cargar_autopartes(){/*
        autopartes = new ArrayList<>();
        autopartes.add(new Autoparte(1000, 2015,"Audi", "A4", "Wrench", "4 Cil. 1.8 Lts.", 5, "Esta es la descripción", R.drawable.wrench_flat, "Poncho", "Dirección de Poncho", 2.5, 0));
        autopartes.add(new Autoparte(2784.50, 2017,"Tesla", "Model X", "Wrench 2", "Electrico", 5, "Esta es la descripción", R.drawable.wrench2_flat, "Julio", "Dirección de Julio", 5, 0));
        autopartes.add(new Autoparte(182.49, 2013,"VW", "Gol", "Wrench 3", "4 Cils.", 5, "Esta es la descripción", R.drawable.image1, "Jose", "Dirección de Jose", 3.7, 0));
        autopartes.add(new Autoparte(942, 2017,"Tesla", "Model 3", "Wrench 4", "Electrico", 5, "Esta es la descripción", R.drawable.image2, "Adolfo", "Dirección de Adolfo", 5, 0));
        autopartes.add(new Autoparte(47895.55, 2013,"VW", "Clasico", "Wrench 5", "4 Cils.", 5, "Esta es la descripción", R.drawable.image3, "Renata", "Dirección de Renata", 1.5, 0));*/
    }



    /**
     * Obtiene como lista todos los cursos de prueba
     *
     * @return Lista de cursos
     */

    public static void SetAutopartesArray(ArrayList<Autoparte> autopartes_pasa){
        autopartes = autopartes_pasa;
    }

    public static ArrayList<Autoparte> getAutopartes() {
        //cargar_autopartes();
        return autopartes;
    }

    public static void setAutoparte(Autoparte a) {
        //cargar_autopartes();
        autopartes.add(a);
    }

    /**
     * Obtiene un curso basado en la posición del array
     *
     * @param position Posición en el array
     * @return Curso seleccioando
     */
    public static Autoparte getAutoparteByPosition(int position) {
        return autopartes.get(position);
    }

}
