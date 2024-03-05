package com.marm4.torneoexal.service;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marm4.torneoexal.listener.InicioSesionExito;

public class AuthService {
    private FirebaseAuth mAuth;

    public AuthService() {
        mAuth = FirebaseAuth.getInstance();

    }

    public void logIn(String email, String password, InicioSesionExito listener){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("AuthService", "Login success");
                            listener.inicioSesionExito(true);
                        }
                        else
                            listener.inicioSesionExito(false);
                    }
                });
    }

    public void signUp(String email, String password, InicioSesionExito listener){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            listener.inicioSesionExito(true);
                        else
                            listener.inicioSesionExito(false);

                    }
                });
    }


    public boolean isUserLoggedIn() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("AuthService", "is user logged? " + (currentUser!=null));
        if(currentUser!=null)
            Log.i("AuthService", "Id: " + currentUser.getUid());
        return currentUser != null;
    }

    public void anonymousUser(){
        //mAuth.signInAnonymously();
    }

}
