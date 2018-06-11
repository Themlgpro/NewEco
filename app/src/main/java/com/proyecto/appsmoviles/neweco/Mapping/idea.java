package com.proyecto.appsmoviles.neweco.Mapping;

/**
 * Created by carlo on 10/06/2018.
 */

public class idea {
    private String nombre,contacto,cuerpo,titulo,referencia;

    public idea(String nombre, String contacto, String cuerpo, String titulo, String referencia) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.cuerpo = cuerpo;
        this.titulo = titulo;
        this.referencia = referencia;
    }

    public idea() {
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
