package com.proyecto.appsmoviles.neweco;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.nsd.NsdManager;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.proyecto.appsmoviles.neweco.Database.NewEco;
import com.proyecto.appsmoviles.neweco.Mapping.usuario;
import com.proyecto.appsmoviles.neweco.RegistroLocal.OnFragmentInteractionListener;


public class LoginActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private Button ingresar, registrar;
    private EditText user, pass;
    private NewEco conexion;
    private SQLiteDatabase bd;
    private usuario obj;
    private RegistroLocal reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    public void logIn(View view) {
        String u,p;
        u = user.getText().toString().trim(); p = pass.getText().toString().trim();
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
