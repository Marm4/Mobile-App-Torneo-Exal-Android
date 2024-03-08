package com.marm4.torneoexal.view.admin.club;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.TorneoController;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.utility.ImagenUtility;
import com.marm4.torneoexal.view.admin.AdminActivity;

import java.util.ArrayList;
import java.util.List;


public class CrearClubActivity extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 1;

    private EditText nombreEquipo;
    private TextView escudoTV;
    private EditText jugadores;
    private ImageView agregarJugadores;
    private ImageView escudoIV;
    private LinearLayout jugadoresLL;
    private TextView guardar;
    private Uri escudo;
    private List<EditText> jugadoresList;

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
            escudo = ImagenUtility.reduceSize(this, data.getData(), 500);
            escudoIV.setImageURI(escudo);
        }
    }


    private void initUI() {
        nombreEquipo = findViewById(R.id.nombre);
        escudoTV = findViewById(R.id.escudo);
        jugadores = findViewById(R.id.jugadores);
        agregarJugadores = findViewById(R.id.agregarJugadores);
        jugadoresLL = findViewById(R.id.jugadoresLL);
        guardar = findViewById(R.id.guardar);
        escudoIV = findViewById(R.id.escudoIV);

        jugadoresList = new ArrayList<>();
        jugadoresList.add(jugadores);

        listeners();
    }

    private void listeners() {
        escudoTV.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        agregarJugadores.setOnClickListener(view -> {
                jugadoresLL.addView(agregarJugador(R.color.background), jugadoresLL.getChildCount());
        });

        guardar.setOnClickListener(view -> {
            guardarEquipo();
        });
    }


    private void guardarEquipo() {
        String nombreString = nombreEquipo.getText().toString();
        if(nombreString.isEmpty()){
            Toast.makeText(this, "El equipo debe tener un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        int jugadoresCreado = 0;

        Equipo equipo = new Equipo();
        String nombreFormateado = nombreString.substring(0, 1).toUpperCase() + nombreString.substring(1).toLowerCase();

        equipo.setNombre(nombreFormateado);
        equipo.setEscudo(escudo);
        for(EditText et : jugadoresList){
            String jugadorString = et.getText().toString();
            if(!jugadorString.isEmpty()){
                Jugador jugador = new Jugador();
                jugador.setNombre(jugadorString);
                equipo.addJugador(jugador);
                jugadoresCreado++;
            }
        }

        if(jugadoresCreado==0){
            Toast.makeText(this, "El equipo debe tener al menos un jugador", Toast.LENGTH_SHORT).show();
            equipo = null;
            return;
        }
        LinearLayout pantallaCarga = findViewById(R.id.pantallaCarga);
        pantallaCarga.setVisibility(View.VISIBLE);

        Torneo.getInstance().addEquipo(equipo);
        TorneoController equipoController = new TorneoController();
        equipoController.guardarEquipo(equipo, new TorneoNotificacion() {
            @Override
            public void torneoNotificacion(Boolean exito) {
                Intent intent = new Intent(CrearClubActivity.this, AdminActivity.class);
                if(exito){
                    startActivity(intent);
                }
                else {
                    startActivity(intent);
                }
            }
        });
    }

    private EditText agregarJugador(int colorFondo){
        EditText editText = new EditText(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                toPx(250),
                toPx(40));

        params.gravity = Gravity.CENTER;
        params.setMargins(toPx(7), 0, 0, 0);
        editText.setLayoutParams(params);

        editText.setBackgroundResource(colorFondo);
        editText.setHint("Escribe aquí");
        editText.setHintTextColor(getResources().getColor(R.color.textColorHint));
        editText.setPadding(0, toPx(25), 0, 0);

        editText.setTextAppearance(R.style.textStyle);
        editText.setTextColor(getResources().getColor(R.color.textColor));
        editText.setTextSize(12);
        editText.setSingleLine(true);

        jugadoresList.add(editText);
        return editText;
    }

    private int toPx(int value){
        int heightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                value,
                getResources().getDisplayMetrics()
        );
        return heightInPixels;
    }

}