package com.proyecto.appsmoviles.neweco.Database;

import android.os.AsyncTask;
import android.os.StrictMode;


import com.proyecto.appsmoviles.neweco.Mapping.idea;


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
 * Created by carlo on 10/06/2018.
 */

public class postIdeas extends AsyncTask<idea,String,String> {
    @Override
    protected String doInBackground(idea... params) {
        try {
            URL url = new URL("http://env-4185869.njs.jelastic.vps-host.net/addI");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            List<AbstractMap.SimpleEntry> parametros = new ArrayList<AbstractMap.SimpleEntry>();
            parametros.add(new AbstractMap.SimpleEntry("titulo", params[0].getTitulo()));
            parametros.add(new AbstractMap.SimpleEntry("cuerpo",params[0].getCuerpo()));
            parametros.add(new AbstractMap.SimpleEntry("referencia", params[0].getReferencia()));
            parametros.add(new AbstractMap.SimpleEntry("contacto", params[0].getContacto()));
            parametros.add(new AbstractMap.SimpleEntry("nombre",params[0].getNombre()));

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
