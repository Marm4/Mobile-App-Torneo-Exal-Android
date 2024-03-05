package com.marm4.torneoexal.global;

import com.marm4.torneoexal.adapter.VistaPartidosAdapter;
import com.marm4.torneoexal.model.Partido;

import java.util.List;

public class Adapters {

    private VistaPartidosAdapter partidosAdapter;
    private static Adapters instance;

    private Adapters() {
    }

    public static synchronized Adapters getInstance(){
        if(instance == null)
            instance = new Adapters();
        return instance;
    }

    public void setPartidosAdapter(VistaPartidosAdapter partidosAdapter){
        this.partidosAdapter = partidosAdapter;
    }

    public void setListPartidos(List<Partido> partidos){

        if(partidosAdapter!=null) {
            partidosAdapter.setPartidos(partidos);
            partidosAdapter.notifyDataSetChanged();
        }
    }

    public Boolean isPartidoSet(){
        return partidosAdapter == null;
    }

    public VistaPartidosAdapter getPartidosAdapter() {
        return partidosAdapter;
    }
}
