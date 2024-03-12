package com.marm4.torneoexal.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView emailCheck;
    private ImageView contraseñaCheck;

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
        emailCheck = findViewById(R.id.emailCheck);
        contraseñaCheck = findViewById(R.id.contraseñaCheck);

        listeners();

    }

    private void listeners() {
        AuthController authController = new AuthController();

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(email.getText().toString().endsWith(".com")){
                    emailCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
                }else {
                    emailCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(contraseña.getText().toString().length() >= 8){
                        contraseñaCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
                    }else{
                        contraseñaCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        iniciarSesion.setOnClickListener(view -> {
            String emailString = email.getText().toString();
            String contraseñaString = contraseña.getText().toString();
            if(emailString.isEmpty() || contraseñaString.isEmpty()){
                emailCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
                contraseñaCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
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
                            emailCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
                            contraseñaCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
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