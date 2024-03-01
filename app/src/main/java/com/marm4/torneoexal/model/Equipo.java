package com.marm4.torneoexal.model;

import android.net.Uri;

import java.util.List;

public class Equipo {

    private String nombre;
    private Uri escudo;
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
        jugadores.add(jugador);
    }

    public void addPartido(Partido partido){
        partidos.add(partido);
    }
}
