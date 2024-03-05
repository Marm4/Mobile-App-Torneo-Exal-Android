package com.marm4.torneoexal.global;

import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Partido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Globals {

    private Equipo equipo;
    private List<Equipo> equipos;
    private Partido partido;
    private Fixture fixture;


    private static Globals instance;

    public Globals() {
        equipos = new ArrayList<>();
    }

    public static synchronized Globals getInstance(){
        if(instance == null)
            instance = new Globals();
        return instance;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public void agregarEquipos(Equipo e1, Equipo e2){
        if(e1==null || e2==null)
            return;
        equipos.add(e1);
        equipos.add(e2);
    }

    public List<Equipo> getListaEquipos(){
        return equipos;
    }

    public void deleteEquipos(){
        equipos.clear();
        partido = null;
        fixture = null;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }
}
