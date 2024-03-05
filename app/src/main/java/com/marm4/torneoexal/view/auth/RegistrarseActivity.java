package com.marm4.torneoexal.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.marm4.torneoexal.R;
import com.marm4.torneoexal.controller.AuthController;
import com.marm4.torneoexal.listener.InicioSesionExito;
import com.marm4.torneoexal.view.MainActivity;

public class RegistrarseActivity extends AppCompatActivity {

    private EditText email;
    private EditText contraseña;
    private EditText repetirContraseña;
    private TextView registrarse;
    private TextView mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        initUI();
    }

    private void initUI() {
        email = findViewById(R.id.email);
        contraseña = findViewById(R.id.contraseña);
        repetirContraseña = findViewById(R.id.repetirContraseña);
        registrarse = findViewById(R.id.registrarse);
        mensaje = findViewById(R.id.mensaje);

        listener();
    }

    private void listener() {
        registrarse.setOnClickListener(view -> {
            String emailString = email.getText().toString();
            String contraseñaString = contraseña.getText().toString();
            String repetirContraseñaString = repetirContraseña.getText().toString();

            if(emailString.isEmpty() || contraseñaString.isEmpty()){
                mensaje.setText("Complete todos los campos");
                mensaje.setVisibility(View.VISIBLE);
            }
            else if((!contraseñaString.equals(repetirContraseñaString))){
                mensaje.setText("Las contraseñas no coinciden");
                mensaje.setVisibility(View.VISIBLE);
            }
            else {
                registrar(emailString, contraseñaString);
            }
        });
    }

    private void registrar(String email, String contraseña) {
        AuthController authController = new AuthController();
        authController.signUp(email, contraseña, new InicioSesionExito() {
            @Override
            public void inicioSesionExito(Boolean exito) {
                if(exito){
                    Intent intent = new Intent(RegistrarseActivity.this, MainActivity.class)     ;
                    startActivity(intent);
                }
                else{
                    mensaje.setText("Error al registrarse");
                    mensaje.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}