package com.marm4.torneoexal.view;




import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.AuthController;
import com.marm4.torneoexal.controller.TorneoController;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.DescargaExito;
import com.marm4.torneoexal.listener.DevolverUsuarios;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.model.Usuario;
import com.marm4.torneoexal.service.AuthService;
import com.marm4.torneoexal.utility.DescargarImagenUtility;
import com.marm4.torneoexal.view.admin.AdminActivity;
import com.marm4.torneoexal.view.club.VerClubesActivity;
import com.marm4.torneoexal.view.fixture.VerFixtureActivity;
import com.marm4.torneoexal.view.auth.IniciarSesionActivity;
import com.marm4.torneoexal.view.nav.EstadisticasFragment;
import com.marm4.torneoexal.view.nav.PartidosFragment;
import com.marm4.torneoexal.view.nav.PosicionesFragment;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private LinearLayout pantallaCarga;
    private static final int TIME_INTERVAL = 2000;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkAuth();
    }

    private void checkAuth() {
        AuthController authController = new AuthController();
        pantallaCarga = findViewById(R.id.pantallaCarga);
        setSharedPreferences(false);
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
        TorneoController torneoController = new TorneoController();

        torneoController.cargarEquipos(new TorneoNotificacion() {
            @Override
            public void torneoNotificacion(Boolean exito) {

                    torneoController.cargarFixture(new TorneoNotificacion() {
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

                                            AuthController authController = new AuthController();
                                            Adapters.getInstance().setListPartidos(partidos);
                                            torneoController.getProximaFecha();
                                            authController.setUser(new DevolverUsuarios() {
                                                @Override
                                                public void devolverUsuarios(List<Usuario> usuarios) {
                                                    initUI();
                                                }
                                            });

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
        if(Globals.getInstance().getSharedPreferences(this).getBoolean("view initialized", false))
            return;

        getWindow().setBackgroundDrawableResource(R.drawable.view_top_title);
        pantallaCarga.setVisibility(View.GONE);

        navView = findViewById(R.id.nav_view);
        navView.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Posiciones");

        navView.setItemBackgroundResource(R.drawable.navigation_item);
        navView.setItemTextColor(colorState());
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(Globals.getInstance().getUsuario()==null ){
            AuthService authService = new AuthService();
            authService.getUsuarios(true, new DevolverUsuarios() {
                @Override
                public void devolverUsuarios(List<Usuario> usuarios) {
                    if(!Globals.getInstance().getUsuario().isAdmin()) {
                        MenuItem adminMenuItem = navView.getMenu().findItem(R.id.adminActivity);
                        adminMenuItem.setVisible(false);
                    }
                }
            });
        }
        else if(!Globals.getInstance().getUsuario().isAdmin()){
            MenuItem adminMenuItem = navView.getMenu().findItem(R.id.adminActivity);
            adminMenuItem.setVisible(false);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PosicionesFragment()).commit();
        navView.setCheckedItem(R.id.posicionesFragment);
        navView.setNavigationItemSelectedListener(this);
        navView.bringToFront();
        setSharedPreferences(true);
    }

    private ColorStateList colorState(){
        ColorStateList colorSelector = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.primaryVariation),
                        Color.BLACK
                }
        );
        return colorSelector;
    }

    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.partidosFragment){
            replaceFragment(new PartidosFragment());
            setTitle("Partidos");
        }

        else if(item.getItemId() == R.id.posicionesFragment){
            replaceFragment(new PosicionesFragment());
            setTitle("Posiciones");
        }

        else if(item.getItemId() == R.id.estadisticasFragment){
            replaceFragment(new EstadisticasFragment());
            setTitle("Estadisticas");
        }

        else if(item.getItemId() == R.id.adminActivity){
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.equiposActivity){
            Intent intent = new Intent(MainActivity.this, VerClubesActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.fixtureActivity){
            Intent intent = new Intent(MainActivity.this, VerFixtureActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.logOut){
            AuthController authController = new AuthController();
            authController.logOut();
            Intent intent = new Intent(MainActivity.this, IniciarSesionActivity.class);
            startActivity(intent);
        }


        return true;
    }

    private void setSharedPreferences(boolean value){
        SharedPreferences.Editor editor = Globals.getInstance().getSharedPreferences(this).edit();
        editor.putBoolean("view initialized", value);
        editor.apply();
    }



    @Override
    public void onBackPressed() {
        if (backPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        }
        backPressedTime = System.currentTimeMillis();
    }


    public void setTitle(String title){
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new TypefaceSpan("sf_regular.ttf"), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(this, R.style.forToolBar);
        spannableString.setSpan(textAppearanceSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
    }

}
