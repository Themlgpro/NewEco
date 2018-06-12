package com.proyecto.appsmoviles.neweco.Database;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlo on 11/06/2018.
 */

public class pendingIdeas extends AsyncTask {
    private NewEco conexion;
    protected Object doInBackground(Object[] objects) {
        conexion = (NewEco) objects[0];
        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM idea;",null);
        if(c.moveToFirst()){
            do{
                try {
                    URL url = new URL("http://env-4185869.njs.jelastic.vps-host.net/addI");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    List<AbstractMap.SimpleEntry> parametros = new ArrayList<AbstractMap.SimpleEntry>();
                    parametros.add(new AbstractMap.SimpleEntry("titulo", c.getString(2)));
                    parametros.add(new AbstractMap.SimpleEntry("cuerpo",c.getString(3)));
                    parametros.add(new AbstractMap.SimpleEntry("referencia", c.getString(4)));
                    parametros.add(new AbstractMap.SimpleEntry("contacto", c.getString(5)));
                    parametros.add(new AbstractMap.SimpleEntry("nombre",objects[1]));

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getQuery(parametros));
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    //Manejemos la respuesta

                    StringBuilder sb = new StringBuilder();
                    int HttpResult = conn.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println("" + sb.toString());
                    } else {
                        System.out.println(conn.getResponseMessage());
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }while(c.moveToNext());
            db = conexion.getWritableDatabase();
            String delete = "DELETE FROM idea WHERE 1;";
            db.execSQL(delete);
        }
        return null;
    }
    private String getQuery(List<AbstractMap.SimpleEntry> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (AbstractMap.SimpleEntry pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode((String) pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
