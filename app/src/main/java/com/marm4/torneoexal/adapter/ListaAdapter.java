package com.marm4.torneoexal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.AuthController;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Usuario;
import com.marm4.torneoexal.view.club.ClubActivity;
import com.marm4.torneoexal.view.fixture.FixtureActivity;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {

    private List<?> lista;
    private Context context;

    public ListaAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new ListaAdapter.ViewHolder(view, context);
    }


    @Override
    public void onBindViewHolder(@NonNull ListaAdapter.ViewHolder holder, int position) {
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

        private ImageView escudo;
        private TextView nombre;
        private View view;
        private Context context;
        private TextView textoParaMostrar;

        public ViewHolder(@NonNull View view, Context context) {
            super(view);
            this.view = view;
            this.context = context;
            escudo = view.findViewById(R.id.escudo);
            nombre = view.findViewById(R.id.nombre);
            textoParaMostrar = view.findViewById(R.id.textoParaMostrar);


        }

        public void bind(Object object) {
            if(object instanceof  Equipo){

                Equipo equipo = (Equipo) object;
                if(equipo.getEscudo()!=null)
                    escudo.setImageURI(equipo.getEscudo());
                nombre.setText(equipo.getNombre());
                view.setOnClickListener(view1 -> {
                        Globals.getInstance().setEquipo(equipo);
                        Intent intent = new Intent(context, ClubActivity.class);
                        context.startActivity(intent);
                });
            }

            else if (object instanceof Usuario) {
                Usuario usuario = (Usuario) object;
                if(usuario.equals(Globals.getInstance().getUsuario())){
                    view.setVisibility(view.GONE);
                    return;
                }
                escudo.setVisibility(View.GONE);
                nombre.setText(usuario.getEmail());
                textoParaMostrar.setVisibility(View.VISIBLE);
                AuthController authController = new AuthController();
                if(usuario.isAdmin()){
                    textoParaMostrar.setText("Sacar privilegios");
                    textoParaMostrar.setTextColor(context.getResources().getColor(R.color.rojoEliminar));
                    textoParaMostrar.setOnClickListener(view1 -> {
                        usuario.setAdmin(false);
                        authController.guardarUsuario(usuario);
                    });
                }
                else{
                    textoParaMostrar.setText("Hacer administrador");
                    textoParaMostrar.setTextColor(context.getResources().getColor(R.color.textColor));
                    textoParaMostrar.setOnClickListener(view1 -> {
                        usuario.setAdmin(true);
                        authController.guardarUsuario(usuario);
                    });
                }

            }

            else{
                if(!Globals.getInstance().getUsuario().isAdmin()){
                    textoParaMostrar.setVisibility(View.GONE);
                }
                else{
                    textoParaMostrar.setVisibility(View.VISIBLE);
                }
                Fixture fixture = (Fixture) object;
                if(fixture.getId().equals(Globals.getInstance().getProximaFecha())){
                    textoParaMostrar.setTextColor(textoParaMostrar.getContext().getResources().getColor(R.color.primaryVariation));
                    textoParaMostrar.setText("En vista");
                }
                else {
                    textoParaMostrar.setTextColor(textoParaMostrar.getContext().getResources().getColor(R.color.textColor));
                    textoParaMostrar.setText("Mostrar en fixture");
                }

                escudo.setImageDrawable(escudo.getContext().getDrawable(R.drawable.ic_fixture));
                int sizeInPixels = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 25, escudo.getContext().getResources().getDisplayMetrics());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(sizeInPixels, sizeInPixels);

                escudo.setLayoutParams(params);
                nombre.setText(fixture.getFechaNro());
                view.setOnClickListener(view1 -> {
                    Globals.getInstance().setFixture(fixture);
                    Intent intent = new Intent(context, FixtureActivity.class);
                    context.startActivity(intent);
                });

                textoParaMostrar.setOnClickListener(view1 -> {
                    Globals.getInstance().setProximaFecha(fixture.getId());
                    Adapters.getInstance().notifyDataChangeListaAdapter();
                });



            }

        }

    }
}
