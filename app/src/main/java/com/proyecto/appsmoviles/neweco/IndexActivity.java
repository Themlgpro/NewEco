package com.proyecto.appsmoviles.neweco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.app.Fragment;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.proyecto.appsmoviles.neweco.Database.NewEco;

import com.google.android.gms.common.api.Status;

import com.proyecto.appsmoviles.neweco.Mapping.usuario;


public class IndexActivity extends AppCompatActivity implements donacionesEcologicas.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener, PublicarIdea.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener{


    private NewEco conexion;
    private SQLiteDatabase bd;
    private usuario userData;
    PublicarIdea pi;

     GoogleSignInAccount account;
    private ImageView photo;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView idTextView;
    private LoginActivity la;
    private  donacionesEcologicas donaEco;


    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Inicializacion y creacion;
        conexion = new NewEco(this,"NewEco",null,1);
        bd = conexion.getWritableDatabase();


        //Recibiendo el usuario local
        userData = new usuario(getIntent().getExtras().getString("Usuario"),getIntent().getExtras().getString("Correo"),
                getIntent().getExtras().getString("idUsuario"));


        TextView userName = (TextView) findViewById(R.id.userName);
        TextView userContact = (TextView) findViewById(R.id.correoUsuario);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //Recibiendo el usuario local
        userData = new usuario(getIntent().getExtras().getString("Usuario"),getIntent().getExtras().getString("Correo"),
                getIntent().getExtras().getString("idUsuario"));
        if(!getIntent().getExtras().getBoolean("bandera")){
            configOffline();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

          account = result.getSignInAccount();

            configOnline();
            userData = new usuario(account.getDisplayName(),account.getEmail(),"");
           //


        } else {
           Toast.makeText(getApplicationContext(),"Has iniciado sesion local.", Toast.LENGTH_SHORT).show();
        }
    }


     private void configOffline(){
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        ((TextView) header.findViewById(R.id.userName)).setText(userData.getNombre());
        ((TextView) header.findViewById(R.id.correoUsuario)).setText(userData.getCorreo());

    }
    private void configOnline(){
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        ((TextView) header.findViewById(R.id.userName)).setText(account.getDisplayName());
        ((TextView) header.findViewById(R.id.correoUsuario)).setText(account.getEmail());
        photo = (ImageView) header.findViewById(R.id.fotoPerfil);

       Glide.with(this).load(account.getPhotoUrl()).into(photo);


    }
    private void goLogInScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void logOut(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(),"not close", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }






    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.publicIdea) {
            Toast.makeText(this,"Publicar idea",Toast.LENGTH_LONG).show();


        } else if (id == R.id.nav_gallery) {

            Toast.makeText(this,"Inicio",Toast.LENGTH_LONG).show();


        } else if (id == R.id.nav_slideshow) {
            Toast.makeText(this,"Noticias",Toast.LENGTH_LONG).show();
            Intent goToNoticias = new Intent(this,Noticias.class);
            goToNoticias.addFlags(goToNoticias.FLAG_ACTIVITY_CLEAR_TOP | goToNoticias.FLAG_ACTIVITY_CLEAR_TASK);


            goToNoticias.putExtra("Usuario", userData.getNombre());
            goToNoticias.putExtra("Correo", userData.getCorreo());
            goToNoticias.putExtra("idUsuario", userData.getCedula());

            startActivity(goToNoticias);

        } else if (id == R.id.nav_manage) {
            Toast.makeText(this,"Donaciones",Toast.LENGTH_LONG).show();
            donaEco = new donacionesEcologicas();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contexto,donaEco);
            transaction.commit();

        } else if (id == R.id.donaciones) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        goLogInScreen();
                    } else {
                        Toast.makeText(getApplicationContext(),"not close", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void publicarIdea(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
}
