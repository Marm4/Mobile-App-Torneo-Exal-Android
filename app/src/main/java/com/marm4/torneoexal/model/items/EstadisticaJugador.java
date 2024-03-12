package com.marm4.torneoexal.model.items;

import android.net.Uri;

public class EstadisticaJugador {

    private String nombre;
    private Uri logoEquipo;
    private String goles;
    private String amarillas;
    private String rojas;
    private String mvp;
    private String posicion;

    public EstadisticaJugador(String nombre, Uri logoEquipo, String goles, String amarillas, String rojas, String mvp, String podio) {
        this.nombre = nombre;
        this.logoEquipo = logoEquipo;
        this.goles = goles;
        this.amarillas = amarillas;
        this.rojas = rojas;
        this.mvp = mvp;
        this.posicion = podio;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public EstadisticaJugador() {
        goles = "0";
        amarillas = "0";
        rojas = "0";
        mvp = "0";
        posicion = "no";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGoles() {
        return goles;
    }

    public void setGoles(String goles) {
        this.goles = goles;
    }

    public Uri getLogoEquipo() {
        return logoEquipo;
    }

    public void setLogoEquipo(Uri logoEquipo) {
        this.logoEquipo = logoEquipo;
    }

    public String getAmarillas() {
        return amarillas;
    }

    public void setAmarillas(String amarillas) {
        this.amarillas = amarillas;
    }

    public String getRojas() {
        return rojas;
    }

    public void setRojas(String rojas) {
        this.rojas = rojas;
    }

    public String getMvp() {
        return mvp;
    }

    public void setMvp(String mvp) {
        this.mvp = mvp;
    }
}
