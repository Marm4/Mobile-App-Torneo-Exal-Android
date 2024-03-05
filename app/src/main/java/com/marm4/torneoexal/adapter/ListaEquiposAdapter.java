package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.view.ClubActivity;
import com.marm4.torneoexal.view.admin.VerClubesActivity;

import java.util.List;

public class ListaEquiposAdapter extends RecyclerView.Adapter<ListaEquiposAdapter.ViewHolder> {

    private List<Equipo> equipos;
    private Context context;

    public ListaEquiposAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public ListaEquiposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipo, parent, false);
        return new ListaEquiposAdapter.ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull ListaEquiposAdapter.ViewHolder holder, int position) {
        if(equipos!=null){
            Equipo equipo = equipos.get(position);
            holder.bind(equipo);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Equipo> equipos) {
        this.equipos = equipos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(equipos==null)
            return 0;
        else
            return equipos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView escudo;
        private TextView nombre;
        private View view;
        private Context context;

        public ViewHolder(@NonNull View view, Context context) {
            super(view);
            this.view = view;
            this.context = context;
            escudo = view.findViewById(R.id.escudo);
            nombre = view.findViewById(R.id.nombre);


        }

        public void bind(Equipo equipo) {
            if(equipo.getEscudo()!=null)
                escudo.setImageURI(equipo.getEscudo());
            nombre.setText(equipo.getNombre());
            view.setOnClickListener(view1 -> {
                Globals.getInstance().setEquipo(equipo);
                Intent intent = new Intent(context, ClubActivity.class);
                context.startActivity(intent);
            });

        }

    }
}
