package com.marm4.torneoexal.view.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.TorneoController;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Partido;
import com.marm4.torneoexal.view.AdminActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CrearFixtureActivity extends AppCompatActivity {
    private TextView fecha;

    private TextView guardar;
    private String[] fechas;
    private ImageView agregar;
    private LinearLayout equipoLL;
    private AlertDialog alertDialogFechas;
    private AlertDialog alertDialogEquipos;
    private String[] equipos;
    private List<View> equiposViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_fixture);

        initUI();
    }

    private void initUI() {
        fecha = findViewById(R.id.fecha);
        agregar = findViewById(R.id.agregarIV);
        equipoLL = findViewById(R.id.equiposLL);

        guardar = findViewById(R.id.guardar);
        equiposViewList = new ArrayList<>();

        configEquipos();
        configFechas();
        listener();
    }



    private void listener() {

        fecha.setOnClickListener(view -> {
            alertDialogFechas.show();

        });

        agregar.setOnClickListener(view -> {
            agregarLayout();
        });

        guardar.setOnClickListener(view -> {
            guardarPartidos();
        });
    }



    private void guardarPartidos() {
        List<Partido> partidos = new ArrayList<>();
        Fixture fixture = Torneo.getInstance().getFixtureByFecha(fecha.getText().toString());
        if(fixture == null)
            fixture = new Fixture(fecha.getText().toString(), partidos);
        else{
            for(Partido partido : partidos){
                fixture.addPartidos(partido);
            }
        }
        Torneo.getInstance().addFixtures(fixture);

        if(fecha.getText().toString().isEmpty()){
            Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        for(View v : equiposViewList){
            TextView equipoUnoAux = v.findViewById(R.id.equipoUNO);
            TextView equipoDosAux = v.findViewById(R.id.equipoDOS);
            TextView dia = v.findViewById(R.id.diaTV);
            String eUnoString = equipoUnoAux.getText().toString();
            String eDosString = equipoDosAux.getText().toString();

            if((eUnoString.isEmpty() && !eDosString.isEmpty()) || (!eUnoString.isEmpty() && eDosString.isEmpty())){
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            Partido partido = new Partido();
            String diaString = dia.getText().toString();
            if(!diaString.isEmpty())
                partido.setDiaHora(diaString);
            for(Equipo equipo : Torneo.getInstance().getEquipos()){
                if(equipo.getNombre().equals(eUnoString))
                    partido.setEquipoUno(equipo.getId());

                else if(equipo.getNombre().equals(eDosString))
                    partido.setEquipoDos(equipo.getId());
            }
            partido.setDiaHora(dia.getText().toString());
            fixture.addPartidos(partido);
        }

        LinearLayout pantallaCarga = findViewById(R.id.pantallaCarga);
        pantallaCarga.setVisibility(View.VISIBLE);
        guardar.setVisibility(View.GONE);

        guardarVolver(fixture);
    }

    private void guardarVolver(Fixture fixture) {
        TorneoController torneoController = new TorneoController();
        torneoController.guardarFixture(fixture, new TorneoNotificacion() {
            @Override
            public void torneoNotificacion(Boolean exito) {
                if(exito){
                    Intent intent = new Intent(CrearFixtureActivity.this, AdminActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void agregarLayout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View equipoLayout = inflater.inflate(R.layout.layout_equipo_horario, equipoLL, false);
        TextView equipoUnoTV = equipoLayout.findViewById(R.id.equipoUNO);
        TextView equipoDosTV = equipoLayout.findViewById(R.id.equipoDOS);
        TextView diaTV = equipoLayout.findViewById(R.id.diaTV);
        TextView borrarTV = equipoLayout.findViewById(R.id.borrar);

        listenerParaEquipo(equipoUnoTV);
        listenerParaEquipo(equipoDosTV);
        listenerParaDia(diaTV);

        borrarTV.setOnClickListener(view -> {
            equiposViewList.remove(equipoLayout);
            equipoLL.removeView(equipoLayout);
            if(!equipoUnoTV.getText().toString().isEmpty()){
                agregarEquipoALista(equipoUnoTV.getText().toString());
            }
            if(!equipoDosTV.getText().toString().isEmpty()){
                agregarEquipoALista(equipoDosTV.getText().toString());
            }
        });

        equiposViewList.add(equipoLayout);
        equipoLL.addView(equipoLayout, equipoLL.getChildCount()-1);
    }

    private void agregarEquipoALista(String nuevoEquipo) {
        String[] equiposAux = new String[equipos.length+1];
        int i = 0;
        for(String e : equipos){
            equiposAux[i] = e;
            i++;
        }
        equiposAux[i] = nuevoEquipo;
        equipos = equiposAux;
    }

    private void listenerParaEquipo(TextView tv){
        tv.setOnClickListener(view -> {
            mostrarEquipos(tv);
        });
    }

    private void configFechas() {
        fechas = new String[50];
        for(int i=1; i<51; i++) {
            fechas[i-1] = "Fecha " + i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione una fecha");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fechas);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedDate = fechas[which];
                fecha.setText(selectedDate);
                fecha.setTextColor(getResources().getColor(R.color.textColor));
                actualizarListaEquipos();
            }
        });
        alertDialogFechas = builder.create();
    }

    private void actualizarListaEquipos() {
        Fixture fixture = Torneo.getInstance().getFixtureByFecha(fecha.getText().toString());
        if(fixture==null)
            return;

        String[] equiposAux = new String[Torneo.getInstance().getEquipos().size() - fixture.getPartidos().size()*2];
        int i=0;
        for(Equipo equipo : Torneo.getInstance().getEquipos()){
            Boolean contieneEquipo = false;
            for(Partido partido : fixture.getPartidos()){
                if(partido.getEquipoUno().equals(equipo.getId()) || partido.getEquipoDos().equals(equipo.getId()))
                    contieneEquipo = true;
            }
            if(!contieneEquipo){
                Log.i("tag", "equipo : " + equipo.getNombre());
                equiposAux[i] = equipo.getNombre();
                i++;
            }
        }
        equipos = equiposAux;
    }

    private void mostrarEquipos(TextView equipoTV){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione un equipo");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, equipos);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int posicion) {
                String equipoElegido = equipos[posicion];
                equipoTV.setText(equipoElegido);
                equipoTV.setTextColor(getResources().getColor(R.color.textColor));
                borrarEquipo(posicion);
            }
        });
        alertDialogEquipos = builder.create();
        alertDialogEquipos.show();
    }


    private void borrarEquipo(int posicion) {
        String[] equiposAux = new String[equipos.length - 1];
        for (int i = 0, j = 0; i < equipos.length; i++) {
            if (i != posicion) {
                equiposAux[j++] = equipos[i];
            }
        }
        equipos = equiposAux;
    }

    private void configEquipos() {
        List<Equipo> equiposList = Torneo.getInstance().getEquipos();
        equipos = new String[equiposList.size()];

        int i=0;
        for(Equipo equipo : equiposList){
            equipos[i] = equipo.getNombre();
            i++;
        }
    }

    private void listenerParaDia(TextView tv){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                Toast.makeText(CrearFixtureActivity.this, "Fecha seleccionada: " + selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear, Toast.LENGTH_SHORT).show();
            }
        }, year, month, dayOfMonth);

        datePickerDialog.setOnDateSetListener((view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth);

            TimePickerDialog timePickerDialog = new TimePickerDialog(CrearFixtureActivity.this, (timePicker, selectedHourOfDay, selectedMinute) -> {

                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(selectedYear, selectedMonth, selectedDayOfMonth, selectedHourOfDay, selectedMinute);


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                String selectedDateTimeString = dateFormat.format(selectedDateTime.getTime());
                tv.setText(selectedDateTimeString);
                tv.setTextColor(getResources().getColor(R.color.textColor));
            }, hourOfDay, minute, true);

            timePickerDialog.show();
        });
        tv.setOnClickListener(view -> {
            datePickerDialog.show();
        });

    }


}