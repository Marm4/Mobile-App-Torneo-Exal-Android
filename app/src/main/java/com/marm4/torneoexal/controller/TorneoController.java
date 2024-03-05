package com.marm4.torneoexal.controller;

import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.service.TorneoService;

public class TorneoController {

    private TorneoService equipoService;

    public TorneoController(){
        equipoService = new TorneoService();
    }

    public void guardarEquipo(Equipo equipo, TorneoNotificacion listener){
        equipoService.guardarEquipo(equipo, listener);
    }

    public void cargarEquipos(TorneoNotificacion listener){
        equipoService.devolverEquipos(listener);
    }

    public void guardarFixture(Fixture fixture, TorneoNotificacion listener){
        equipoService.guardarFixture(fixture, listener);
    }

    public void cargarFixture(TorneoNotificacion listener){
        equipoService.devolverFixture(listener);
    }
}
