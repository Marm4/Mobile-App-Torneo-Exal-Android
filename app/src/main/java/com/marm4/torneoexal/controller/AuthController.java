package com.marm4.torneoexal.controller;


import android.util.Log;
import com.marm4.torneoexal.listener.InicioSesionExito;
import com.marm4.torneoexal.service.AuthService;

public class AuthController {
    private AuthService authService;

    public AuthController() {
        authService = new AuthService();
    }


    public void logIn(String email, String password, InicioSesionExito listener) {
        Log.i("AuthController", " --- LOGIN USER ---");
        authService.logIn(email, password, listener);
    }

    public void signUp(String email, String password, InicioSesionExito listener) {
        Log.i("AuthController", " --- SIGN UP ---");
        authService.signUp(email, password, listener);
    }


    public boolean isUserLoggedIn() {

        Log.i("AuthController", " --- USER LOGGED ---");
        return authService.isUserLoggedIn();
    }

    public void anonymousUser(){
        authService.anonymousUser();
    }


}
