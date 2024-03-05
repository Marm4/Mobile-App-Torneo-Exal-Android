package com.marm4.torneoexal.model;

import java.util.ArrayList;
import java.util.List;

public class Partido{
    private String diaHora;
    private String equipoUnoID;
    private String equipoDosID;
    private Jugador mvp;
    private String fixtureId;
    private List<Tarjeta> tarjetas;
    private List<String> golesEquipoUno;
    private List<String> golesEquipoDos;

    public Partido(String diaHora, String equipoUnoID, String equipoDosID, Jugador mvp, String fixtureId, List<Tarjeta> tarjetas, List<String> golesEquipoUno, List<String> golesEquipoDos) {
        this.diaHora = diaHora;
        this.equipoUnoID = equipoUnoID;
        this.equipoDosID = equipoDosID;
        this.mvp = mvp;
        this.fixtureId = fixtureId;
        this.tarjetas = tarjetas;
        this.golesEquipoUno = golesEquipoUno;
        this.golesEquipoDos = golesEquipoDos;
    }

    public Partido() {
    }

    public String getDiaHora() {
        return diaHora;
    }

    public void setDiaHora(String diaHora) {
        this.diaHora = diaHora;
    }

    public String getEquipoUno() {
        return equipoUnoID;
    }

    public void setEquipoUno(String equipoUno) {
        this.equipoUnoID = equipoUno;
    }

    public String getEquipoDos() {
        return equipoDosID;
    }

    public void setEquipoDos(String equipoDos) {
        this.equipoDosID = equipoDos;
    }

    public Jugador getMvp() {
        return mvp;
    }

    public void setMvp(Jugador mvp) {
        this.mvp = mvp;
    }

    public String getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(String fixtureId) {
        this.fixtureId = fixtureId;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public void addTarjeta (Tarjeta tarjeta){
        if(tarjetas == null)
            tarjetas = new ArrayList<>();
        tarjetas.add(tarjeta);
    }

    public List<String> getGolesEquipoUno() {
        return golesEquipoUno;
    }

    public void setGolesEquipoUno(List<String> golesEquipoUno) {
        this.golesEquipoUno = golesEquipoUno;
    }

    public List<String> getGolesEquipoDos() {
        return golesEquipoDos;
    }

    public void setGolesEquipoDos(List<String> golesEquipoDos) {
        this.golesEquipoDos = golesEquipoDos;
    }

    public void addGolEquipoUno(String idJugador){
        if(golesEquipoUno == null)
            golesEquipoUno = new ArrayList<>();
        golesEquipoUno.add(idJugador);
    }

    public void removeGolEquipoUno(String idJugador){
        if(golesEquipoUno == null)
            return;
        golesEquipoUno.remove(idJugador);
    }

    public void removeGolEquipoDos(String idJugador){
        if(golesEquipoDos == null)
            return;
        golesEquipoDos.remove(idJugador);
    }

    public void addGolEquipoDos(String idJugador){
        if(golesEquipoDos == null)
            golesEquipoDos = new ArrayList<>();
        golesEquipoDos.add(idJugador);
    }

    public int getResultadoUno(){
        if(golesEquipoUno == null)
            return 0;
        return golesEquipoUno.size();
    }
    public int getResultadoDos(){
        if(golesEquipoDos==null)
            return 0;
        return golesEquipoDos.size();
    }

    public void removeTarjeta(Tarjeta tarjeta){
        if(tarjetas == null)
            return;
        tarjetas.remove(tarjeta);
    }

}
