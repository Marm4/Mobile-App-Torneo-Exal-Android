package com.marm4.torneoexal.view.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.EstadisticasAdapter;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.Tarjeta;
import com.marm4.torneoexal.model.items.EstadisticaJugador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EstadisticasFragment extends Fragment {
    private View root;
    private EstadisticasAdapter adapter;
    private List<EstadisticaJugador> jugadoresList;

    private ImageView goles;
    private ImageView amarillas;
    private ImageView rojas;
    private ImageView mvps;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        initUI();
        return root;
    }

    private void initUI() {
        findJugadores();
        recyclerView();

        findViews();
        listeners();


    }

    private void findViews() {
        goles = root.findViewById(R.id.goles);
        amarillas = root.findViewById(R.id.amarillas);
        rojas = root.findViewById(R.id.rojas);
        mvps = root.findViewById(R.id.mvps);
    }

    private void listeners() {
        goles.setOnClickListener(view -> {
            setListAdapter("goles");
            setBackgroundColor(goles);
        });

        amarillas.setOnClickListener(view -> {
            setListAdapter("amarillas");
            setBackgroundColor(amarillas);
        });

        rojas.setOnClickListener(view -> {
            setListAdapter("rojas");
            setBackgroundColor(rojas);
        });

        mvps.setOnClickListener(view -> {
            setListAdapter("mvps");
            setBackgroundColor(mvps);
        });
    }

    private void setBackgroundColor(ImageView iv) {
        goles.setBackgroundColor(getResources().getColor(R.color.gris));
        amarillas.setBackgroundColor(getResources().getColor(R.color.gris));
        rojas.setBackgroundColor(getResources().getColor(R.color.gris));
        mvps.setBackgroundColor(getResources().getColor(R.color.gris));

        iv.setBackgroundColor(getResources().getColor(R.color.grisOscuro));
    }

    private void findJugadores() {
        jugadoresList = new ArrayList<>();
        if(Torneo.getInstance().getEquipos()==null)
            return;
        for(Equipo e : Torneo.getInstance().getEquipos()){
            if(e.getJugadores()!=null)
                for(Jugador j : e.getJugadores()){
                    EstadisticaJugador estadisticaJugador = findAllStats(j);
                    if(e.getEscudo()!=null)
                        estadisticaJugador.setLogoEquipo(e.getEscudo());
                    jugadoresList.add(estadisticaJugador);
                }
        }
    }

    private EstadisticaJugador findAllStats(Jugador jugador) {
        EstadisticaJugador estadisticaJugador = new EstadisticaJugador();
        estadisticaJugador.setNombre(jugador.getNombre());
        estadisticaJugador.setAmarillas(getTarjetas(jugador, true));
        estadisticaJugador.setRojas(getTarjetas(jugador, false));
        estadisticaJugador.setGoles(String.valueOf(jugador.getGoles()));
        estadisticaJugador.setMvp(getMvp(jugador));

        return estadisticaJugador;
    }

    private String getTarjetas(Jugador jugador, Boolean amarilla) {
        int tarjetas = 0;
        if(jugador.getTarjetas()!=null){
            for(Tarjeta t : jugador.getTarjetas()){
                if(amarilla && t.getAmarilla())
                    tarjetas ++;
                else if(!amarilla && !t.getAmarilla())
                    tarjetas++;
            }
        }
        return String.valueOf(tarjetas);
    }


    private String getMvp(Jugador j) {
        List<Fixture> fixtures = Torneo.getInstance().getFixtures();
        int mvp = 0;
        if(fixtures.isEmpty())
            return String.valueOf(mvp);

        for (Fixture f : fixtures) {
            if (!f.getPartidos().isEmpty()) {
                for (Partido p : f.getPartidos()) {
                    if (p.getMvp() != null && p.getMvp().getId().equals(j.getId())) {
                        mvp++;
                    }
                }
            }
        }
        return String.valueOf(mvp);
    }


    private List<EstadisticaJugador> orderList(List<EstadisticaJugador> jugadores, String orderBy){
        Collections.sort(jugadores, new Comparator<EstadisticaJugador>() {
            @Override
            public int compare(EstadisticaJugador jugador1, EstadisticaJugador jugador2) {
                if (orderBy.equals("amarillas"))
                    return Integer.compare(Integer.parseInt(jugador2.getAmarillas()), Integer.parseInt(jugador1.getAmarillas()));
                else if(orderBy.equals("rojas"))
                    return Integer.compare(Integer.parseInt(jugador2.getRojas()), Integer.parseInt(jugador1.getRojas()));
                else if(orderBy.equals("goles"))
                    return Integer.compare(Integer.parseInt(jugador2.getGoles()), Integer.parseInt(jugador1.getGoles()));
                else
                    return Integer.compare(Integer.parseInt(jugador2.getMvp()), Integer.parseInt(jugador1.getMvp()));

            }
        });

        List<EstadisticaJugador> returnList = jugadores.subList(0, Math.min(jugadores.size(), 10));
        int i = 1;
        for(EstadisticaJugador jugador : returnList){
            jugador.setPosicion(String.valueOf(i));
            i++;
        }

        return returnList;
    }


    private void setListAdapter(String orderBy) {
        if(jugadoresList == null)
            return;
        if(jugadoresList.isEmpty() || jugadoresList.size() == 1)
            adapter.setList(jugadoresList);
        else
            adapter.setList(orderList(jugadoresList, orderBy));
    }




    private void recyclerView() {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EstadisticasAdapter();
        recyclerView.setAdapter(adapter);
        setListAdapter("goles");
    }
}