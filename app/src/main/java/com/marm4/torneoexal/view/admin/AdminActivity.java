package com.marm4.torneoexal.view.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.view.MainActivity;
import com.marm4.torneoexal.view.admin.club.CrearClubActivity;
import com.marm4.torneoexal.view.admin.fixture.CrearFixtureActivity;
import com.marm4.torneoexal.view.admin.club.VerClubesActivity;
import com.marm4.torneoexal.view.admin.fixture.VerFixtureActivity;

public class AdminActivity extends AppCompatActivity {

    private TextView agregarEquipo;
    private TextView agregarFixture;
    private TextView verEquipo;
    private TextView verFixture;

    private TextView gestionarAdmins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initUI();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void initUI() {
        agregarEquipo = findViewById(R.id.agregarEquipo);
        agregarFixture = findViewById(R.id.agregarFixture);
        verEquipo = findViewById(R.id.verEquipo);
        verFixture = findViewById(R.id.verFixture);
        gestionarAdmins = findViewById(R.id.admins);

        agregarEquipo.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, CrearClubActivity.class);
            startActivity(intent);
        });

        agregarFixture.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, CrearFixtureActivity.class);
            startActivity(intent);
        });

        verEquipo.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, VerClubesActivity.class);
            startActivity(intent);
        });

        verFixture.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, VerFixtureActivity.class);
            startActivity(intent);
        });


        gestionarAdmins.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, GestionarAdminActivity.class);
            startActivity(intent);
        });
    }
}