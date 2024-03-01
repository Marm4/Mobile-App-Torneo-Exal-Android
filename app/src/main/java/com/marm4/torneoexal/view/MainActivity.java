package com.marm4.torneoexal.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.marm4.torneoexal.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        navView = findViewById(R.id.botNavView);
        NavigationUI.setupWithNavController(navView, navController);
    }
}