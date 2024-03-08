package com.marm4.torneoexal.model.items;

import com.marm4.torneoexal.model.Equipo;

public class EquipoTabla {
    private Equipo equipo;
    private String posicion;
    private String partidos;
    private String ganados;
    private String empatados;
    private String perdidos;
    private String golesFavor;
    private String golesContra;
    private String diferenciaGoles;
    private String puntos;

    public EquipoTabla() {
    }

    public String getGanados() {
        return ganados;
    }

    public void setGanados(String ganados) {
        this.ganados = ganados;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getPartidos() {
        return partidos;
    }

    public void setPartidos(String partidos) {
        this.partidos = partidos;
    }


    public String getEmpatados() {
        return empatados;
    }

    public void setEmpatados(String empatados) {
        this.empatados = empatados;
    }

    public String getPerdidos() {
        return perdidos;
    }

    public void setPerdidos(String perdidos) {
        this.perdidos = perdidos;
    }

    public String getGolesFavor() {
        return golesFavor;
    }

    public void setGolesFavor(String golesFavor) {
        this.golesFavor = golesFavor;
    }

    public String getGolesContra() {
        return golesContra;
    }

    public void setGolesContra(String golesContra) {
        this.golesContra = golesContra;
    }

    public String getDiferenciaGoles() {
        return diferenciaGoles;
    }

    public void setDiferenciaGoles() {
        int diferencia = Integer.parseInt(golesFavor) - Integer.parseInt(golesContra);
        this.diferenciaGoles = String.valueOf(diferencia);
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }
}
