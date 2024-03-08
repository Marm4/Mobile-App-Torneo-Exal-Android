package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.view.PartidoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VistaPartidosAdapter extends RecyclerView.Adapter<VistaPartidosAdapter.ViewHolder> {

    private List<Partido> partidos;
    private String ultimaFecha = "";

    public VistaPartidosAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partido, parent, false);
        return new ViewHolder(view);
    }


    public void setPartidos(List<Partido> partidos){
        this.partidos = partidos;
    }

    public void setUltimaFecha(String s){
        ultimaFecha = s;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(partidos!=null){
            Partido partido = partidos.get(position);
            holder.bind(partido, ultimaFecha);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Partido> partidosList) {
        partidos = partidosList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(partidos==null)
            return 0;
        else
            return partidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fecha;
        private TextView hora;
        private ImageView escudoUno;
        private ImageView escudoDos;
        private TextView equipoUno;
        private TextView equipoDos;
        private TextView vs;
        private View root;
        private LinearLayout ll;


        public ViewHolder(@NonNull View view) {
            super(view);
            root = view;
            fecha = view.findViewById(R.id.fecha);
            hora = view.findViewById(R.id.hora);
            escudoUno = view.findViewById(R.id.escudoUno);
            escudoDos = view.findViewById(R.id.escudoDos);
            equipoUno = view.findViewById(R.id.equipoUno);
            equipoDos = view.findViewById(R.id.equipoDos);
            vs = view.findViewById(R.id.vs);
            ll = view.findViewById(R.id.ll);

        }

        @SuppressLint("SetTextI18n")
        public void bind(Partido partido, String ultimaFecha) {
            try {
                setFecha(partido.getDiaHora(), ultimaFecha);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Equipo eUno = Torneo.getInstance().getEquipoId(partido.getEquipoUno());
            Equipo eDos = Torneo.getInstance().getEquipoId(partido.getEquipoDos());

            if(eUno==null || eDos==null)
                return;

            escudoUno.setImageURI(eUno.getEscudo());
            escudoDos.setImageURI(eDos.getEscudo());

            equipoUno.setText(eUno.getNombre());
            equipoDos.setText(eDos.getNombre());

            String golesEquipoUno = "0";
            String golesEquipoDos = "0";
            if(partido.getGolesEquipoUno()!=null)
                golesEquipoUno = String.valueOf(partido.getGolesEquipoUno().size());

            if(partido.getGolesEquipoDos()!=null)
                golesEquipoDos = String.valueOf(partido.getGolesEquipoDos().size());

            if(compararFechas(partido.getDiaHora()))
                vs.setText(golesEquipoUno + " - " + golesEquipoDos);
            else
                vs.setText("vs");


            ll.setOnClickListener(view -> {
                Globals.getInstance().deleteEquipos();
                Globals.getInstance().agregarEquipos(eUno, eDos);

                Intent intent = new Intent(view.getContext(), PartidoActivity.class);
                view.getContext().startActivity(intent);
            });

        }

        private Boolean compararFechas(String selectedDateTimeString) {
            if(selectedDateTimeString.isEmpty())
                return false;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date fechaActual = new Date();
            Boolean retorno = false;
            try {
                Date fechaSeleccionada = dateFormat.parse(selectedDateTimeString);

                if (fechaActual.after(fechaSeleccionada)) {
                    retorno = true;
                } else if (fechaActual.before(fechaSeleccionada)) {
                    retorno = false;
                } else {
                    retorno = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return retorno;
        }

        private void setFecha(String fechaCompleta, String ultimaFecha) throws ParseException {
            if (fechaCompleta.isEmpty()){
                fecha.setText("");
                return;
            }
            String[] partes = fechaCompleta.split(" ");
            String fechaString = partes[0];
            String horaString = partes[1];

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = dateFormat.parse(fechaString);

            SimpleDateFormat nuevoFormato = new SimpleDateFormat("EEEE dd/MM", Locale.getDefault());
            String fechaFormateada = nuevoFormato.format(date);

            String primerCaracter = fechaFormateada.substring(0, 1);
            String restoCadena = fechaFormateada.substring(1);

            primerCaracter = primerCaracter.toUpperCase(Locale.getDefault());
            restoCadena = restoCadena.toLowerCase(Locale.getDefault());


            String fechaFormateadaFinal = primerCaracter + restoCadena;

            if(ultimaFecha.equals(fechaFormateadaFinal))
                fecha.setVisibility(View.GONE);
            else
                fecha.setText(fechaFormateadaFinal);

            hora.setText(horaString);
            Adapters.getInstance().getPartidosAdapter().setUltimaFecha(fechaFormateadaFinal);
        }
    }
}