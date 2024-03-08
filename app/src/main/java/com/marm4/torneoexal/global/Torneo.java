package com.marm4.torneoexal.global;

import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.Tarjeta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Torneo {
    private List<Fixture> fixtures;
    private List<Equipo> equipos;

    private static Torneo instance;

    private Torneo() {
        fixtures = new ArrayList<>();
        equipos = new ArrayList<>();
    }

    public static synchronized Torneo getInstance(){
        if(instance == null)
            instance = new Torneo();
        return instance;
    }


    public List<Fixture> getFixtures() {
        return fixtures;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void addFixtures(Fixture fixture){
        fixtures.add(fixture);
    }

    public void addEquipo(Equipo equipo){
        equipos.add(equipo);
    }

    public Equipo getEquipoId(String id){
        Equipo equipo = null;
        for(Equipo equipoAux : equipos){
            if(equipoAux.getId().equals(id))
                equipo = equipoAux;
        }
        return equipo;
    }

    public Fixture getFixtureByFecha(String fecha){
        Fixture fixture = null;
        for(Fixture fixtureAux : fixtures){
            if(fixtureAux.getFechaNro().equals(fecha))
                fixture = fixtureAux;
        }
        return fixture;
    }

    public Partido getPartidoByEquipos(String id1, String id2){
        Partido partido = null;
        for(Fixture f : fixtures){
            for(Partido p : f.getPartidos()){
                if(p.getEquipoUno().equals(id1) && p.getEquipoDos().equals(id2)){
                    partido = p;
                    Globals.getInstance().setPartido(p);
                    Globals.getInstance().setFixture(f);
                }
                else if (p.getEquipoUno().equals(id2) && p.getEquipoDos().equals(id1)) {
                    partido = p;
                    Globals.getInstance().setPartido(p);
                    Globals.getInstance().setFixture(f);
                }
            }
        }
        return partido;
    }

    public String getPuntosPorId(String id){
        int puntos = 0;
        for(Fixture fixture : fixtures){
            for(Partido partido : fixture.getPartidos()){
                if(compararFechas(partido.getDiaHora())){
                    int golesUno = 0;
                    int golesDos = 0;
                    if(partido.getGolesEquipoUno()!=null)
                        golesUno = partido.getGolesEquipoUno().size();
                    if(partido.getGolesEquipoDos()!=null)
                        golesDos = partido.getGolesEquipoDos().size();

                    if(partido.getEquipoUno().equals(id))
                        puntos = puntos + getPuntos(golesUno, golesDos);

                    else if(partido.getEquipoDos().equals(id))
                        puntos = puntos + getPuntos(golesDos, golesUno);
                }

            }
        }
        return String.valueOf(puntos);
    }

    private int getPuntos(int i, int j){
        int puntos = 0;
        if(i==j)
            puntos = 1;
        else if(i>j)
            puntos  = 3;
        return puntos;
    }

    public String getPartidosJugador(String id){
        int partidos = 0;
        for(Fixture fixture : fixtures){
            for(Partido partido : fixture.getPartidos()){
                if(partido.getEquipoUno().equals(id) || partido.getEquipoDos().equals(id)){
                    if(compararFechas(partido.getDiaHora()))
                        partidos++;

                }
            }
        }
        return String.valueOf(partidos);
    }

    public Boolean compararFechas(String selectedDateTimeString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date fechaActual = new Date();
        Boolean retorno = false;
        try {
            Date fechaSeleccionada = dateFormat.parse(selectedDateTimeString);

            if (fechaActual.after(fechaSeleccionada)) {
                retorno = true;
            } else if (fechaActual.before(fechaSeleccionada)) {
                retorno = false;
            } else {
                retorno = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return retorno;
    }

    public String tarjetasAmarillasJugador(Jugador jugador){
        int amarillas = 0;
        if(jugador.getTarjetas() == null)
            return String.valueOf(amarillas);
        else
            for(Tarjeta tarjeta : jugador.getTarjetas())
                if(tarjeta.getAmarilla())
                    amarillas++;

       return String.valueOf(amarillas);
    }

    public String tarjetasRojasJugador(Jugador jugador){
        int rojas = 0;
        if(jugador.getTarjetas() == null)
            return String.valueOf(rojas);
        else
            for(Tarjeta tarjeta : jugador.getTarjetas())
                if(!tarjeta.getAmarilla())
                    rojas++;

        return String.valueOf(rojas);
    }

    public String[] getFechaString(String fechaCompleta) throws ParseException {
        String[] fecha = new String[2];
        if(fechaCompleta.isEmpty()){
            fecha[0] = "";
            fecha[1] = "";
            return fecha;
        }

        String[] partes = fechaCompleta.split(" ");
        String fechaString = partes[0];
        String horaString = partes[1];

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = dateFormat.parse(fechaString);

        SimpleDateFormat nuevoFormato = new SimpleDateFormat("EEEE dd/MM", Locale.getDefault());
        String fechaFormateada = nuevoFormato.format(date);

        String primerCaracter = fechaFormateada.substring(0, 1);
        String restoCadena = fechaFormateada.substring(1);

        primerCaracter = primerCaracter.toUpperCase(Locale.getDefault());
        restoCadena = restoCadena.toLowerCase(Locale.getDefault());


        String fechaFormateadaFinal = primerCaracter + restoCadena;

        fecha[0] = fechaFormateadaFinal;
        fecha[1] = horaString;
        return fecha;
    }

    public void limpiarEquipos(){
        equipos.clear();
    }

    public void limpiarFixture(){
        fixtures.clear();
    }

    public void destroy(){
        instance = null;
    }
}
