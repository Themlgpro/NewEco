package com.proyecto.appsmoviles.neweco.Database;

import android.os.AsyncTask;
import android.os.StrictMode;

import com.proyecto.appsmoviles.neweco.Mapping.idea;
import com.proyecto.appsmoviles.neweco.Mapping.usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by carlo on 10/06/2018.
 */

public class postIdeas extends AsyncTask<idea,String,String> {
    @Override
    protected String doInBackground(idea... params) {
        // Create URL
        //putIdea
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        System.out.println("AsyncTask");
        URL apiEndpoint = null;
        // Create connection
        try {
            JSONObject postDataParams = new JSONObject();

            apiEndpoint = new URL("http://env-4185869.njs.jelastic.vps-host.net/addI");
            HttpURLConnection myConnection = (HttpURLConnection) apiEndpoint.openConnection();
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestMethod("POST");
            myConnection.setRequestProperty("Content-Type", "application/json");
            myConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            myConnection.setRequestProperty("Accept", "application/json");

            try {
                postDataParams.put("titulo", params[0].getTitulo());
                postDataParams.put("cuerpo", params[0].getCuerpo());
                postDataParams.put("referencia", params[0].getReferencia());
                postDataParams.put("contacto", params[0].getContacto());
                postDataParams.put("nombre", params[0].getNombre());
                System.out.println(postDataParams.toString());
                myConnection.connect();
                OutputStreamWriter posting;

                byte[] outputBytes = postDataParams.toString().getBytes("UTF-8");
                OutputStream os = myConnection.getOutputStream();
                os.write(outputBytes);
                //posting = new OutputStreamWriter(myConnection.getOutputStream());
                //posting.write(postDataParams.toString().getBytes("UTF-8"));
                os.flush ();
                os.close ();

               //Manejemos la respuesta

                StringBuilder sb = new StringBuilder();
                int HttpResult = myConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(myConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println("" + sb.toString());
                } else {
                    System.out.println(myConnection.getResponseMessage());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
