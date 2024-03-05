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

public class IniciarSesionActivity extends AppCompatActivity {

    private EditText email;
    private EditText contraseña;
    private TextView iniciarSesion;
    private TextView registrarse;
    private TextView invitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        initUI();
    }

    private void initUI() {
        email = findViewById(R.id.email);
        contraseña = findViewById(R.id.contraseña);
        iniciarSesion = findViewById(R.id.iniciarSesion);
        registrarse = findViewById(R.id.registrarse);
        invitado = findViewById(R.id.invitado);

        listeners();

    }

    private void listeners() {
        TextView mensaje = findViewById(R.id.mensaje);
        AuthController authController = new AuthController();

        iniciarSesion.setOnClickListener(view -> {
            String emailString = email.getText().toString();
            String contraseñaString = contraseña.getText().toString();
            if(emailString.isEmpty() || contraseñaString.isEmpty()){
                mensaje.setText("Complete los campos");
                mensaje.setVisibility(View.VISIBLE);
            }
            else{
                authController.logIn(emailString, contraseñaString, new InicioSesionExito() {
                    @Override
                    public void inicioSesionExito(Boolean exito) {
                        if(exito){
                            Intent intent = new Intent(IniciarSesionActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            mensaje.setText("Error al iniciar sesión");
                            mensaje.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        registrarse.setOnClickListener(view -> {
            Intent intent = new Intent(IniciarSesionActivity.this, RegistrarseActivity.class);
            startActivity(intent);
        });

        /*invitado.setOnClickListener(view -> {
            authController.anonymousUser();
            Intent intent = new Intent(IniciarSesionActivity.this, MainActivity.class);
            startActivity(intent);
        });*/
    }
}