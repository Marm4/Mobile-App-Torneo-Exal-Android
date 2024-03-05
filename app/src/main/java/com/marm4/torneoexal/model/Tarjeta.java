package com.marm4.torneoexal.model;

public class Tarjeta {
    private Boolean amarilla;
    private String jugadorId;

    public Tarjeta(Boolean amarilla, String jugadorId) {
        this.amarilla = amarilla;
        this.jugadorId = jugadorId;
    }

    public Tarjeta() {
    }

    public Boolean getAmarilla() {
        return amarilla;
    }

    public void setAmarilla(Boolean amarilla) {
        this.amarilla = amarilla;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }
}
