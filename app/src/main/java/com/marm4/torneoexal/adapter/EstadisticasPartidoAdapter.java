package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.model.EstadisticasPartido;

import java.util.List;

public class EstadisticasPartidoAdapter extends RecyclerView.Adapter<EstadisticasPartidoAdapter.ViewHolder> {

    private List<EstadisticasPartido> estadisticas;

    public EstadisticasPartidoAdapter() {
    }

    @NonNull
    @Override
    public EstadisticasPartidoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jugador_partido2, parent, false);
        return new EstadisticasPartidoAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EstadisticasPartidoAdapter.ViewHolder holder, int position) {
        if(estadisticas !=null){
            EstadisticasPartido gol = estadisticas.get(position);
            holder.bind(gol);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<EstadisticasPartido> estadisticas) {
        this.estadisticas = estadisticas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(estadisticas ==null)
            return 0;
        else
            return estadisticas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout cl1;
        private ConstraintLayout cl2;
        private TextView jugador1;
        private TextView jugador2;
        private ImageView item1;
        private ImageView item2;
        public ViewHolder(@NonNull View view) {
            super(view);

            cl1 = view.findViewById(R.id.clUno);
            cl2 = view.findViewById(R.id.clDos);
            jugador1 = view.findViewById(R.id.jugadorUno);
            jugador2 = view.findViewById(R.id.jugadorDos);
            item1 = view.findViewById(R.id.itemUno);
            item2 = view.findViewById(R.id.itemDos);

        }

        public void bind(EstadisticasPartido estadisticas) {
            if(estadisticas.getTarjeta1()!=null){
                if(estadisticas.getTarjeta1().getAmarilla())
                    item1.setImageDrawable(item1.getContext().getDrawable(R.drawable.ic_amarilla));
                else
                    item1.setImageDrawable(item1.getContext().getDrawable(R.drawable.ic_roja));
            }

            if(estadisticas.getTarjeta2()!=null){
                if(estadisticas.getTarjeta2().getAmarilla())
                    item2.setImageDrawable(item1.getContext().getDrawable(R.drawable.ic_amarilla));
                else
                    item2.setImageDrawable(item1.getContext().getDrawable(R.drawable.ic_roja));
            }


            if(!estadisticas.getNombreJugadorEquipo1().isEmpty()){
                jugador1.setText(estadisticas.getNombreJugadorEquipo1());
            }
            else
                cl1.setVisibility(View.GONE);

            if(!estadisticas.getNombreJugadorEquipo2().isEmpty()){
                jugador2.setText(estadisticas.getNombreJugadorEquipo2());
            }
            else
                cl2.setVisibility(View.GONE);

        }

    }
}
