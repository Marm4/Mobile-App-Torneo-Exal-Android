package com.marm4.torneoexal.model;

import java.util.ArrayList;
import java.util.List;

public class Fixture {
    private String id;
    private String fechaNro;
    private List<Partido> partidos;

    public Fixture(String fechaNro, List<Partido> partidos) {
        this.fechaNro = fechaNro;
        this.partidos = partidos;
    }

    public Fixture(String id, String fechaNro, List<Partido> partidos) {
        this.id = id;
        this.fechaNro = fechaNro;
        this.partidos = partidos;
    }

    public Fixture() {
    }

    public String getFechaNro() {
        return fechaNro;
    }

    public void setFechaNro(String fechaNro) {
        this.fechaNro = fechaNro;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }

    public void addPartidos(Partido partido){
        if(partidos==null)
            partidos =new ArrayList<>();

        partidos.add(partido);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
