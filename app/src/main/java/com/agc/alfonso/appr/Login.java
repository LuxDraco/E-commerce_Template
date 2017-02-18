package com.agc.alfonso.appr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

/**
 * Created by Alfonso on 14/09/2016.
 */
public class Login extends AppCompatActivity
{
    //Objetos Facebook Login
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseUser user;

    //Objetos Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Inicia la sesion con boton normal
        final ActionProcessButton entrar = (ActionProcessButton) findViewById(R.id.btnInicioS);
        entrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, LoggedinActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        //Inicia sesion con Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.btnInicioFacebook);
        loginButton.setReadPermissions(Arrays.asList("email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccesToken(loginResult.getAccessToken());
                //goMainScreen();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.fb_login_cancel, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),getString(R.string.error_facebook_login), Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    goMainScreen();
                }
                else {

                }
            }
        };


        //Se va al layout de Registrar
        Button btn_ir_registrar = (Button) findViewById(R.id.hasNoAccount);

        btn_ir_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Login.this, Registrar.class);
                startActivity(intent2);
                finish();
            }
        });


    }//Final On Create

    private void handleFacebookAccesToken(AccessToken accessToken) {
        AuthCredential credential  = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), getString(R.string.Error_firebase_login), Toast.LENGTH_LONG).show();
                }
            }
        });

        if(user == null){
            //Toast.makeText(getApplicationContext(), "Error Al iniciar en Firebase", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent intentfb = new Intent(this, LoggedinActivity.class);
        intentfb.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentfb);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart(){
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }



}
