package com.proyecto.appsmoviles.neweco.Mapping;

/**
 * Created by carlo on 6/06/2018.
 */

public class usuario {

    private String nombre,correo,cedula;


    public usuario(String nombre, String correo, String cedula) {
        this.nombre = nombre;
        this.correo = correo;
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}
