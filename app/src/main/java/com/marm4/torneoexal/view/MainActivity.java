package com.marm4.torneoexal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.AuthController;
import com.marm4.torneoexal.controller.TorneoController;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.DescargaExito;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.utility.DescargarImagenUtility;
import com.marm4.torneoexal.view.auth.IniciarSesionActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private ImageView menu;

    private LinearLayout pantallaCarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuth();
    }

    private void checkAuth() {
        AuthController authController = new AuthController();
        pantallaCarga = findViewById(R.id.pantallaCarga);
        if(!authController.isUserLoggedIn()){
            Intent intent = new Intent(MainActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
        }
        else if(Torneo.getInstance().getEquipos().isEmpty() || Torneo.getInstance().getFixtures().isEmpty()){
            pantallaCarga.setVisibility(View.VISIBLE);
            cargarDatos();
        }
        else{
            initUI();
        }
    }

    private void cargarDatos() {
        TorneoController equipoController = new TorneoController();
        equipoController.cargarEquipos(new TorneoNotificacion() {
            @Override
            public void torneoNotificacion(Boolean exito) {

                    equipoController.cargarFixture(new TorneoNotificacion() {
                        @Override
                        public void torneoNotificacion(Boolean exito) {
                            File file = new File(getApplicationContext().getExternalFilesDir(null), "Torneo-Exal");
                            int i = 1;
                            for(Equipo equipo : Torneo.getInstance().getEquipos()){
                                String fileName = equipo.getId() + ".png";
                                DescargarImagenUtility.getEscudo(file, getApplicationContext(), fileName, equipo.getUrlEscudo(), i==Torneo.getInstance().getEquipos().size(), new DescargaExito() {
                                    @Override
                                    public void descargaExito(Uri uri, Boolean retorno) {
                                        equipo.setEscudo(uri);
                                        ordenarPartidos();
                                        if(retorno){
                                            List<Partido> partidos = null;
                                            for(Fixture fixture : Torneo.getInstance().getFixtures())
                                                if(fixture.getFechaNro().equals("Fecha 1"))
                                                    partidos = fixture.getPartidos();


                                            Adapters.getInstance().setListPartidos(partidos);
                                            initUI();

                                        }

                                    }
                                });
                                i++;

                            }
                            if(Torneo.getInstance().getEquipos().size() == 0)
                                initUI();
                        }
                    });
            }
        });
    }

    private void ordenarPartidos() {
        for(Fixture f : Torneo.getInstance().getFixtures()){
            Collections.sort(f.getPartidos(), new Comparator<Partido>() {
                @Override
                public int compare(Partido p1, Partido p2) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    try {
                        Date fechaP1 = dateFormat.parse(p1.getDiaHora());
                        Date fechaP2 = dateFormat.parse(p2.getDiaHora());
                        return fechaP1.compareTo(fechaP2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }
    }

    private void initUI() {
        pantallaCarga.setVisibility(View.GONE);

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        navView = findViewById(R.id.botNavView);
        navView.setVisibility(View.VISIBLE);
        NavigationUI.setupWithNavController(navView, navController);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        });
    }
}