package com.marm4.torneoexal.view.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.ListaEquiposAdapter;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Equipo;

import java.util.ArrayList;
import java.util.List;

public class VerClubesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListaEquiposAdapter adapter;

    private EditText buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_clubes);
        initUI();
    }

    private void initUI() {
        initRecycler();

        buscar = findViewById(R.id.buscar);
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    String input = charSequence.toString();
                    String firstLetter = input.substring(0, 1).toUpperCase();
                    String restOfWord = input.substring(1).toLowerCase();
                    buscarEquipos(firstLetter + restOfWord);
                }
                else{
                    adapter.setList(Torneo.getInstance().getEquipos());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void buscarEquipos(String equipoString) {
        List<Equipo> equipoList = new ArrayList<>();
        List<Equipo> equipos = Torneo.getInstance().getEquipos();

        for(Equipo equipo : equipos){
            if(equipo.getNombre().contains(equipoString))
                equipoList.add(equipo);
        }
        if(equipoList.size()>0){
            adapter.setList(equipoList);
        }
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListaEquiposAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(Torneo.getInstance().getEquipos());
    }

}