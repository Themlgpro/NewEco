package com.proyecto.appsmoviles.neweco;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.proyecto.appsmoviles.neweco.Mapping.usuario;

public class Noticias extends AppCompatActivity implements  national.OnFragmentInteractionListener,ecoportal.OnFragmentInteractionListener {

    private WebView page;
    private TabLayout menu;
    private national na;
    private ecoportal eco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        menu = (TabLayout) findViewById(R.id.menu);
        menu.setTabMode(TabLayout.MODE_SCROLLABLE);
        irNational();

        menu.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.equals(menu.getTabAt(1))){
                    Toast.makeText(getApplicationContext(),"crear", Toast.LENGTH_SHORT).show();
                    irEcoportal();
                }
                else if(tab.equals(menu.getTabAt(0))){
                    Toast.makeText(getApplicationContext(),"National", Toast.LENGTH_SHORT).show();
                    irNational();
                }else if (tab.equals(menu.getTabAt(2))){
                    Toast.makeText(getApplicationContext(),"volver", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Usuario", getIntent().getExtras().getString("Usuario"));
                    intent.putExtra("Correo", getIntent().getExtras().getString("Correo"));
                    intent.putExtra("idUsuario", getIntent().getExtras().getString("idUsuario"));
                    startActivity(intent);

                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }




    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void irEcoportal() {
        eco = new ecoportal();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.noticiaas,eco);
        transaction.commit();
    }


    private void irNational() {
        na = new national();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.noticiaas,na);
        transaction.commit();
    }
}
