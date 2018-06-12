package com.proyecto.appsmoviles.neweco;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.app.Fragment;

import android.widget.ListView;
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
import com.proyecto.appsmoviles.neweco.Database.AsyncResponse;
import com.proyecto.appsmoviles.neweco.Database.NewEco;

import com.google.android.gms.common.api.Status;

import com.proyecto.appsmoviles.neweco.Database.getIdeas;
import com.proyecto.appsmoviles.neweco.Database.pendingIdeas;
import com.proyecto.appsmoviles.neweco.Mapping.usuario;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Vector;

public class IndexActivity extends AppCompatActivity implements donacionesEcologicas.OnFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener, PublicarIdea.OnFragmentInteractionListener,GoogleApiClient.OnConnectionFailedListener, AsyncResponse{

    private NewEco conexion;
    private SQLiteDatabase bd;
    private usuario userData;
    private PublicarIdea pi;
    private String ideas;
    private JSONObject ideasJson;
    private ArrayList<JSONObject> listIdeas;
    private ArrayList<String> unitIdea;

    private GoogleSignInAccount account;
    private ImageView photo;
    private LoginActivity la;
    private donacionesEcologicas donaEco;


    private GoogleApiClient googleApiClient;
    ListView listaIdeas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.listIdeas = new ArrayList<JSONObject>();
        this.listaIdeas = (ListView) findViewById(R.id.listaIdeas);
        //Inicializando la lista ----------------------------------
        unitIdea = new ArrayList<String>();
        ArrayAdapter listAdapter = new ArrayAdapter(this,  android.R.layout.simple_expandable_list_item_1, this.unitIdea);
        listaIdeas.setAdapter(listAdapter);

        //---------------------------------------------------------

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Inicializacion y creacion;
        conexion = new NewEco(this, "NewEco", null, 1);
        bd = conexion.getWritableDatabase();


        //Recibiendo el usuario local
        userData = new usuario(getIntent().getExtras().getString("Usuario"), getIntent().getExtras().getString("Correo"),
                getIntent().getExtras().getString("idUsuario"));



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        if (!getIntent().getExtras().getBoolean("bandera")) {
            configOffline();
        }

        if(isNetDisponible()){
            new pendingIdeas().execute(conexion,userData.getNombre());
            System.out.println("Subimos todas las ideas que publicaste fuera de linea.");
        }
        else{
            System.out.println("NO Subimos todas las ideas que publicaste fuera de linea.");
        }

