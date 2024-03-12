package com.marm4.torneoexal.view.fixture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.VistaPartidosAdapter;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.model.Fixture;

public class FixtureActivity extends AppCompatActivity {

    private TextView fecha;
    private Fixture fixture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture);
        initUI();
    }

    private void initUI() {
        fecha = findViewById(R.id.fecha);
        fixture = Globals.getInstance().getFixture();
        fecha.setText(fixture.getFechaNro());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VistaPartidosAdapter adapter = new VistaPartidosAdapter();
        Adapters.getInstance().setPartidosAdapter(adapter);
        recyclerView.setAdapter(adapter);
        adapter.setList(fixture.getPartidos());
    }
}