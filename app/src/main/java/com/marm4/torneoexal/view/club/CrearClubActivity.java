package com.marm4.torneoexal.view.club;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.service.TorneoService;
import com.marm4.torneoexal.view.admin.AdminActivity;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;


public class CrearClubActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 1;
    private EditText nombre;
    private ImageView nombreCheck;
    private TextView colorUno;
    private TextView colorDos;
    private ImageView escudo;
    private ImageView escudoCheck;
    private LinearLayout jugadoresLL;
    private LinearLayout agregarJugadorLL;
    private List<View> jugadoresView;
    private TextView guardar;
    private Uri escudoUri;
    private Equipo equipo;
    private List<Integer> colores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_club);


        initUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            escudoUri = data.getData();
            escudo.setImageURI(escudoUri);
            escudoCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
        }
    }


    private void initUI() {
        findViews();
        listeners();

        boolean equipoExistente = getIntent().getBooleanExtra("equipoExistente", false);

        if(equipoExistente){
            equipo = Globals.getInstance().getEquipo();
            nombre.setText(equipo.getNombre());
            if(equipo.getEscudo()!=null)
                escudo.setImageURI(equipo.getEscudo());

            if(equipo.getColores()!=null && !equipo.getColores().isEmpty()) {
                colorUno.setBackgroundColor(equipo.getColores().get(0));
                if (equipo.getColores().size() > 1)
                    colorDos.setBackgroundColor(equipo.getColores().get(1));
            }

            if(equipo.getJugadores()!=null) {
                for (Jugador j : equipo.getJugadores())
                    agregarViewJugador(j);
            }
        }
        else{
            equipo = new Equipo();
            equipo.setJugadores(new ArrayList<>());
        }
        agregarViewJugador(null);

    }

    private void findViews() {
        nombre = findViewById(R.id.nombre);
        nombreCheck = findViewById(R.id.nombreCheck);
        colorUno = findViewById(R.id.colorUno);
        colorDos = findViewById(R.id.colorDos);
        escudo = findViewById(R.id.escudo);
        escudoCheck = findViewById(R.id.escudoCheck);
        jugadoresLL = findViewById(R.id.jugadoresLL);
        agregarJugadorLL = findViewById(R.id.agregarJugadorLL);
        guardar = findViewById(R.id.guardar);
        jugadoresView = new ArrayList<>();
        colores = new ArrayList<>();
    }



    private void listeners() {
        agregarJugadorLL.setOnClickListener(view -> {
            agregarViewJugador(null);
        });

        escudo.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Selecciona un escudo"), PICK_IMAGE_REQUEST);
        });

        guardar.setOnClickListener(view -> {
            guardarEquipo();
        });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(nombre.getText().toString().isEmpty())
                    nombreCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
                else{
                    nombreCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        colorUno.setOnClickListener(view -> {
            showColorPickerDialog(colorUno);
        });

        colorDos.setOnClickListener(view -> {
            showColorPickerDialog(colorDos);
        });
    }


    public void showColorPickerDialog(View view) {
        AmbilWarnaDialog colorPickerDialog = new AmbilWarnaDialog(this, getColor(R.color.grisOscuro), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(), "Color seleccionado", Toast.LENGTH_SHORT).show();
                view.setBackgroundColor(color);
                escudoCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
                colores.add(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }
        });
        colorPickerDialog.show();
    }


    private void guardarEquipo() {
        if(nombre.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese nombre equipo", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText jugadorCero = jugadoresView.get(0).findViewById(R.id.jugador);
        if(jugadorCero.getText().toString().isEmpty()){
            Toast.makeText(this, "Ingrese al menos un jugador", Toast.LENGTH_SHORT).show();
            return;
        }

        findViewById(R.id.pantallaCarga).setVisibility(View.VISIBLE);
        equipo.setNombre(nombre.getText().toString());
        if(escudoUri!=null){
            equipo.setEscudo(escudoUri);
            equipo.setUrlEscudo(null);
        }
        if(!colores.isEmpty())
            equipo.setColores(colores);


        for(View v : jugadoresView){
            EditText et = v.findViewById(R.id.jugador);
            if(v.getTag()==null || v.getTag().toString().isEmpty()){
                if(!et.getText().toString().isEmpty()){
                    Jugador jugador = new Jugador();
                    jugador.setNombre(et.getText().toString());
                    equipo.addJugador(jugador);
                }
            } else if (v.getTag()!=null) {
                String jugadorId = v.getTag().toString();
                for(Jugador j : equipo.getJugadores()){
                    if(j.getId().equals(jugadorId)){
                        j.setNombre(et.getText().toString());
                    }
                }
            }

        }
        TorneoService torneoService = new TorneoService();
        torneoService.guardarEquipo(equipo, new TorneoNotificacion() {
            @Override
            public void torneoNotificacion(Boolean exito) {
                findViewById(R.id.pantallaCarga).setVisibility(View.GONE);
                Intent intent = new Intent(CrearClubActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void agregarViewJugador(Jugador jugador) {
        View itemJugadorView = LayoutInflater.from(this).inflate(R.layout.item_jugador_crear_equipo, null);
        jugadoresLL.addView(itemJugadorView, jugadoresLL.getChildCount()-1);
        jugadoresView.add(itemJugadorView);

        EditText nombreJugador = itemJugadorView.findViewById(R.id.jugador);
        ImageView borrar = itemJugadorView.findViewById(R.id.borrarJugador);


        if(jugador!=null){
            nombreJugador.setText(jugador.getNombre());
            itemJugadorView.setTag(jugador.getId());

            borrar.setOnClickListener(view -> {
                equipo.getJugadores().remove(jugador);
                jugadoresLL.removeView(itemJugadorView);
                jugadoresView.remove(itemJugadorView);
                itemJugadorView.setTag(null);
            });
        }
        else {
            borrar.setOnClickListener(view -> {
                jugadoresLL.removeView(itemJugadorView);
                jugadoresView.remove(itemJugadorView);
            });
        }

    }


}