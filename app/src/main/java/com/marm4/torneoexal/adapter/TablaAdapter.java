package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.items.EquipoTabla;

import java.util.List;

public class TablaAdapter extends RecyclerView.Adapter<TablaAdapter.ViewHolder> {

    private List<EquipoTabla> eLista;


    public TablaAdapter() {
    }

    @NonNull
    @Override
    public TablaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabla, parent, false);
        return new TablaAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TablaAdapter.ViewHolder holder, int position) {
        if(eLista!=null){
            EquipoTabla tabla = eLista.get(position);
            holder.bind(tabla);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<EquipoTabla> lista) {
        this.eLista = lista;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if(eLista==null)
            return 0;
        else
            return eLista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll;
        private TextView posicion;
        private ImageView escudo;
        private TextView nombre;
        private TextView partidos;
        private TextView golesDif;
        private TextView puntos;

        public ViewHolder(@NonNull View view) {
            super(view);
            ll = view.findViewById(R.id.ll);
            posicion = view.findViewById(R.id.posicion);
            escudo = view.findViewById(R.id.escudo);
            nombre = view.findViewById(R.id.nombre);
            partidos = view.findViewById(R.id.partidos);
            golesDif = view.findViewById(R.id.golesDif);
            puntos = view.findViewById(R.id.puntos);


        }

        public void bind(EquipoTabla e) {
            int posicionInt = Integer.parseInt(e.getPosicion());

            posicion.setText(e.getPosicion());
            escudo.setImageURI(e.getEquipo().getEscudo());
            nombre.setText(e.getEquipo().getNombre());
            partidos.setText(e.getPartidos());
            golesDif.setText(e.getDiferenciaGoles());
            puntos.setText(e.getPuntos());
        }

    }
}
