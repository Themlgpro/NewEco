package com.proyecto.appsmoviles.neweco;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.proyecto.appsmoviles.neweco.Database.NewEco;
import com.proyecto.appsmoviles.neweco.Mapping.usuario;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;




public class LoginActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener, RegistroLocal.OnFragmentInteractionListener {

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    public  static final int SIGN_IN_CODE =777;
    private Button ingresar, registrar;
    private EditText user, pass;
    private NewEco conexion;
    private SQLiteDatabase bd;
    private usuario obj;
    private RegistroLocal reg;
    private boolean bandera;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        AdSize adSize= new AdSize(321, 49);
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();



       mAdView.loadAd(adRequest);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail()
                .build();
        bandera = false;
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();



        signInButton = (SignInButton)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });

        ingresar = (Button) findViewById(R.id.logIn);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.contraseña);



        conexion= new NewEco(this,"NewEco",null,1);
    }

    public void registrarUsuario (View view){
        reg = new RegistroLocal();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contexto,reg);
        transaction.commit();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void logIn(View view) {
        String u,p;
        u = user.getText().toString().trim();
        p = pass.getText().toString().trim();
        if(u.equals("") ||p.equals(""))
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("Ingresa usuario y contraseña")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create();
            builder.show();
        }
        else {
            SQLiteDatabase db = conexion.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM usuario WHERE cedula='"+u+"' AND password='"+MD5.getMD5(p)+"';",null);
            if(c.moveToFirst()){
                user.setText("");
                pass.setText("");
                //Lanzamos el index y le enviamos el objeto
                Intent logIn = new Intent(this, IndexActivity.class);
                logIn.addFlags(logIn.FLAG_ACTIVITY_CLEAR_TOP | logIn.FLAG_ACTIVITY_CLEAR_TASK);
                //Envio de la data
                c.moveToLast();
                int idVenta = c.getInt(0);
                logIn.putExtra("Usuario", c.getString(1));
                logIn.putExtra("Correo", c.getString(3));
                logIn.putExtra("idUsuario", c.getString(0));
                System.out.println(c.getString(0));
                logIn.putExtra("bandera", false);
                startActivity(logIn);

            }else{
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.setMessage("El usuario no existe");
                dialog.show();
            }
        }

    }

    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlesSignInResult(result);

        }

    }
    private void goMainScreen() {
        Intent intent = new Intent(this, IndexActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("Usuario", "");
        intent.putExtra("Correo", "");
        intent.putExtra("idUsuario", "");

        startActivity(intent);
    }


    //Validacion
    private void handlesSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            bandera =true;
            Toast.makeText(this,"Has iniciado sesion con Google.",Toast.LENGTH_SHORT).show();
            goMainScreen();
        }else {
            Toast.makeText(this,"no se pudo iniciar sesion",Toast.LENGTH_SHORT).show();
        }
    }

    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }



}
