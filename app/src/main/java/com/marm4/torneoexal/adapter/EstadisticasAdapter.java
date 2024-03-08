package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Tarjeta;

import java.util.List;

public class EstadisticasAdapter extends RecyclerView.Adapter<EstadisticasAdapter.ViewHolder> {

    private List<Jugador> jugadores;
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
    public void setList(List<Jugador> jugadores) {
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
        private TextView item1;
        private TextView item2;


        public ViewHolder(@NonNull View view) {
            super(view);

            jugador = view.findViewById(R.id.jugador);
            item1 = view.findViewById(R.id.item1);
            item2 = view.findViewById(R.id.item2);

        }

        public void bind(Jugador j) {
            jugador.setText(j.getNombre());
            item2.setVisibility(View.INVISIBLE);
            if(j.getId().equals("tarjetas")){
                item1.setText(getCantidadTarjetas(j, true));
                item2.setVisibility(View.VISIBLE);
                item2.setText(getCantidadTarjetas(j, false));
            }
            else
                item1.setText(String.valueOf(j.getGoles()));

        }

        private String getCantidadTarjetas(Jugador j, boolean amarilla){
            int tarjetas = 0;
            if(j.getTarjetas().isEmpty())
                return String.valueOf(tarjetas);

            for(Tarjeta t : j.getTarjetas()){
                if(amarilla && t.getAmarilla())
                    tarjetas ++;
                else if(!amarilla && !t.getAmarilla())
                    tarjetas++;

            }
            return String.valueOf(tarjetas);
        }
    }
}
