package com.proyecto.appsmoviles.neweco;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginActivity extends AppCompatActivity {

    private Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ingresar = (Button) findViewById(R.id.logIn);

    }

    public void logIn(View view) {
        Intent logIn = new Intent(this, IndexActivity.class);
        logIn.addFlags(logIn.FLAG_ACTIVITY_CLEAR_TOP | logIn.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logIn);
    }
}
