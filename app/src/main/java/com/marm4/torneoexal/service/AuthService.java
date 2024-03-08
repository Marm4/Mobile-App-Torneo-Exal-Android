package com.marm4.torneoexal.service;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.DevolverUsuarios;
import com.marm4.torneoexal.listener.InicioSesionExito;
import com.marm4.torneoexal.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthService {
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public AuthService() {
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("usuarios");
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
                        if (task.isSuccessful()){
                            Usuario usuario = new Usuario();
                            usuario.setEmail(email);
                            guardarUsuario(usuario);
                            listener.inicioSesionExito(true);

                        }

                        else
                            listener.inicioSesionExito(false);

                    }
                });
    }

    public void guardarUsuario(Usuario usuario){
        usuario.setId(mAuth.getCurrentUser().getUid());
        usersRef.child(mAuth.getCurrentUser().getUid()).setValue(usuario);
        Globals.getInstance().setUsuario(usuario);
    }

    public void getUsuarios(Boolean setUser, DevolverUsuarios listener){
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Usuario> usuarios;
                usuarios = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if(usuario.getId().equals(mAuth.getCurrentUser().getUid()))
                        Globals.getInstance().setUsuario(usuario);
                    else if(!setUser)
                        usuarios.add(usuario);


                }
                if(listener!=null)
                    listener.devolverUsuarios(usuarios);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.devolverUsuarios(null);
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
        mAuth.signInAnonymously();
    }

    public void logOut(){
        mAuth.signOut();
        Globals.getInstance().destroy();
        Torneo.getInstance().destroy();
    }

}
