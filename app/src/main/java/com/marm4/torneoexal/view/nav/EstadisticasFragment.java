package com.marm4.torneoexal.view.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.EstadisticasAdapter;
import com.marm4.torneoexal.adapter.VistaPartidosAdapter;
import com.marm4.torneoexal.global.Adapters;
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

public class EstadisticasFragment extends Fragment {

    private LinearLayout gol;
    private LinearLayout tarjetas;
    private LinearLayout mvp;
    private TextView item1;
    private TextView item2;
    private View root;
    private RecyclerView recyclerView;
    private EstadisticasAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_estadisticas, container, false);
        initUI();
        return root;
    }

    private void initUI() {
        findViews();
        listeners();
        recyclerView();

    }
    private void findViews() {
        gol = root.findViewById(R.id.llGol);
        tarjetas = root.findViewById(R.id.llTarjetas);
        mvp = root.findViewById(R.id.llMvp);
        recyclerView = root.findViewById(R.id.recyclerView);
        item1 = root.findViewById(R.id.item1);
        item2 = root.findViewById(R.id.item2);

    }

    private void listeners() {
        gol.setOnClickListener(view -> {
            changeBackground(gol);
            setListAdapter(getListOf(true));
            item1.setText("G");
            item2.setVisibility(View.INVISIBLE);
        });

        tarjetas.setOnClickListener(view -> {
            changeBackground(tarjetas);
            setListAdapter(getListOf(false));
            item1.setText("A");
            item2.setVisibility(View.VISIBLE);
            item2.setText("R");
        });

        mvp.setOnClickListener(view -> {
            changeBackground(mvp);
            setListAdapter(getListOfMvp());
            item1.setText("MVP");
            item2.setVisibility(View.INVISIBLE);
        });
    }

    private List<Jugador> getListOfMvp() {
        List<Fixture> fixtures = Torneo.getInstance().getFixtures();
        List<Jugador> jugadores = new ArrayList<>();
        if(fixtures.isEmpty())
            return jugadores;

        Map<String, Jugador> mapaJugadores = new HashMap<>();

        for (Fixture f : fixtures) {
            if (!f.getPartidos().isEmpty()) {
                for (Partido p : f.getPartidos()) {
                    if (p.getMvp() != null) {
                        String nombreMvp = p.getMvp().getNombre();
                        if (mapaJugadores.containsKey(nombreMvp)) {
                            Jugador jugadorExistente = mapaJugadores.get(nombreMvp);
                            jugadorExistente.addGol();
                        } else {
                            Jugador nuevoJugador = new Jugador();
                            nuevoJugador.setId("mvp");
                            nuevoJugador.setNombre(nombreMvp);
                            nuevoJugador.addGol();
                            mapaJugadores.put(nombreMvp, nuevoJugador);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(mapaJugadores.values());
    }


    private void changeBackground(LinearLayout ll){
        if(ll.getBackground().equals(getResources().getDrawable(R.drawable.view_redondeado)))
            return;

        ll.setBackground(getResources().getDrawable(R.drawable.view_redondeado));

        if(ll.equals(gol)){
            tarjetas.setBackground(getResources().getDrawable(R.drawable.view_redondeado_gris));
            mvp.setBackground(getResources().getDrawable(R.drawable.view_redondeado_gris));
        }
        else if(ll.equals(tarjetas)){
            gol.setBackground(getResources().getDrawable(R.drawable.view_redondeado_gris));
            mvp.setBackground(getResources().getDrawable(R.drawable.view_redondeado_gris));
        }
        else {
            gol.setBackground(getResources().getDrawable(R.drawable.view_redondeado_gris));
            tarjetas.setBackground(getResources().getDrawable(R.drawable.view_redondeado_gris));
        }


    }

    private List<Jugador> getListOf(Boolean goles){
        if(Torneo.getInstance().getEquipos().isEmpty())
            return null;

        List<Jugador> jugadores = new ArrayList<>();

        for(Equipo e : Torneo.getInstance().getEquipos()){
            for(Jugador j : e.getJugadores()){
                Jugador jugador = new Jugador();
                jugador.setNombre(j.getNombre());
                if(goles){
                    jugador.setId("goles");
                    jugador.setGoles(j.getGoles());
                    jugadores.add(jugador);
                }
                else{
                    if(j.getTarjetas()!=null){
                        jugador.setId("tarjetas");
                        jugador.setTarjetas(j.getTarjetas());
                        jugadores.add(jugador);
                    }
                }
            }
        }
        return jugadores;
    }

    private List<Jugador> orderList(List<Jugador> jugadores, String orderBy){
        Collections.sort(jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador jugador1, Jugador jugador2) {
                if (orderBy.equals("tarjetas"))
                    return Integer.compare(jugador2.getTarjetas().size(), jugador1.getTarjetas().size());
                else
                    return Integer.compare(jugador2.getGoles(), jugador1.getGoles());
            }
        });
        return jugadores.subList(0, Math.min(jugadores.size(), 10));
    }


    private void setListAdapter(List<Jugador> jugadores) {
        if(jugadores == null)
            return;
        if(jugadores.isEmpty() || jugadores.size() == 1)
            adapter.setList(jugadores);
        else
            adapter.setList(orderList(jugadores, jugadores.get(0).getId()));
    }




    private void recyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EstadisticasAdapter();
        recyclerView.setAdapter(adapter);
        setListAdapter(getListOf(true));
    }
}