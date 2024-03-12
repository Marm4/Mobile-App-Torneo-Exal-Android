package com.marm4.torneoexal.view.fixture;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.marm4.torneoexal.view.admin.AdminActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CrearFixtureActivity extends AppCompatActivity {
    private TextView fecha;

    private TextView guardar;
    private String[] fechas;
    private LinearLayout agregarPartidoLL;
    private LinearLayout partidosLL;
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
        agregarPartidoLL = findViewById(R.id.agregarPartidoLL);
        guardar = findViewById(R.id.guardar);
        partidosLL = findViewById(R.id.partidosLL);

        equiposViewList = new ArrayList<>();

        configEquipos();
        configFechas();
        listener();
        agregarLayout();
    }



    private void listener() {
        ImageView nombreCheck = findViewById(R.id.nombreCheck);
        fecha.setOnClickListener(view -> {
            alertDialogFechas.show();
            nombreCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
        });

        agregarPartidoLL.setOnClickListener(view -> {
            agregarLayout();
        });

        guardar.setOnClickListener(view -> {
            guardarPartidos();
        });
    }



    private void guardarPartidos() {
        if(equiposViewList.isEmpty())
            return;

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
            else if(!eUnoString.isEmpty() && !eDosString.isEmpty()){
                Toast.makeText(this, "No se puede crear un fixture vacio", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
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
        View equipoLayout = inflater.inflate(R.layout.item_equipo_horario_fixture, partidosLL, false);
        TextView equipoUnoTV = equipoLayout.findViewById(R.id.equipoUNO);
        TextView equipoDosTV = equipoLayout.findViewById(R.id.equipoDOS);
        ImageView checkUno = equipoLayout.findViewById(R.id.equipoUnoCheck);
        ImageView checkDos = equipoLayout.findViewById(R.id.equipoDosCheck);
        ImageView diaHoraCheck = equipoLayout.findViewById(R.id.diaHoraCheck);
        TextView diaTV = equipoLayout.findViewById(R.id.diaTV);
        ImageView borrarIV = equipoLayout.findViewById(R.id.borrar);

        listenerParaEquipo(equipoUnoTV, checkUno);
        listenerParaEquipo(equipoDosTV, checkDos);
        listenerParaDia(diaTV, diaHoraCheck);

        borrarIV.setOnClickListener(view -> {
            equiposViewList.remove(equipoLayout);
            partidosLL.removeView(equipoLayout);
            if(!equipoUnoTV.getText().toString().isEmpty()){
                agregarEquipoALista(equipoUnoTV.getText().toString());
            }
            if(!equipoDosTV.getText().toString().isEmpty()){
                agregarEquipoALista(equipoDosTV.getText().toString());
            }
        });

        equiposViewList.add(equipoLayout);
        partidosLL.addView(equipoLayout, partidosLL.getChildCount()-1);
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

    private void listenerParaEquipo(TextView tv, ImageView check){
        tv.setOnClickListener(view -> {
            mostrarEquipos(tv, check);
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

    private void mostrarEquipos(TextView equipoTV, ImageView check){
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
                check.setImageDrawable(getDrawable(R.drawable.ic_check));

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

    private void listenerParaDia(TextView tv, ImageView diaHoraCheck){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                //Toast.makeText(CrearFixtureActivity.this, "Fecha seleccionada: " + selectedDayOfMonth + "/" + (selectedMonth + 1) + "/" + selectedYear, Toast.LENGTH_SHORT).show();
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
                diaHoraCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
            }, hourOfDay, minute, true);

            timePickerDialog.show();
        });
        tv.setOnClickListener(view -> {
            datePickerDialog.show();
        });

    }


}