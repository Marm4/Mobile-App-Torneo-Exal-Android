package com.marm4.torneoexal.view.club;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.PerfilEquipoAdapter;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubActivity extends AppCompatActivity {


    private Equipo equipo;
    private TextView nombre;
    private ImageView escudo;
    private TextView goles;
    private TextView puntos;
    private TextView partidos;
    private RecyclerView rv1;
    private RecyclerView rv2;
    private List<Map<Partido, String>> mapList;
    private TextView eliminarPartido;
    private TextView modificar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        initUI();
    }

    private void initUI() {
        equipo = Globals.getInstance().getEquipo();
        mapList = new ArrayList<>();
        setMap();
        findViews();
        setViews();
        adapters();

        if(Globals.getInstance().getUsuario()!=null && Globals.getInstance().getUsuario().isAdmin()){
            modificar.setOnClickListener(view -> {
                Intent intent = new Intent(ClubActivity.this, CrearClubActivity.class);
                intent.putExtra("equipoExistente", true);
                startActivity(intent);
            });
        }
        else{
            modificar.setVisibility(View.GONE);
        }


    }

    private void findViews() {
        nombre = findViewById(R.id.nombre);
        escudo = findViewById(R.id.escudo);
        goles = findViewById(R.id.goles);
        puntos = findViewById(R.id.puntos);
        partidos = findViewById(R.id.partidos);
        rv1 = findViewById(R.id.recyclerViewUno);
        rv2 = findViewById(R.id.recyclerViewDos);
        eliminarPartido = findViewById(R.id.eliminar);
        modificar = findViewById(R.id.modificar);
    }
    private void setViews() {
        nombre.setText(equipo.getNombre());
        escudo.setImageURI(equipo.getEscudo());
        goles.setText(getGoles());
        puntos.setText(Torneo.getInstance().getPuntosPorId(equipo.getId()));
        partidos.setText(Torneo.getInstance().getPartidosJugador(equipo.getId()));
        if(!Globals.getInstance().getUsuario().isAdmin())
            eliminarPartido.setVisibility(View.GONE);
    }

    private String getGoles() {
        int goles = 0;
        if(equipo.getJugadores()==null)
            return String.valueOf(goles);
        for(Jugador jugador : equipo.getJugadores()){
            goles = goles + jugador.getGoles();
        }
        return String.valueOf(goles);
    }

    private void adapters(){
        rv1.setLayoutManager(new LinearLayoutManager(this));
        rv2.setLayoutManager(new LinearLayoutManager(this));
        PerfilEquipoAdapter adapter1 = new  PerfilEquipoAdapter();
        rv1.setAdapter(adapter1);
        PerfilEquipoAdapter adapter2 = new  PerfilEquipoAdapter();
        rv2.setAdapter(adapter2);

        adapter1.setList(orderList(equipo.getJugadores()));
        adapter2.setList(mapList);
    }

    private void setMap(){
        for(Fixture fixture : Torneo.getInstance().getFixtures()){
            for(Partido partido : fixture.getPartidos()){
                if(partido.getEquipoUno().equals(equipo.getId()) || partido.getEquipoDos().equals(equipo.getId())){
                    Map<Partido, String> map = new HashMap<>();
                    map.put(partido, fixture.getFechaNro());
                    mapList.add(map);
                }
            }
        }
    }

    private List<Jugador> orderList(List<Jugador> jugadores){
        if(jugadores == null)
            return null;
        Collections.sort(jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador jugador1, Jugador jugador2) {
                return Integer.compare(jugador2.getGoles(), jugador1.getGoles());
            }
        });

        return jugadores;
    }

}