package com.marm4.torneoexal.view.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.view.MainActivity;
import com.marm4.torneoexal.view.club.CrearClubActivity;
import com.marm4.torneoexal.view.fixture.CrearFixtureActivity;
import com.marm4.torneoexal.view.club.VerClubesActivity;
import com.marm4.torneoexal.view.fixture.VerFixtureActivity;

public class AdminActivity extends AppCompatActivity {

    private LinearLayout agregarEquipo;
    private LinearLayout agregarFixture;

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

        agregarEquipo.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, CrearClubActivity.class);
            startActivity(intent);
        });

        agregarFixture.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, CrearFixtureActivity.class);
            startActivity(intent);
        });
    }
}