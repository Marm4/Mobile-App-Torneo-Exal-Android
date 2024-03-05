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
            imageView = view.findViewById(R.id.imageView);
            eliminarJugador = view.findViewById(R.id.eliminarJugador);
            cambiarNombre = view.findViewById(R.id.cambiarNombre);
            ll = view.findViewById(R.id.layoutOpciones);
        }

        public void bind(Object object) {
            if(object instanceof Jugador) {
                Jugador jugador = (Jugador) object;
                item1.setText(jugador.getNombre());
                item2.setText(String.valueOf(jugador.getGoles()));
                item3.setText(Torneo.getInstance().tarjetasAmarillasJugador(jugador));
                item4.setText(Torneo.getInstance().tarjetasRojasJugador(jugador));


                /*imageView.setOnClickListener(view -> {
                    if(ll.getVisibility() == View.VISIBLE)
                        ll.setVisibility(View.GONE);
                    else
                        ll.setVisibility(View.VISIBLE);
                });

                cambiarNombre.setOnClickListener(view -> {
                    if(cambiarNombre.getText().equals("Guardar")){
                        cambiarNombre.setText("Cambiar nombre");
                    }

                    else
                        cambiarNombre.setText("Guardar");
                    if(item1.getParent()!=null)
                        cambiarNombreET = convertirAEditText(item1);
                    else{
                        convertirATextView(cambiarNombreET);
                    }
                });*/

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
                item1.setText(valor);
                item2.setText(equipoAux.getNombre());
                if(!Torneo.getInstance().compararFechas(partido.getEquipoUno())){
                    if(equipo.getId().equals(partido.getEquipoUno()))
                        item3.setText(partido.getResultadoUno() + " - " + partido.getResultadoDos());
                    else
                        item3.setText(partido.getResultadoDos() + " - " + partido.getResultadoUno());
                }
                else
                    item3.setText("Por jugar");


                item4.setVisibility(View.GONE)  ;
                imageView.setVisibility(View.GONE);
            }
        }

        private Equipo equipoUnoODos(Partido partido) {
            if(partido.getEquipoUno().equals(Globals.getInstance().getEquipo().getId()))
                return Torneo.getInstance().getEquipoId(partido.getEquipoDos());
            else
                return Torneo.getInstance().getEquipoId(partido.getEquipoUno());
        }


        private EditText convertirAEditText(TextView textView){

            CharSequence texto = textView.getText();
            int id = textView.getId();
            ViewGroup padre = (ViewGroup) textView.getParent();
            int indice = padre.indexOfChild(textView);

            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();

            padre.removeView(textView);

            EditText editText = new EditText(textView.getContext());
            editText.setId(id);
            editText.setText(texto);

            editText.setLayoutParams(layoutParams);

            padre.addView(editText, indice);

            editText.requestFocus();
            return editText;
        }

        private void convertirATextView(EditText et){
            CharSequence texto = et.getText();
            int id = et.getId();
            ViewGroup padre = (ViewGroup) et.getParent();
            int indice = padre.indexOfChild(et);
            padre.removeView(et);
            item1.setId(id);
            item1.setText(texto);
            padre.addView(item1, indice);
        }
    }
}

