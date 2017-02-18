package com.agc.alfonso.appr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static android.R.attr.targetActivity;
import static android.R.attr.x;
import static com.agc.alfonso.appr.R.id.url;

public class Fragment_add extends Fragment{


    ImageView imgImagen;
    FloatingActionButton fab;
    Button botonAceptar;
    Activity parent;


    private TextInputLayout autoparte;
    private EditText autoparte_t;
    private TextInputLayout year;
    private EditText year_t;
    private TextInputLayout marca;
    private EditText marca_t;
    private TextInputLayout modelo;
    private EditText modelo_t;
    private TextInputLayout motor;
    private EditText motor_t;
    private TextInputLayout precio;
    private EditText precio_t;
    private TextInputLayout descripcion;
    private EditText descripcion_t;
    private TextInputLayout direccion_vend;
    private EditText direccion_vend_t;


    private EditText editText1;
    private NumberPicker numberPicker;
    private  int numero_piezas;

    private DatabaseReference reference;
    private DatabaseReference obj_referencia;
    private String imageFileName;



    static final int REQUEST_IMAGE_CAPTURE = 1;


    public Fragment_add() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        //getActivity().setTheme(R.style.AppTheme_AppBarOverlay);

        imgImagen = (ImageView) view.findViewById(R.id.foto_add);

        //Boton Flotante
        fab = (FloatingActionButton) view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamrintent();

            }
        });

        // Referencia Botón
        botonAceptar = (Button) view.findViewById(R.id.boton_aceptar_add);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                validarDatos();
            }
        });

        numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // do your other stuff depends on the new value
                //editText1.setText(String.valueOf(newVal));
                numero_piezas = newVal;
            }
        });

        editText1 = (EditText) view.findViewById(R.id.label_cantidad);


        //Referencia a Text Input Layouts
        autoparte = (TextInputLayout) view.findViewById(R.id.input_layout_autoparte);
        autoparte_t = (EditText) view.findViewById(R.id.sube_autoparte);

        year = (TextInputLayout) view.findViewById(R.id.input_layout_year);
        year_t = (EditText) view.findViewById(R.id.sube_year);

        marca = (TextInputLayout) view.findViewById(R.id.input_layout_marca);
        marca_t = (EditText) view.findViewById(R.id.sube_year);

        modelo = (TextInputLayout) view.findViewById(R.id.input_layout_modelo);
        modelo_t = (EditText) view.findViewById(R.id.sube_year);

        motor = (TextInputLayout) view.findViewById(R.id.input_layout_motor);
        motor_t = (EditText) view.findViewById(R.id.sube_year);

        precio = (TextInputLayout) view.findViewById(R.id.input_layout_precio);
        precio_t = (EditText) view.findViewById(R.id.sube_year);

        descripcion = (TextInputLayout) view.findViewById(R.id.input_layout_descripcion);
        descripcion_t = (EditText) view.findViewById(R.id.sube_year);

        direccion_vend = (TextInputLayout) view.findViewById(R.id.input_layout_direccion_vendedor);
        direccion_vend_t = (EditText) view.findViewById(R.id.sube_direccion_vendedor);

        autoparte_t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoparte_t.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void llamrintent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgImagen.setImageBitmap(imageBitmap);
        }
    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z 0-9]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30 || nombre.isEmpty()) {
            autoparte.setError("Nombre inválido");
            return false;
        } else {
            autoparte.setError(null);
        }

        return true;
    }

    private boolean esYearValido(String _year) {
        if (_year.isEmpty()) {
            year.setError("Correo electrónico inválido");
            return false;
        } else {
            year.setError(null);
        }

        return true;
    }

    private void validarDatos() {
        String nombre = autoparte.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esYearValido(year.getEditText().getText().toString());

        if (a)
        {
            // OK, se pasa a la siguiente acción

            try {
                guardar_en_base();
                Fragment fragment = new Fragment_mas_visitados();
                FragmentTransaction ft5 = getActivity().getSupportFragmentManager().beginTransaction();
                ft5.replace(R.id.fragment, fragment);
                ft5.commit();
                getActivity().setTitle("Mas Vendidos");
            }catch (IOException ie){

            }

            Toast.makeText(getContext(), "Producto Guardado", Toast.LENGTH_LONG).show();
        }

    }



    public void guardar_en_base() throws java.io.IOException{

        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://la-refa-e937e.appspot.com");
        // Create a child reference
        // imagesRef now points to "images"

// Create a reference to "mountains.jpg"
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        StorageReference mountainsRef = storageRef.child(imageFileName);

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/mountains.jpg");

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

        imgImagen.setDrawingCacheEnabled(true);
        imgImagen.buildDrawingCache();
        Bitmap bitmap = imgImagen.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Autoparte autoparte1 = new Autoparte(Double.parseDouble(precio.getEditText().getText().toString()),
                Integer.parseInt(year.getEditText().getText().toString()),
                marca.getEditText().getText().toString(),
                modelo.getEditText().getText().toString(),
                autoparte.getEditText().getText().toString(),
                motor.getEditText().getText().toString(),
                numero_piezas,
                descripcion.getEditText().getText().toString(),
                imageFileName,
                firebaseUser.getDisplayName(),
                direccion_vend.getEditText().getText().toString(),
                4,
                0);

        Autopartes.setAutoparte(autoparte1);
        //Log.e("URL ",downloadUrl.toString());
        reference = FirebaseDatabase.getInstance().getReference();

        obj_referencia = reference.child("Partes");
        autoparte1.setID(obj_referencia.push().getKey());
        obj_referencia.push().setValue(autoparte1);
    }





}
