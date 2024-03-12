package com.marm4.torneoexal.view.fixture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.ListaAdapter;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Fixture;

import java.util.ArrayList;
import java.util.List;

public class VerFixtureActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_fixture);

        initRecycler();
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListaAdapter adapter = new ListaAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setList(Torneo.getInstance().getFixtures());
        Adapters.getInstance().setListaAdapter(adapter);

    }
}