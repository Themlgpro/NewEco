package com.proyecto.appsmoviles.neweco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.proyecto.appsmoviles.neweco.Database.NewEco;
import com.proyecto.appsmoviles.neweco.Database.postIdeas;
import com.proyecto.appsmoviles.neweco.Mapping.idea;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicarIdea.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicarIdea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicarIdea extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String correo, nombre, idUser;
    private Boolean online;

    private EditText  titulo, referencia, cuerpo;
    private Button publicar;
    private NewEco conexion;
    private SQLiteDatabase db;

    private OnFragmentInteractionListener mListener;

    public PublicarIdea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PublicarIdea.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicarIdea newInstance(String param1, String param2) {
        PublicarIdea fragment = new PublicarIdea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        conexion = new NewEco(getContext(), "NewEco", null, 1);
        db = conexion.getWritableDatabase();
        this.correo =getArguments().getString("correo");
        this.online =getArguments().getBoolean("conexion");
        this.nombre =getArguments().getString("nombre");
        this.idUser = getArguments().getString("idUsuario");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View indexView = inflater.inflate(R.layout.fragment_publicar_idea, container, false);

        titulo = (EditText) indexView.findViewById(R.id.titulo);
        cuerpo = (EditText) indexView.findViewById(R.id.cuerpo);
        referencia = (EditText) indexView.findViewById(R.id.referencia);

        publicar = (Button) indexView.findViewById(R.id.publicaIdea);

       publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postIdea(view);
            }
        });

        return indexView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void postIdea(View view){
        //Preparando la data local
        String tittle,content,reference,contact;
        tittle = titulo.getText().toString().trim();
        content = cuerpo.getText().toString().trim();
        reference = referencia.getText().toString().trim();
        contact = correo;
        Toast.makeText(getActivity(),"Entro al metodo.",Toast.LENGTH_LONG).show();
        //Aqui se debe validar la conectividad a internet.
        if(online){
            Toast.makeText(getActivity(),"Post Online",Toast.LENGTH_LONG).show();
            postIdeaOnline(tittle,content,reference,contact,nombre);
        }
        else{
            if (titulo.getText().toString().trim().length() == 0
                    ||cuerpo.getText().toString().trim().length() == 0
                    ||referencia.getText().toString().trim().length() == 0){

                Toast.makeText(getActivity(),"Los campos Titulo, cuerpo y referencia son obligatorios.",Toast.LENGTH_LONG).show();
            }else{

                System.out.println(this.idUser);
                //Sentencia y post local
                String query = "insert into idea (usuario_cedula,titulo,cuerpo,referencia,contacto) values ('" + this.idUser + "','" + tittle + "','" + content+ "'," +
                        "'" + reference + "','" + contact + "');";
                db.execSQL(query);
                Toast.makeText(getActivity(),"Hemos publicado tu idea.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postIdeaOnline(final String tittle, final String content, final String reference, final String contact, final String nombre) {
        new postIdeas().execute(new idea(nombre,contact,content,tittle,reference));
    }


}
