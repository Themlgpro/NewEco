package com.proyecto.appsmoviles.neweco;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.proyecto.appsmoviles.neweco.Database.NewEco;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroLocal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroLocal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroLocal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText name,id,pass1,pass2,email;
    private Button registrar;
    View indexView;

    private NewEco conexion;
    private SQLiteDatabase bd;
    private OnFragmentInteractionListener mListener;

    public RegistroLocal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroLocal.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroLocal newInstance(String param1, String param2) {
        RegistroLocal fragment = new RegistroLocal();
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

        conexion= new NewEco(getContext(),"NewEco",null,1);
        bd = conexion.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        indexView = inflater.inflate(R.layout.fragment_registro_local, container, false);

        id = (EditText) indexView.findViewById(R.id.cedula);
        pass1 = (EditText) indexView.findViewById(R.id.pass);
        pass2 = (EditText) indexView.findViewById(R.id.passConfirm);
        email = (EditText) indexView.findViewById(R.id.correo);
        name = (EditText) indexView.findViewById(R.id.nombre);
        registrar = (Button) indexView.findViewById(R.id.registrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regUser(view);
            }
        });

        return indexView;


    }

    public void regUser(View view){
        if(id.getText().toString().trim().equals("")||pass1.getText().toString().trim().equals("")||
                pass2.getText().toString().trim().equals("")||
                email.getText().toString().trim().equals("")||
                name.getText().toString().trim().equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Todos los campos son obligatorios.")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create();
            builder.show();
        }
        else {
            try {

                if(!pass1.getText().toString().trim().equals(pass2.getText().toString().trim())){
                    Toast.makeText(getActivity(),"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }else{
                    //Inicializar variables
                    String userName,idUser,contact,password;

                    userName = name.getText().toString().trim();
                    idUser = id.getText().toString().trim();
                    password = pass1.getText().toString().trim();
                    contact = email.getText().toString().trim();

                    String query = "insert into usuario (cedula, nombre,password,correo) values ('" + idUser + "','" +
                             userName+ "','" +MD5.getMD5(password) + "','"+ contact + "');";

                    bd.execSQL(query);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent ir = new Intent(getActivity(), LoginActivity.class);
                            ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(ir);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setMessage("Usuario registrado satisfactoriamente :)");
                    dialog.show();
                }
            }catch (Exception e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent ir = new Intent(getActivity(), LoginActivity.class);
                        ir.addFlags(ir.FLAG_ACTIVITY_CLEAR_TOP | ir.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(ir);
                        // User clicked OK button
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setMessage("Ya existe un usuario con esta cedula");
                dialog.show();
                id.setText("");
                pass1.setText("");
                pass2.setText("");
                email.setText("");
                name.setText("");
            }
        }
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
}
