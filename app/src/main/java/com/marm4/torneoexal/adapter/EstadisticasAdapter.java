package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Tarjeta;
import com.marm4.torneoexal.model.items.EstadisticaJugador;

import java.util.List;

public class EstadisticasAdapter extends RecyclerView.Adapter<EstadisticasAdapter.ViewHolder> {

    private List<EstadisticaJugador> jugadores;
    public EstadisticasAdapter() {

    }

    @NonNull
    @Override
    public EstadisticasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estadisticas, parent, false);
        return new EstadisticasAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EstadisticasAdapter.ViewHolder holder, int position) {
        if(jugadores!=null){
            holder.bind(jugadores.get(position));
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<EstadisticaJugador> jugadores) {
        this.jugadores = jugadores;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(jugadores==null)
            return 0;
        else
            return jugadores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView jugador;
        private TextView goles;
        private TextView amarillas;
        private TextView rojas;
        private TextView mvp;
        private TextView posicion;
        private ImageView logo;

        public ViewHolder(@NonNull View view) {
            super(view);

            jugador = view.findViewById(R.id.jugador);
            goles = view.findViewById(R.id.goles);
            amarillas = view.findViewById(R.id.amarillas);
            rojas = view.findViewById(R.id.rojas);
            mvp = view.findViewById(R.id.mvps);
            posicion = view.findViewById(R.id.posicion);
            logo = view.findViewById(R.id.logo);

        }

        public void bind(EstadisticaJugador j) {
        jugador.setText(j.getNombre());
        goles.setText(j.getGoles());
        amarillas.setText(j.getAmarillas());
        rojas.setText(j.getRojas());
        mvp.setText(j.getMvp());
        if(j.getLogoEquipo()!=null)
            logo.setImageURI(j.getLogoEquipo());
        posicion.setText(j.getPosicion());

    }
}
}
