package com.proyecto.appsmoviles.neweco.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLData;

/**
 * Created by carlo on 3/06/2018.
 */

public class NewEco extends SQLiteOpenHelper{
    String query="create table usuario " + "(cedula TEXT PRIMARY KEY,nombre TEXT, password TEXT, correo TEXT);";
    String query2="create table idea" + "(idIdea INTEGER PRIMARY KEY AUTOINCREMENT,usuario_cedula TEXT,titulo TEXT,cuerpo TEXT,referencia TEXT, contacto TEXT, FOREIGN KEY(usuario_cedula) REFERENCES usuario(cedula)ON DELETE CASCADE ON UPDATE CASCADE);";
    String query3="CREATE INDEX idea_FKIndex1 ON idea (usuario_cedula);";

    public NewEco(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.execSQL(query2);
        sqLiteDatabase.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}



