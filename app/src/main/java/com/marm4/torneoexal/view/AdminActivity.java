package com.marm4.torneoexal.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.view.admin.CrearClubActivity;
import com.marm4.torneoexal.view.admin.CrearFixtureActivity;
import com.marm4.torneoexal.view.admin.VerClubesActivity;

public class AdminActivity extends AppCompatActivity {

    private TextView agregarEquipo;
    private TextView agregarFixture;
    private TextView verEquipo;
    private TextView verFixture;

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
    }
}