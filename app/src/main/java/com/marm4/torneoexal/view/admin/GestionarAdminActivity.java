package com.marm4.torneoexal.view.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.ListaAdapter;
import com.marm4.torneoexal.controller.AuthController;
import com.marm4.torneoexal.listener.DevolverUsuarios;
import com.marm4.torneoexal.model.Usuario;

import java.util.List;

public class GestionarAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_admin);

        initUI();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListaAdapter adapter = new ListaAdapter(this);
        recyclerView.setAdapter(adapter);
        setUsuarios(adapter);
    }

    private void setUsuarios(ListaAdapter adapter){
        AuthController authController = new AuthController();
        authController.getAllUsers(new DevolverUsuarios() {
            @Override
            public void devolverUsuarios(List<Usuario> usuarios) {
                adapter.setList(usuarios);
            }
        });
    }
}