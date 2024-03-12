package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;

import java.util.List;
import java.util.Map;

public class PerfilEquipoAdapter extends RecyclerView.Adapter<PerfilEquipoAdapter.ViewHolder> {

    private List<?> lista;

    public PerfilEquipoAdapter() {

    }

    @NonNull
    @Override
    public PerfilEquipoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipo_info, parent, false);
        return new PerfilEquipoAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PerfilEquipoAdapter.ViewHolder holder, int position) {
        if(lista!=null){
            holder.bind(lista.get(position));
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<?> lista) {
        this.lista = lista;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(lista==null)
            return 0;
        else
            return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item1;
        private TextView item1Aux;
        private TextView item2;
        private TextView item3;
        private TextView item4;
        private TextView item5;
        private TextView item6;
        private TextView item7;
        private LinearLayout ll7;
        private ImageView imageView;
        private TextView eliminarJugador;
        private TextView cambiarNombre;
        private LinearLayout ll;
        private EditText cambiarNombreET;


        public ViewHolder(@NonNull View view) {
            super(view);

            item1 = view.findViewById(R.id.itemUno);
            item2 = view.findViewById(R.id.itemDos);
            item3 = view.findViewById(R.id.itemTres);
            item4 = view.findViewById(R.id.itemCuatro);
            item5 = view.findViewById(R.id.itemCinco);
            item6 = view.findViewById(R.id.itemSeis);
            item7 = view.findViewById(R.id.itemSiete);
            ll7 = view.findViewById(R.id.llSiete);
            imageView = view.findViewById(R.id.imageView);
        }

        public void bind(Object object) {
            if(object instanceof Jugador) {
                Jugador jugador = (Jugador) object;
                item1.setText(jugador.getNombre());
                item2.setText(String.valueOf(jugador.getGoles()));
                item3.setText(Torneo.getInstance().tarjetasAmarillasJugador(jugador));
                item4.setText(Torneo.getInstance().tarjetasRojasJugador(jugador));

            }
            else if (object instanceof Map){
                Map<Partido, String> map = (Map<Partido, String>) object;
                Partido partido = null;
                String valor = "";
                for (Map.Entry<Partido, String> entry : map.entrySet()) {
                    partido = entry.getKey();
                    valor = entry.getValue();
                }
                Equipo equipoAux = equipoUnoODos(partido);
                Equipo equipo = Globals.getInstance().getEquipo();
                item5.setText(valor);
                item6.setText(equipoAux.getNombre());
                if(!Torneo.getInstance().compararFechas(partido.getEquipoUno())){
                    if(equipo.getId().equals(partido.getEquipoUno()))
                        item7.setText(partido.getResultadoUno() + " - " + partido.getResultadoDos());
                    else
                        item7.setText(partido.getResultadoDos() + " - " + partido.getResultadoUno());
                }
                else
                    item7.setText("Por jugar");

                item5.setVisibility(View.VISIBLE);
                item6.setVisibility(View.VISIBLE);
                item7.setVisibility(View.VISIBLE);
                ll7.setVisibility(View.VISIBLE);
                item1.setVisibility(View.GONE);
                item2.setVisibility(View.GONE);
                item3.setVisibility(View.GONE);
                item4.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
            }
        }

        private Equipo equipoUnoODos(Partido partido) {
            if(partido.getEquipoUno().equals(Globals.getInstance().getEquipo().getId()))
                return Torneo.getInstance().getEquipoId(partido.getEquipoDos());
            else
                return Torneo.getInstance().getEquipoId(partido.getEquipoUno());
        }

    }
}

