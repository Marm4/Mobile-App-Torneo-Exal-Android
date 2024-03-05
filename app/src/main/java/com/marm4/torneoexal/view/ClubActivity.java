package com.marm4.torneoexal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
    }

    private void findViews() {
        nombre = findViewById(R.id.nombre);
        escudo = findViewById(R.id.escudo);
        goles = findViewById(R.id.goles);
        puntos = findViewById(R.id.puntos);
        partidos = findViewById(R.id.partidos);
        rv1 = findViewById(R.id.recyclerViewUno);
        rv2 = findViewById(R.id.recyclerViewDos);
    }
    private void setViews() {
        nombre.setText(equipo.getNombre());
        escudo.setImageURI(equipo.getEscudo());
        goles.setText(getGoles());
        puntos.setText(Torneo.getInstance().getPuntosPorId(equipo.getId()));
        partidos.setText(Torneo.getInstance().getPartidosJugador(equipo.getId()));
    }

    private String getGoles() {
        int goles = 0;
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

        adapter1.setList(equipo.getJugadores());
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

}