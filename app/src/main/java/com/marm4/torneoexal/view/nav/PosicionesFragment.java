package com.marm4.torneoexal.view.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.TablaAdapter;
import com.marm4.torneoexal.adapter.VistaPartidosAdapter;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.items.EquipoTabla;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PosicionesFragment extends Fragment {

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_posiciones, container, false);
        initUI();
        return root;
    }

    private void initUI() {
        List<EquipoTabla> equiposTabla = crearEquipos();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TablaAdapter adapter = new TablaAdapter();
        recyclerView.setAdapter(adapter);
        Log.i("Tabla size: ", "  "  + equiposTabla.size());
        adapter.setList(equiposTabla);

    }

    private List<EquipoTabla> crearEquipos() {
        List<EquipoTabla> equiposTabla = new ArrayList<>();
        List<Equipo> equipos = Torneo.getInstance().getEquipos();
        for (Equipo e : equipos) {
            EquipoTabla et = new EquipoTabla();
            et.setEquipo(e);
            int goles = 0;
            for (Jugador j : e.getJugadores()) {
                goles = goles + j.getGoles();
            }
            et.setGolesFavor(String.valueOf(goles));
            et.setPuntos(Torneo.getInstance().getPuntosPorId(e.getId()));
            et.setPartidos(getCantidadPartidos(e,"partidos"));
            et.setGanados(getCantidadPartidos(e,"ganados"));
            et.setPerdidos(getCantidadPartidos(e,"perdidos"));
            et.setEmpatados(getCantidadPartidos(e,"empatados"));
            et.setGolesContra(getGolesEnContra(e));
            et.setDiferenciaGoles();
            equiposTabla.add(et);

        }

        Collections.sort(equiposTabla, new Comparator<EquipoTabla>() {
            @Override
            public int compare(EquipoTabla equipo1, EquipoTabla equipo2) {
                return Integer.compare(Integer.parseInt(equipo2.getPuntos()), Integer.parseInt(equipo1.getPuntos()));
            }
        });
        int i = 1;
        for(EquipoTabla e : equiposTabla){
            e.setPosicion(String.valueOf(i));
            i++;
        }


        return equiposTabla;
    }

    private String getCantidadPartidos(Equipo e, String retorno) {
        List<Fixture> fList = Torneo.getInstance().getFixtures();
        int cantidadPartidos = 0;
        int ganados = 0;
        int perdidos = 0;
        int empatados = 0;
        for (Fixture f : fList) {
            for (Partido p : f.getPartidos()) {
                if (p.getEquipoUno().equals(e.getId()) || p.getEquipoDos().equals(e.getId())) {
                    if (Torneo.getInstance().compararFechas(p.getDiaHora())) {
                        if (retorno.equals("partidos"))
                            cantidadPartidos++;
                        else {
                            int golesUno = 0;
                            int golesDos = 0;
                            if(p.getGolesEquipoUno()!=null)
                                golesUno = p.getGolesEquipoUno().size();
                            if(p.getGolesEquipoDos()!=null)
                                golesDos = p.getGolesEquipoDos().size();
                            if (p.getEquipoUno().equals(e.getId())) {
                                if (golesUno > golesDos)
                                    ganados++;
                                else if (golesUno == golesDos)
                                    empatados++;
                                else
                                    perdidos++;
                            }
                            else {
                                if (p.getEquipoDos().equals(e.getId())) {
                                    if (golesUno < golesDos)
                                        ganados++;
                                    else if (golesUno == golesDos)
                                        empatados++;
                                    else
                                        perdidos++;

                                }
                            }
                        }
                    }
                }
            }
        }
        if(retorno.equals("ganados"))
            return String.valueOf(ganados);
        else if (retorno.equals("empatados"))
            return String.valueOf(empatados);
        else if(retorno.equals("perdidos"))
            return String.valueOf(perdidos);
        else
            return String.valueOf(cantidadPartidos);
    }

    private String getGolesEnContra(Equipo e){
        List<Fixture> fList = Torneo.getInstance().getFixtures();
        int golesEnContra = 0;
        for(Fixture f : fList){
            for(Partido p : f.getPartidos()){
                if(p.getEquipoUno().equals(e.getId()))
                    if(p.getGolesEquipoDos()!=null)
                        golesEnContra = golesEnContra + p.getGolesEquipoDos().size();
                else if (p.getEquipoDos().equals(e.getId()))
                    if(p.getGolesEquipoUno()!=null)
                        golesEnContra = golesEnContra + p.getGolesEquipoUno().size();
            }
        }

        return String.valueOf(golesEnContra);
    }
}