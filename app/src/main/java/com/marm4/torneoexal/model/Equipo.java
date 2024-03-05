package com.marm4.torneoexal.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private String id;
    private String nombre;
    private Uri escudo;
    private String urlEscudo;
    private List<Jugador> jugadores;
    private List<Partido> partidos;

    public Equipo() {
    }

    public Equipo(String nombre, Uri escudo, List<Jugador> jugadores, List<Partido> partidos) {
        this.nombre = nombre;
        this.escudo = escudo;
        this.jugadores = jugadores;
        this.partidos = partidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Uri getEscudo() {
        return escudo;
    }

    public void setEscudo(Uri escudo) {
        this.escudo = escudo;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setJugadores(List<Jugador> jugadores){
        this.jugadores = jugadores;
    }

    public void setPartidos(List<Partido> partidos){
        this.partidos = partidos;
    }

    public void addJugador(Jugador jugador){
        if(jugadores == null)
            jugadores = new ArrayList<>();
        jugadores.add(jugador);
    }

    public void addPartido(Partido partido){
        if(partidos == null)
            partidos = new ArrayList<>();
        partidos.add(partido);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlEscudo() {
        return urlEscudo;
    }

    public void setUrlEscudo(String urlEscudo) {
        this.urlEscudo = urlEscudo;
    }

    public Jugador buscarJugadorPorId(String id){
        for(Jugador j : jugadores){
            if(j.getId().equals(id))
                return j;
        }
        return null;
    }
}
