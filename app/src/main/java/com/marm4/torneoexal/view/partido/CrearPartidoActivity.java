package com.marm4.torneoexal.view.partido;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.TorneoController;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.Tarjeta;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CrearPartidoActivity extends AppCompatActivity {

    private TextView fecha;
    private ImageView escudo1;
    private ImageView escudo2;
    private TextView nombre1;
    private TextView nombre2;
    private Equipo equipo1;
    private Equipo equipo2;
    private Partido partido;
    private String fechaPartido;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private List<View> jugadoresView;
    private Jugador jugadorSeleccionado;
    private ImageView gol;
    private ImageView amarilla;
    private ImageView roja;
    private FlexboxLayout jugadorLayout;
    private TorneoController torneoController;
    private ImageView mvp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_partido);

        initUI();
    }

    private void initUI() {
        findViews();
        listener();
        setValues();
        setViews();
        setJugadores();
        verificarPartido();
    }

    private void findViews() {
        fecha = findViewById(R.id.fecha);
        escudo1 = findViewById(R.id.escudoUno);
        escudo2 = findViewById(R.id.escudoDos);
        nombre1 = findViewById(R.id.equipoUno);
        nombre2 = findViewById(R.id.equipoDos);
        ll1 = findViewById(R.id.llUno);
        ll2 = findViewById(R.id.llDos);
        gol = findViewById(R.id.gol);
        amarilla = findViewById(R.id.amarilla);
        roja = findViewById(R.id.roja);
        mvp = findViewById(R.id.mvp);

        jugadoresView = new ArrayList<>();
        torneoController = new TorneoController();
    }

    private void listener() {
        gol.setOnClickListener(view -> {
            if(jugadorSeleccionado!=null){
                golJugador(true);
            }
        });

        amarilla.setOnClickListener(view -> {
            if(jugadorSeleccionado!=null){
                tarjetaJugador(true, true);
            }
        });

        roja.setOnClickListener(view -> {
            if(jugadorSeleccionado!=null){
                tarjetaJugador(false, true);
            }
        });

        mvp.setOnClickListener(view -> {
            if(jugadorSeleccionado!=null){
                agregarMvp(false);
            }
        });

    }

    private void agregarMvp(Boolean cargando) {
        if(partido.getMvp()!=null && !cargando) {
            Toast.makeText(this, "No se pueden seleccionar dos MVP. Elimine el anterior", Toast.LENGTH_LONG).show();
            return;
        }
        ImageView mvpIV = crearView(R.drawable.ic_mvp);
        jugadorLayout.addView(mvpIV);
        if(!cargando){
            partido.setMvp(jugadorSeleccionado);
            torneoController.guardarFixture(Globals.getInstance().getFixture(), null);
        }

        mvpIV.setOnClickListener(view -> {
            partido.setMvp(null);
            jugadorLayout.removeView(mvpIV);
        });
    }

    private void golJugador(Boolean agregarGol) {
        ImageView golIV = crearView(R.drawable.ic_gol);
        if(agregarGol)
            jugadorSeleccionado.addGol();
        if(equipo1.getJugadores().contains(jugadorSeleccionado) && agregarGol){
            partido.addGolEquipoUno(jugadorSeleccionado.getId());
            torneoController.guardarEquipo(equipo1,null);
        }

        else if(agregarGol){
            partido.addGolEquipoDos(jugadorSeleccionado.getId());
            torneoController.guardarEquipo(equipo2,null);
        }
        if(agregarGol)
            torneoController.guardarFixture(Globals.getInstance().getFixture(), null);
        jugadorLayout.addView(golIV);

        golIV.setOnClickListener(view -> {
            if(equipo1.getJugadores().contains(jugadorSeleccionado)){
                partido.removeGolEquipoUno(jugadorSeleccionado.getId());
                torneoController.guardarEquipo(equipo1,null);
                torneoController.guardarFixture(Globals.getInstance().getFixture(), new TorneoNotificacion() {
                    @Override
                    public void torneoNotificacion(Boolean exito) {
                        jugadorSeleccionado.removeGol();
                    }
                });
                jugadorSeleccionado.removeGol();
            }
            else{
                partido.removeGolEquipoDos(jugadorSeleccionado.getId());
                torneoController.guardarEquipo(equipo2,null);
                torneoController.guardarFixture(Globals.getInstance().getFixture(), new TorneoNotificacion() {
                    @Override
                    public void torneoNotificacion(Boolean exito) {
                        jugadorSeleccionado.removeGol();
                    }
                });

            }
            jugadorLayout.removeView(golIV);
        });


    }

    private void tarjetaJugador(Boolean amarilla, Boolean agregarAmarilla) {
        ImageView tarjeta;
        if(amarilla)
            tarjeta = crearView(R.drawable.ic_amarilla);
        else
            tarjeta = crearView(R.drawable.ic_roja);


        jugadorLayout.addView(tarjeta);
        Tarjeta t = null;
        if(agregarAmarilla){
            t = new Tarjeta(amarilla, jugadorSeleccionado.getId());
            partido.addTarjeta(t);
            jugadorSeleccionado.addTarjeta(t);
            torneoController.guardarFixture(Globals.getInstance().getFixture(), null);
        }
        else{
            for(Tarjeta tAux : jugadorSeleccionado.getTarjetas())
                if(tAux != null && tAux.getJugadorId() != null && tAux.getJugadorId().equals(jugadorSeleccionado.getId()))
                    t = tAux;
        }

        if(equipo1.getJugadores().contains(jugadorSeleccionado) && agregarAmarilla)
            torneoController.guardarEquipo(equipo1,null);
        else if(agregarAmarilla)
            torneoController.guardarEquipo(equipo2,null);


        Tarjeta finalT = t;
        tarjeta.setOnClickListener(view -> {
            if(agregarAmarilla)
                partido.removeTarjeta(finalT);
            else{
                for(Tarjeta tarjetaAux : partido.getTarjetas()){
                    if(tarjetaAux.getJugadorId().equals(jugadorSeleccionado.getId())){
                        partido.getTarjetas().remove(tarjetaAux);
                        break;
                    }

                }
            }
            jugadorSeleccionado.removeTarjeta(finalT);
            jugadorLayout.removeView(tarjeta);
            torneoController.guardarFixture(Globals.getInstance().getFixture(), null);
            if(equipo1.getJugadores().contains(jugadorSeleccionado))
                torneoController.guardarEquipo(equipo1,null);
            else
                torneoController.guardarEquipo(equipo2,null);

        });
    }

    private ImageView crearView(int drawable){
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        imageView.setImageResource(drawable);
        imageView.setAdjustViewBounds(true);


        int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = widthInDp;
        layoutParams.height = heightInDp;
        imageView.setLayoutParams(layoutParams);

        return imageView;
    }

    private void setValues(){
        for(Equipo equipo : Globals.getInstance().getListaEquipos()){
            if(equipo1 == null)
                equipo1 = equipo;
            else if(equipo2 == null)
                equipo2 = equipo;
        }

        partido = Torneo.getInstance().getPartidoByEquipos(equipo1.getId(), equipo2.getId());
        fechaPartido = Globals.getInstance().getFixture().getFechaNro();
    }

    private void setViews() {
        fecha.setText(fechaPartido);
        escudo1.setImageURI(equipo1.getEscudo());
        escudo2.setImageURI(equipo2.getEscudo());
        nombre1.setText(equipo1.getNombre());
        nombre2.setText(equipo2.getNombre());

    }


    private void setJugadores(){
        for(Jugador j : equipo1.getJugadores()){
            crearVista(j, ll1, false);
        }

        for(Jugador j : equipo2.getJugadores()){
            crearVista(j, ll2, true);
        }
    }

    private void crearVista(Jugador j, LinearLayout ll, Boolean changeView){
        View jugadorView = LayoutInflater.from(this).inflate(R.layout.item_jugador_partido, null);
        TextView jugadorTV;
        LinearLayout ll1 = jugadorView.findViewById(R.id.llUno);
        LinearLayout ll2 = jugadorView.findViewById(R.id.llDos);
        FlexboxLayout layoutActual;

        if(changeView){
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
            layoutActual = jugadorView.findViewById(R.id.agregarItemsDos);
            jugadorTV = jugadorView.findViewById(R.id.jugadorDos);
        }
        else{
            jugadorTV = jugadorView.findViewById(R.id.jugadorUno);
            layoutActual = jugadorView.findViewById(R.id.agregarItemsUno);

        }
        jugadorTV.setText(j.getNombre());
        jugadorTV.setTextColor(getColor(R.color.textColor));
        ll.addView(jugadorView);
        jugadorView.setTag(j.getId());
        jugadorView.setTag(j.getId());
        jugadoresView.add(jugadorView);

        jugadorView.setOnClickListener(view -> {
            limpiarJugadores();
            jugadorTV.setBackgroundResource(R.drawable.view_redondeado_gris);
            jugadorTV.setTag("Resaltado");
            jugadorSeleccionado = j;
            jugadorLayout = layoutActual;
        });
    }

    private void limpiarJugadores(){
        for(View v : jugadoresView){
            TextView jugador1TV = v.findViewById(R.id.jugadorUno);
            TextView jugador2TV = v.findViewById(R.id.jugadorDos);
            if(jugador1TV.getTag()!=null && jugador1TV.getTag().equals("Resaltado")){
                jugador1TV.setBackgroundColor(getResources().getColor(R.color.background));
                jugador1TV.setTag(null);
            }

            else if(jugador2TV.getTag()!=null && jugador2TV.getTag().equals("Resaltado")){
                jugador2TV.setBackgroundColor(getResources().getColor(R.color.background));
                jugador2TV.setTag(null);
            }
        }
    }

    private void verificarPartido() {
        int golesUno = 0;
        int golesDos = 0;
        if(partido.getGolesEquipoUno()!=null)
            golesUno = partido.getGolesEquipoUno().size();
        if(partido.getGolesEquipoDos()!=null)
            golesDos = partido.getGolesEquipoDos().size();
        if(golesUno!=0 || golesDos!=0){
            if(partido.getGolesEquipoUno()!=null){
                for(String jugadorId : partido.getGolesEquipoUno()){
                    Jugador j = equipo1.buscarJugadorPorId(jugadorId);
                    if(j!=null){
                        for(View v: jugadoresView){
                            if(v.getTag().equals(j.getId())){
                                jugadorLayout = v.findViewById(R.id.agregarItemsUno);
                                jugadorSeleccionado = j;
                                golJugador(false);
                            }
                        }

                    }
                }
            }
            if(partido.getGolesEquipoDos()!=null){
                for(String jugadorId : partido.getGolesEquipoDos()){
                    Jugador j = equipo2.buscarJugadorPorId(jugadorId);
                    if(j!=null){
                        for(View v: jugadoresView){
                            if(v.getTag().equals(j.getId())){
                                jugadorLayout = v.findViewById(R.id.agregarItemsDos);
                                jugadorSeleccionado = j;
                                golJugador(false);
                            }
                        }
                    }
                }
            }

        }

        if(partido.getMvp() != null){
            Jugador mvp = partido.getMvp();
            for(View v : jugadoresView){
                if(v.getTag().equals(mvp.getId())){
                    Jugador j = equipo1.buscarJugadorPorId(mvp.getId());
                    if(j == null){
                        j = equipo2.buscarJugadorPorId(mvp.getId());
                        jugadorLayout = v.findViewById(R.id.agregarItemsDos);
                    }
                    else
                        jugadorLayout = v.findViewById(R.id.agregarItemsUno);
                    jugadorSeleccionado = j;
                    agregarMvp(true);
                }
            }
        }

        if(partido.getTarjetas()==null)
            return;

        for(Tarjeta t : partido.getTarjetas()){
            for(Jugador j : equipo1.getJugadores())
                if(j.getTarjetas()!=null)
                    if(t.getJugadorId().equals(j.getId())){
                        for (View v : jugadoresView) {
                            if (v.getTag().equals(j.getId())) {
                                jugadorLayout = v.findViewById(R.id.agregarItemsUno);
                                jugadorSeleccionado = j;
                                tarjetaJugador(t.getAmarilla(), false);
                            }
                        }
                    }
        }
        for(Tarjeta t : partido.getTarjetas()){
            for(Jugador j : equipo2.getJugadores())
                if(j.getTarjetas()!=null){
                    if(t.getJugadorId().equals(j.getId())){
                        for (View v : jugadoresView) {
                            if (v.getTag().equals(j.getId())) {
                                jugadorLayout = v.findViewById(R.id.agregarItemsDos);
                                jugadorSeleccionado = j;
                                tarjetaJugador(t.getAmarilla(), false);
                            }
                        }
                    }
                }

        }

    }

}