package com.marm4.torneoexal.view.partido;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.EstadisticasPartidoAdapter;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.EstadisticasPartido;
import com.marm4.torneoexal.model.Tarjeta;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PartidoActivity extends AppCompatActivity {

    private TextView fecha;
    private ImageView escudo1;
    private ImageView escudo2;
    private TextView nombre1;
    private TextView nombre2;
    private TextView vs;
    private Equipo equipo1;
    private Equipo equipo2;
    private Partido partido;
    private String fechaPartido;

    private TextView editarPartido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);

        initUI();
    }

    private void initUI() {
        findViews();
        setListeners();
        setEquipos();
        setValues();
        setViews();
        recyclerViews();
    }

    private void findViews() {
        fecha = findViewById(R.id.fecha);
        escudo1 = findViewById(R.id.escudoUno);
        escudo2 = findViewById(R.id.escudoDos);
        nombre1 = findViewById(R.id.equipoUno);
        nombre2 = findViewById(R.id.equipoDos);
        editarPartido = findViewById(R.id.editar);
        vs = findViewById(R.id.vs);

    }

    private void setListeners() {
        if(!Globals.getInstance().getUsuario().isAdmin())
            editarPartido.setVisibility(View.GONE);
        else
            editarPartido.setOnClickListener(view -> {
                Intent intent = new Intent(PartidoActivity.this, CrearPartidoActivity.class);
                startActivity(intent);
            });
    }

    private void setValues() {
        partido = Torneo.getInstance().getPartidoByEquipos(equipo1.getId(), equipo2.getId());
        fechaPartido = Globals.getInstance().getFixture().getFechaNro();


    }

    private void setViews(){
        String[] fechaSplit = new String[0];
        try {
            fechaSplit = Torneo.getInstance().getFechaString(partido.getDiaHora());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        fecha.setText(fechaPartido);
        escudo1.setImageURI(equipo1.getEscudo());
        escudo2.setImageURI(equipo2.getEscudo());
        nombre1.setText(equipo1.getNombre());
        nombre2.setText(equipo2.getNombre());

        int resultadoUno = 0;
        int resultadoDos = 0;
        if(partido.getGolesEquipoUno()!=null)
            resultadoUno = partido.getGolesEquipoUno().size();
        if(partido.getGolesEquipoDos()!=null)
            resultadoDos = partido.getGolesEquipoDos().size();

        vs.setText(resultadoUno + " - " + resultadoDos);
    }

    private void setEquipos(){
        for(Equipo equipo : Globals.getInstance().getListaEquipos()){
            if(equipo1 == null)
                equipo1 = equipo;
            else if(equipo2 == null)
                equipo2 = equipo;
        }
    }

    private void recyclerViews() {
        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        EstadisticasPartidoAdapter a = new EstadisticasPartidoAdapter();
        rv.setAdapter(a);
        List<EstadisticasPartido> estadisticas = getGoles();
        List<EstadisticasPartido> estadisticasAux = getTarjetas();
        if(estadisticasAux!=null){
            for(EstadisticasPartido e : estadisticasAux){
                estadisticas.add(e);
            }
        }
        a.setList(estadisticas);

    }

    private List<EstadisticasPartido> getGoles(){
        List<EstadisticasPartido> estadisticas = new ArrayList<>();

        if(partido.getGolesEquipoUno()==null && partido.getGolesEquipoDos()!=null){
            for(String idJugador : partido.getGolesEquipoDos()){
                EstadisticasPartido stats = new EstadisticasPartido();
                stats.setNombreJugadorEquipo2(getNombreJugadorById(idJugador, equipo2));
                estadisticas.add(stats);
            }
        }
        else if(partido.getGolesEquipoDos()==null && partido.getGolesEquipoUno()!=null){
            for(String idJugador : partido.getGolesEquipoUno()){
                EstadisticasPartido gol = new EstadisticasPartido();
                gol.setNombreJugadorEquipo1(getNombreJugadorById(idJugador, equipo1));
                estadisticas.add(gol);
            }
        }
        else if(partido.getGolesEquipoDos()!=null && partido.getGolesEquipoUno()!=null){
            for (int i = 0; i < Math.max(partido.getGolesEquipoUno().size(), partido.getGolesEquipoDos().size()); i++) {
                EstadisticasPartido stats = new EstadisticasPartido();
                if (i < partido.getGolesEquipoUno().size()) {
                    String jugadorId1 = partido.getGolesEquipoUno().get(i);
                    String nombre1 = getNombreJugadorById(jugadorId1, equipo1);
                    stats.setNombreJugadorEquipo1(nombre1);
                }
                if (i < partido.getGolesEquipoDos().size()) {
                    String jugadorId2 = partido.getGolesEquipoDos().get(i);
                    String nombre2 = getNombreJugadorById(jugadorId2, equipo2);
                    stats.setNombreJugadorEquipo2(nombre2);

                }
                estadisticas.add(stats);
            }

            Iterator<EstadisticasPartido> iterator = estadisticas.iterator();
            while (iterator.hasNext()) {
                EstadisticasPartido statsAux = iterator.next();
                if (statsAux.getNombreJugadorEquipo1().isEmpty() && statsAux.getNombreJugadorEquipo2().isEmpty()) {
                    iterator.remove();
                }
            }
        }

        return estadisticas;
    }

    private List<EstadisticasPartido> getTarjetas(){
        if(partido.getTarjetas()==null)
            return null;

        List<Tarjeta> tarjetas = new ArrayList<>(partido.getTarjetas());
        List<EstadisticasPartido> estadisticas = new ArrayList<>();
        int i = 0;
        boolean getOut = false;
        while(tarjetas.size()!=0 || getOut){
                EstadisticasPartido stats = new EstadisticasPartido();
                for (Jugador j : equipo1.getJugadores()) {
                    if (tarjetas.get(i).getJugadorId().equals(j.getId())) {
                        stats.setTarjeta1(tarjetas.get(i));
                        stats.setNombreJugadorEquipo1(j.getNombre());
                        tarjetas.remove(stats.getTarjeta1());
                        break;
                    }

                }
                if(tarjetas.size()>0){
                    for (Jugador j : equipo2.getJugadores()) {
                        if (tarjetas.get(i).getJugadorId().equals(j.getId())) {
                            stats.setTarjeta2(tarjetas.get(i));
                            stats.setNombreJugadorEquipo2(j.getNombre());
                            tarjetas.remove(stats.getTarjeta2());
                            getOut = false;
                            break;
                        }
                    }
                }
                estadisticas.add(stats);
            }

        return  estadisticas;
        }

    private String getNombreJugadorById(String id, Equipo e){
        for(Jugador j : e.getJugadores()){
            if(j.getId().equals(id))
                return j.getNombre();
        }
        return "";
    }

}