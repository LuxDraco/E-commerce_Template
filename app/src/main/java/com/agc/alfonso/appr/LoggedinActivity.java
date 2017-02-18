package com.agc.alfonso.appr;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.actionitembadge.library.ActionItemBadge;





import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static com.miguelcatalan.materialsearchview.MaterialSearchView.REQUEST_VOICE;


public class LoggedinActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int badgeCount = 1;
    private String nomb_mos;
    private String mail_mos;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private MaterialSearchView searchView;


    private DatabaseReference reference;
    private DatabaseReference obj_referencia;

    protected static final int RESULT_SPEECH = 1;
    static final int check=111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_loggedin);
        setTitle("La Refa");

        //Jalo informacion de firebase para sugererncias
        reference = FirebaseDatabase.getInstance().getReference();
        obj_referencia = reference.child("sugerencias");

        //Asigna AppBarLayout
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarlayout_loggedin);

        //Ver si esta iniciada sesion con Facebook
        //if (AccessToken.getCurrentAccessToken() == null){
            //goLoginScreen();
        //

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Poner los iconos de los item del Navigation Drawer
        Menu menu = navigationView.getMenu();

        MenuItem home = menu.findItem(R.id.nav_home);
        home.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_home)
                .colorRes(R.color.gray)
                .actionBarSize());

        MenuItem carro = menu.findItem(R.id.nav_carrito);
        carro.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_shopping_cart)
                .colorRes(R.color.gray)
                .actionBarSize());

        TextView text_carro = (TextView) MenuItemCompat.getActionView(menu.findItem(R.id.nav_carrito));
        text_carro.setGravity(Gravity.CENTER_VERTICAL);
        text_carro.setTypeface(null, Typeface.BOLD);
        text_carro.setTextColor(getResources().getColor(R.color.colorAccent));
        text_carro.setText(Integer.toString(badgeCount));

        MenuItem busca = menu.findItem(R.id.nav_buscar);
        busca.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_search)
                .colorRes(R.color.gray)
                .actionBarSize());

        MenuItem agrega = menu.findItem(R.id.nav_agregar);
        agrega.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_plus_circle)
                .colorRes(R.color.gray)
                .actionBarSize());

        MenuItem conf = menu.findItem(R.id.nav_config);
        conf.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_cogs)
                .colorRes(R.color.gray)
                .actionBarSize());

        MenuItem about = menu.findItem(R.id.nav_about);
        about.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_question_circle)
                .colorRes(R.color.gray)
                .actionBarSize());

        MenuItem cerrar_sesion = menu.findItem(R.id.nav_cerrar_sesion);
        cerrar_sesion.setIcon(new IconDrawable(this, FontAwesomeIcons.fa_user_times)
                .colorRes(R.color.gray)
                .actionBarSize());


        //Cambiar el nombre del usuario y su correo en el Header del Navigation Drawer
        View headerview = navigationView.getHeaderView(0);
        final TextView nombre_mos = (TextView) headerview.findViewById(R.id.nombre_mostrar);
        final TextView email_mos = (TextView) headerview.findViewById(R.id.correo_mostrar);
        final ImageView profile = (ImageView) headerview.findViewById(R.id.image_profile);

        //Reviso si tengo un usuario valido de Firebase
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            nomb_mos = firebaseUser.getDisplayName();
            mail_mos = firebaseUser.getEmail();

            Glide.with(this)
                    .load(firebaseUser.getPhotoUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(profile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profile.setImageDrawable(circularBitmapDrawable);
                }
            });

        }
        else {
            goLoginScreen();
        }

        nombre_mos.setText(nomb_mos);
        email_mos.setText(mail_mos);

        navigationView.setCheckedItem(0);
        setFragment(5);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);
        searchView.setHintTextColor(getResources().getColor(R.color.gray));
        searchView.setHint("Búsqueda");
        //searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        searchView.setVoiceIcon(new IconDrawable(this, FontAwesomeIcons.fa_microphone).colorRes(R.color.white).actionBarSize());
        //searchView.showVoice(true);
        //searchView.setSuggestions(obj_referencia.ge);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Snackbar.make(getWindow().getDecorView(), "Busqueda: " + query, Snackbar.LENGTH_LONG).show();
                obj_referencia.push().setValue(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                busqueda_voz();
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        obj_referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //ArrayList<String> sug_firebase = new ArrayList<String>(Arrays.asList(dataSnapshot.getKey()));
                ArrayList<Object> sug_firebase = new ArrayList<Object>();
                //Toast t = Toast.makeText(getApplicationContext(), sug_firebase.get(0), Toast.LENGTH_SHORT);
                //t.show();
                //String[] sug = sug_firebase.toArray(new String[sug_firebase.size()]);
                //searchView.setSuggestions(sug);

                Log.e("Count " ,""+ dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    sug_firebase.add(postSnapshot.getValue());
                }

                String[] sug = sug_firebase.toArray(new String[sug_firebase.size()]);
                //Toast t = Toast.makeText(getApplicationContext(), sug[0], Toast.LENGTH_SHORT);
                //t.show();
                searchView.setSuggestions(sug);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        });


    }


    public void busqueda_voz(){

        setFragment(1);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "es-MX");

        try {
            startActivityForResult(intent, check);
            //txtText.setText("");
            //ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //String searchWrd = matches.get(0);
            //searchView.setQuery(matches.get(0), false);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == check && resultCode == RESULT_OK) {
            //Toast t0 = Toast.makeText(getApplicationContext(), "entre", Toast.LENGTH_SHORT);
            //t0.show();
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                    searchView.setQuery(searchWrd, false);
                //Toast t = Toast.makeText(getApplicationContext(), searchWrd, Toast.LENGTH_SHORT);
                //t.show();
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void goLoginScreen() {
        Intent intent_login = new Intent(this, Login.class);
        intent_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent_login);
    }

    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Sesión Finalizada", Toast.LENGTH_SHORT);
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("¿Seguro que desea salir?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoggedinActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loggedin, menu);

        MenuItem ac_search = menu.findItem(R.id.action_search);

        ac_search.setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_search)
                        .colorRes(R.color.white)
                        .actionBarSize()
        );

        searchView.setMenuItem(ac_search);

        if (badgeCount > 0) {
            ActionItemBadge.update(this, menu.findItem(R.id.action_shopping_cart), new IconDrawable(this, FontAwesomeIcons.fa_shopping_cart)
                    .colorRes(R.color.white)
                    .actionBarSize(), ActionItemBadge.BadgeStyles.RED, badgeCount);
        } else {
            ActionItemBadge.hide(menu.findItem(R.id.action_shopping_cart));
        }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            setFragment(1);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_shopping_cart) {
            setFragment(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            if (item.getItemId() == R.id.nav_agregar) {
                appBarLayout.setElevation(0);
            } else {
                appBarLayout.setElevation(8);
            }
        }



        if (id == R.id.nav_carrito)
        {
            // Handle the camera action
            setFragment(0);
        }
        else if (id == R.id.nav_buscar)
        {
            setFragment(1);
        }
        else if (id == R.id.nav_agregar)
        {
            setFragment(2);
        }
        else if (id == R.id.nav_config)
        {
            setFragment(3);
        }
        else if (id == R.id.nav_about)
        {
            setFragment(4);
        }
        else if (id == R.id.nav_home)
        {
            setFragment(5);
        }
        else if (id == R.id.nav_cerrar_sesion)
        {
            logout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragment_carrito();
                //FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction().addToBackStack("");
                FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
                ft0.replace(R.id.fragment, fragment);
                ft0.commit();
                setTitle("Carrito");
                break;
            case 1:
                fragment = new Fragment_searc();
                FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.fragment, fragment);
                ft1.commit();
                setTitle("Buscar");
                break;
            case 2:
                fragment = new Fragment_add();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.fragment, fragment);
                ft2.commit();
                setTitle("Sube tu producto");
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                fragment = new Fragment_mas_visitados();
                FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                ft5.replace(R.id.fragment, fragment);
                ft5.commit();
                setTitle("Mas Vendidos");
                break;
        }
    }



}
