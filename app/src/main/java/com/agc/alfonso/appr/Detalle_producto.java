package com.agc.alfonso.appr;

/**
 * Created by Alfonso on 18/10/2016.
 */


import android.app.Activity;
import android.app.ActivityOptions;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Detalle_producto extends AppCompatActivity {
    private static final String EXTRA_POSITION = "com.agc.alfonso.appr.extra.POSITION";

    private DatabaseReference reference1;
    private DatabaseReference obj_referencia1;

    private Autoparte ap = new Autoparte();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        setToolbar(); // Reemplazar la action bar

        // Se obtiene la posición del item seleccionado
        int position = getIntent().getIntExtra(EXTRA_POSITION, -1);

        reference1 = FirebaseDatabase.getInstance().getReference();
        obj_referencia1 = reference1.child("Partes");

        // Carga los datos en la vista
        setupViews(position);

        Window window = getWindow();
        Explode t0 = new Explode();
        window.setEnterTransition(t0);

        // Elegir transiciones
        /*
        switch (position) {
            // EXPLODE
            case 0:
                Explode t0 = new Explode();
                window.setEnterTransition(t0);
                break;
            // SLIDE
            case 1:
                Slide t1 = new Slide();
                t1.setSlideEdge(Gravity.END);
                window.setEnterTransition(t1);
                break;
            // FADE
            case 2:
                Fade t2 = new Fade();
                window.setEnterTransition(t2);
                break;
            // PERSONALIZADA
            case 3:
                Transition t3 = TransitionInflater.from(this)
                        .inflateTransition(R.transition.detail_enter_trasition);
                window.setEnterTransition(t3);
                break;
            // EVENTOS DE TRANSICIÓN
            case 4:
                Fade t4 = new Fade();
                t4.addListener(
                        new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(Transition transition) {
                                Snackbar.make(
                                        findViewById(R.id.coordinator),
                                        "Terminó la transición",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onTransitionCancel(Transition transition) {

                            }

                            @Override
                            public void onTransitionPause(Transition transition) {

                            }

                            @Override
                            public void onTransitionResume(Transition transition) {

                            }
                        }
                );
                window.setEnterTransition(t4);
                break;
            // POR DEFECTO
            case 5:
                window.setEnterTransition(null);
                break;

        }
        */
    }

    private void setupViews(int position) {
        TextView name = (TextView) findViewById(R.id.name_detalle);
        TextView year = (TextView) findViewById(R.id.year_detalle);
        TextView marca = (TextView) findViewById(R.id.marca_detalle);
        TextView modelo = (TextView) findViewById(R.id.modelo_detalle);
        TextView motor = (TextView) findViewById(R.id.motor_detalle);
        TextView cantidad = (TextView) findViewById(R.id.cantidad_detalle);
        TextView descripcion = (TextView) findViewById(R.id.descripcion_detalle);
        RatingBar rating = (RatingBar) findViewById(R.id.rating_detalle);
        ImageView image = (ImageView) findViewById(R.id.detail_image);
        TextView name_vendedor = (TextView) findViewById(R.id.nombre_vendedor_detalle);
        TextView direccion_vendedor = (TextView) findViewById(R.id.direccion_vendedor_detalle);
        TextView precio = (TextView) findViewById(R.id.precio_detalle);


        // Obtiene la parte ha detallar basado en la posición
        final Autoparte detalleParte = Autopartes.getAutoparteByPosition(position);
        //obj_referencia1.push().setValue(detalleParte);
        //obj_referencia1.removeValue(detalleParte);

        name.setText(detalleParte.getNombre());
        year.setText(String.valueOf(detalleParte.getYear()));
        marca.setText(detalleParte.getMarca());
        modelo.setText(detalleParte.getModelo());
        motor.setText(detalleParte.getMotor());
        cantidad.setText(String.valueOf(detalleParte.getCantidad())); //Este es un entero
        descripcion.setText(detalleParte.getDescripcion());
        rating.setRating((float)detalleParte.getRating());//Este es un double

        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        name_vendedor.setText(detalleParte.getNombre_vendedor());

        direccion_vendedor.setText(detalleParte.getDireccion_vendedor());
        precio.setText("$" + String.valueOf(detalleParte.getPrecio()));

        if(detalleParte.getIdImage() == 0){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReferenceFromUrl("gs://la-refa-e937e.appspot.com");
            StorageReference mountainsRef = storageRef.child(detalleParte.getPath());

            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(mountainsRef)
                    .into(image);
        }
        else {
            // Cargar imagen
            Glide.with(this)
                    .load(detalleParte.getIdImage())
                    .into(image);
        }


        detalleParte.setVisitas(detalleParte.getVisitas()+1);
        obj_referencia1.child(detalleParte.getID()).child("visitas").setValue(detalleParte.getVisitas());

        obj_referencia1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ap = postSnapshot.getValue(Autoparte.class);
                    detalleParte.setID(postSnapshot.getKey());

                    if(ap.getID() == postSnapshot.getKey()){
                        Log.e("Situacion : ", postSnapshot.getValue(Autoparte.class).getNombre());

                        //obj_referencia1.child(postSnapshot.getKey()).child("visitas").setValue(detalleParte.getVisitas());
                    }
                    else if(!ap.equals(detalleParte)) {
                        Log.e(ap.getNombre()+ "  ", postSnapshot.getKey());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        });

        //obj_referencia.child(String.valueOf(position)).child("visitas").setValue(detalleParte.getVisitas());



    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)// Habilitar Up Button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
            case android.R.id.home:
                // Obtener intent de la actividad padre
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                // Comprobar si DetailActivity no se creó desde CourseActivity
                if (NavUtils.shouldUpRecreateTask(this, upIntent)
                        || this.isTaskRoot()) {

                    // Construir de nuevo la tarea para ligar ambas actividades
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Terminar con el método correspondiente para Android 5.x
                    this.finishAfterTransition();
                    return true;
                }

                // Dejar que el sistema maneje el comportamiento del up button
                return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return super.onCreateOptionsMenu(menu);
    }

    public static void launch(Activity context, int position, View sharedView) {
        Intent intent = new Intent(context, Detalle_producto.class);
        intent.putExtra(EXTRA_POSITION, position);

            ActivityOptions options0 = ActivityOptions.makeSceneTransitionAnimation(context);
            context.startActivity(intent, options0.toBundle());


    }
}
