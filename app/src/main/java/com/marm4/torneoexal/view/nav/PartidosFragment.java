package com.marm4.torneoexal.view.nav;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.adapter.VistaPartidosAdapter;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Partido;

import java.util.List;

public class PartidosFragment extends Fragment {
    private View root;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_partidos, container, false);

        initUI();
        return root;
    }

    private void setFixture() {
        if(Torneo.getInstance().getFixtures().isEmpty())
            return;
        List<Partido> partidos = null;
        for(Fixture fixture : Torneo.getInstance().getFixtures())
            if(fixture.getFechaNro().equals("Fecha 1"))
                partidos = fixture.getPartidos();

        Adapters.getInstance().setListPartidos(partidos);
    }

    private void initUI() {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        VistaPartidosAdapter adapter = new VistaPartidosAdapter();
        Adapters.getInstance().setPartidosAdapter(adapter);
        recyclerView.setAdapter(adapter);
        setFixture();
    }
}