        if(isNetDisponible()){
            new getIdeas(this).execute("http://env-4185869.njs.jelastic.vps-host.net/idea");
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    //set icon
                    .setIcon(android.R.drawable.ic_dialog_info)
                    //set title
                    .setTitle("No tienes conexion a internet.")
                    //set message
                    .setMessage("Para ver las ideas que han posteado conectate a internet")
                    //set positive button
                    .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(),"No esperes mucho para conectarte.",Toast.LENGTH_LONG).show();
                        }
                    })
                    .show();
        }
        listaIdeas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String parametro = unitIdea.get(i);
                try {
                    showIdea(parametro);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showIdea(String nombre) throws JSONException {
        JSONObject idea = null;
        for(int i = 0;i<listIdeas.size();i++){
            if(listIdeas.get(i).get("titulo").equals(nombre)){
                idea=listIdeas.get(i);
            }
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(android.R.drawable.ic_dialog_info)
                //set title
                .setTitle(nombre)
                //set message
                .setMessage(idea.getString("cuerpo")+"\n"+"\n"+"Enlaces: "+idea.getString("referencia")+"\n"+"\n"+
                "Contacto: "+idea.getString("contacto")+"\n\n"+"Publicado por: "+idea.get("nombre"))
                //set positive button
                .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Gracias por ver esta idea.",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
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

            userData = new usuario(account.getDisplayName(), account.getEmail(), "");


        } else {
            Toast.makeText(getApplicationContext(), "Has iniciado sesion local.", Toast.LENGTH_SHORT).show();
        }
    }


    private void configOffline() {
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        ((TextView) header.findViewById(R.id.userName)).setText(userData.getNombre());
        ((TextView) header.findViewById(R.id.correoUsuario)).setText(userData.getCorreo());

    }

    private void configOnline() {
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
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
                    Toast.makeText(getApplicationContext(), "not close", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Publicar idea", Toast.LENGTH_LONG).show();
            Bundle data = new Bundle();
            data.putString("correo", userData.getCorreo());
            data.putBoolean("conexion", isNetDisponible());
            data.putString("nombre",userData.getNombre());
            data.putString("idUsuario",userData.getCedula());
            pi = new PublicarIdea();
            pi.setArguments(data);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contexto, pi);
            transaction.commit();


        } else if (id == R.id.inicio) {
            Toast.makeText(this,"Inicio",Toast.LENGTH_LONG).show();
            Intent goToInicio = new Intent(this,IndexActivity.class);
            goToInicio.addFlags(goToInicio.FLAG_ACTIVITY_CLEAR_TOP | goToInicio.FLAG_ACTIVITY_CLEAR_TASK);
            goToInicio.putExtra("Usuario", userData.getNombre());
            goToInicio.putExtra("Correo", userData.getCorreo());
            goToInicio.putExtra("idUsuario", userData.getCedula());
            startActivity(goToInicio);


        } else if (id == R.id.noticias) {
            Toast.makeText(this,"Noticias",Toast.LENGTH_LONG).show();
            Intent goToNoticias = new Intent(this,Noticias.class);
            goToNoticias.addFlags(goToNoticias.FLAG_ACTIVITY_CLEAR_TOP | goToNoticias.FLAG_ACTIVITY_CLEAR_TASK);


            goToNoticias.putExtra("Usuario", userData.getNombre());
            goToNoticias.putExtra("Correo", userData.getCorreo());
            goToNoticias.putExtra("idUsuario", userData.getCedula());

            startActivity(goToNoticias);

        } else if (id == R.id.donaciones) {
            Toast.makeText(this,"Donaciones",Toast.LENGTH_LONG).show();
            donaEco = new donacionesEcologicas();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contexto,donaEco);
            transaction.commit();

        } else if (id == R.id.cerrarSesion) {
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
        }else if(id == R.id.miUbicacion){
            //Aca infla el fragmento con la ubicacion
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void processFinish(String output) {
        this.ideas = output;
        System.out.println("La data:" + this.ideas);
        if(this.ideas == null){
            Toast.makeText(getApplicationContext(),"No estas en linea.",Toast.LENGTH_LONG).show();
        }
        else{
            do {
                String obj = null;
                int inicio = this.ideas.indexOf("{");
                int fin = this.ideas.indexOf("}");
                try{
                    System.out.println(this.ideas.substring(inicio, fin + 1));
                    obj = this.ideas.substring(inicio, fin + 1);
                    this.ideas = this.ideas.replace(this.ideas.substring(inicio, fin + 1), "");
                    //System.out.println(this.ideas);
                    this.ideasJson = new JSONObject(obj);
                    listIdeas.add(ideasJson);
                }catch (Exception e){
                    Toast.makeText(this,"No hay registros online aun.",Toast.LENGTH_LONG).show();
                }
            }while (this.ideas.endsWith("}]"));
            setElementsOnIndex();
        }


    }

    public void setElementsOnIndex(){

        for (int i = 0; i < listIdeas.size(); i++) {
            String Titulo = "";
            try {
                Titulo = listIdeas.get(i).get("titulo").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            unitIdea.add(Titulo);
            System.out.println("todo ok");
            ArrayAdapter listAdapter = new ArrayAdapter(this,  android.R.layout.simple_expandable_list_item_1, this.unitIdea);
            listaIdeas.setAdapter(listAdapter);
        }
    }
}
