package com.marm4.torneoexal.view.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView contraseñaCheck;
    private ImageView emailCheck;
    private ImageView repetirContraseñaCheck;

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
        contraseñaCheck = findViewById(R.id.contraseñaCheck);
        emailCheck = findViewById(R.id.emailCheck);
        repetirContraseñaCheck = findViewById(R.id.repetirContraseñaCheck);

        listener();
    }

    private void listener() {
        registrarse.setOnClickListener(view -> {
            String emailString = email.getText().toString();
            String contraseñaString = contraseña.getText().toString();
            String repetirContraseñaString = repetirContraseña.getText().toString();

            if(!emailString.isEmpty() && !contraseñaString.isEmpty() && contraseñaString.equals(repetirContraseñaString)){
                registrar(emailString, contraseñaString);
            }
        });


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

        repetirContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(contraseña.getText().toString().length() >= 8 && contraseña.getText().toString().equals(repetirContraseña.getText().toString())){
                    repetirContraseñaCheck.setImageDrawable(getDrawable(R.drawable.ic_check));
                }else{
                    repetirContraseñaCheck.setImageDrawable(getDrawable(R.drawable.ic_cerrar));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                    finish();
                }
            }
        });
    }
}