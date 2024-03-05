package com.marm4.torneoexal.model;

public class EstadisticasPartido {

    private String nombreJugadorEquipo1;
    private String nombreJugadorEquipo2;
    private Tarjeta tarjeta1;
    private Tarjeta tarjeta2;

    public EstadisticasPartido(String nombreJugadorEquipo1, String nombreJugadorEquipo2) {
        this.nombreJugadorEquipo1 = nombreJugadorEquipo1;
        this.nombreJugadorEquipo2 = nombreJugadorEquipo2;
    }

    public EstadisticasPartido() {
        nombreJugadorEquipo1 = "";
        nombreJugadorEquipo2 = "";
    }

    public String getNombreJugadorEquipo1() {
        return nombreJugadorEquipo1;
    }

    public String getNombreJugadorEquipo2() {
        return nombreJugadorEquipo2;
    }

    public void setNombreJugadorEquipo1(String nombreJugadorEquipo1) {
        this.nombreJugadorEquipo1 = nombreJugadorEquipo1;
    }

    public void setNombreJugadorEquipo2(String nombreJugadorEquipo2) {
        this.nombreJugadorEquipo2 = nombreJugadorEquipo2;
    }

    public Tarjeta getTarjeta1() {
        return tarjeta1;
    }

    public void setTarjeta1(Tarjeta tarjeta) {
        this.tarjeta1 = tarjeta;
    }

    public Tarjeta getTarjeta2() {
        return tarjeta2;
    }

    public void setTarjeta2(Tarjeta tarjeta) {
        this.tarjeta2 = tarjeta;
    }
}
