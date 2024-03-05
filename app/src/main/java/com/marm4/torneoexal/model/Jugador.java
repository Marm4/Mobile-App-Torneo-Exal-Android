package com.marm4.torneoexal.model;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String id;
    private String nombre;
    private int goles;
    private List<Tarjeta> tarjetas;
    private int partidosJugados;


    public Jugador() {
    }

    public Jugador(String id, String nombre, int goles, List<Tarjeta> tarjetas, int partidosJugados) {
        this.id = id;
        this.nombre = nombre;
        this.goles = goles;
        this.tarjetas = tarjetas;
        this.partidosJugados = partidosJugados;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getGoles() {
        return goles;
    }

    public void setGoles(int goles) {
        this.goles = goles;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public void addTarjeta(Tarjeta tarjeta){
        if(tarjetas == null)
            tarjetas = new ArrayList<>();
        tarjetas.add(tarjeta);
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public void addGol(){
        goles++;
    }

    public void removeGol(){
        goles--;
    }

    public void removeTarjeta(Tarjeta tarjeta){
        if(tarjetas == null)
            return;
        tarjetas.remove(tarjeta);
    }
}
