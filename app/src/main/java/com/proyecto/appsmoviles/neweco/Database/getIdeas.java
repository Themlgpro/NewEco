package com.proyecto.appsmoviles.neweco.Database;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.proyecto.appsmoviles.neweco.Mapping.idea;
import com.proyecto.appsmoviles.neweco.Mapping.usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by carlo on 10/06/2018.
 */

public class getIdeas extends AsyncTask<String,String,String>{
    private idea auxI;
    private usuario auxU;
    private Vector<idea> ideas;
    private String data;
    AsyncResponse delegado = null;

    public getIdeas(AsyncResponse delegado) {
        this.delegado = delegado;
    }

    @Override
    protected String doInBackground(String... params) {
        // Create URL
        URL traeIdeas = null;
        BufferedReader lector = null;
        HttpURLConnection getIdeas = null;
        try {
            traeIdeas = new URL(params[0]);
            getIdeas = (HttpURLConnection) traeIdeas.openConnection();
            getIdeas.connect();
            if (getIdeas.getResponseCode() == 200) {
                InputStream responseBody = getIdeas.getInputStream();
                lector = new BufferedReader(new InputStreamReader(responseBody));
                StringBuffer stringLector = new StringBuffer();
                String linea;

                while((linea = lector.readLine())!= null){
                    stringLector.append(linea);
                }
                this.data = stringLector.toString();
                System.out.println(this.data);
                return this.data;

            } else {
                // Error handling code goes here
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ideas!=null){
                getIdeas.disconnect();
            }
            if(lector!=null){
                try {
                    lector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        delegado.processFinish(result);
    }



    public Vector<idea> getIdeas() throws IOException {


        return this.ideas;
    }

    public idea getAuxI() {
        return auxI;
    }

    public void setAuxI(idea auxI) {
        this.auxI = auxI;
    }

    public usuario getAuxU() {
        return auxU;
    }

    public void setAuxU(usuario auxU) {
        this.auxU = auxU;
    }

    public void setIdeas(Vector<idea> ideas) {
        this.ideas = ideas;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
