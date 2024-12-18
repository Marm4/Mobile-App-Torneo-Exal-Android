package com.marm4.torneoexal.service;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marm4.torneoexal.global.Adapters;
import com.marm4.torneoexal.global.Globals;
import com.marm4.torneoexal.global.Torneo;
import com.marm4.torneoexal.listener.TorneoNotificacion;
import com.marm4.torneoexal.model.Equipo;
import com.marm4.torneoexal.model.Fixture;
import com.marm4.torneoexal.model.Jugador;
import com.marm4.torneoexal.model.Partido;

import java.util.List;
import java.util.UUID;

public class TorneoService {

    private FirebaseDatabase database ;
    private DatabaseReference equiposRef;
    private DatabaseReference fixtureRef;
    private FirebaseStorage storage;

    public TorneoService(){
        database = FirebaseDatabase.getInstance();
        equiposRef = database.getReference("equipos");
        fixtureRef = database.getReference("fixtures");
        storage = FirebaseStorage.getInstance();
    }

    public void guardarEquipo(Equipo equipo, TorneoNotificacion listener){
        if(equipo == null)
            return;

        String equipoId;
        if(equipo.getId() == null){
            equipoId = equiposRef.push().getKey();
            equipo.setId(equipoId);
            int i = 0;
            for(Jugador jugador : equipo.getJugadores()){
                jugador.setId(equipoId + String.valueOf(i));
                i++;
            }
        }
        else {
            equipoId = equipo.getId();
            for(Jugador jugador : equipo.getJugadores())
                if(jugador.getId() == null)
                    jugador.setId(generarIdUnico(equipo));


        }

        String nombreImagen = equipoId.toString() + ".jpg";
        StorageReference storageRef = storage.getReference().child("escudos/" + nombreImagen);

        if(equipo.getEscudo() != null && equipo.getUrlEscudo() == null){
            storageRef.putFile(equipo.getEscudo())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUrl = uri.toString();
                                    equipo.setUrlEscudo(downloadUrl);
                                    Uri escudoAux = equipo.getEscudo();
                                    equipo.setEscudo(null);
                                    equiposRef.child(equipoId).setValue(equipo, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            equipo.setEscudo(escudoAux);
                                            if (databaseError == null && listener !=null)
                                                listener.torneoNotificacion(true);
                                            else if (listener !=null)
                                                listener.torneoNotificacion(false);
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
        else{
            Uri escudoAux = equipo.getEscudo();
            equipo.setEscudo(null);
            equiposRef.child(equipoId).setValue(equipo, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    equipo.setEscudo(escudoAux);
                    if (databaseError == null && listener !=null)
                        listener.torneoNotificacion(true);
                    else if(listener !=null)
                        listener.torneoNotificacion(false);
                }
            });

        }

    }

    public String generarIdUnico(Equipo equipo) {
        String idUnico;
        boolean idUnicoExiste;

        do {
            UUID uuid = UUID.randomUUID();
            idUnico = uuid.toString();
            idUnicoExiste = idUnicoYaExiste(idUnico, equipo);
        } while (idUnicoExiste);

        return idUnico;
    }

    private boolean idUnicoYaExiste(String id, Equipo equipo) {
        // Verificar si el ID ya existe en la lista de jugadores
        for (Jugador jugador : equipo.getJugadores()) {
            if (jugador.getId() != null && jugador.getId().equals(id)) {
                return true; // El ID ya existe
            }
        }
        return false;
    }

    public void devolverEquipos(TorneoNotificacion listener) {
        equiposRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Torneo.getInstance().limpiarEquipos();

                    for (DataSnapshot equipoSnapshot : dataSnapshot.getChildren()) {
                        Equipo equipo = equipoSnapshot.getValue(Equipo.class);
                        Torneo.getInstance().addEquipo(equipo);
                    }

                    listener.torneoNotificacion(true);
                } else {
                    listener.torneoNotificacion(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.torneoNotificacion(false);
            }
        });
    }

    public void setProximoFixture(String fixtureId){
        database.getReference("proxima-fecha").setValue(fixtureId);
    }

    public void getProximoFixture(){
        database.getReference("proxima-fecha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String fixtureId = dataSnapshot.getValue(String.class);
                    Globals.getInstance().setProximaFecha(fixtureId);
                    Adapters.getInstance().setListPartidos(findPartidos(fixtureId));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private List<Partido> findPartidos(String idFixture){
        for(Fixture f : Torneo.getInstance().getFixtures()){
            if(f.getId().equals(idFixture))
                return f.getPartidos();
        }
        return null;
    }

    public void guardarFixture(Fixture fixture, TorneoNotificacion listener) {
        if(fixture == null)
            return;

        String fixtureId = "";
        if(fixture.getId() == null){
            fixtureId = fixtureRef.push().getKey();
            fixture.setId(fixtureId);

            for(Partido partido : fixture.getPartidos()){
                partido.setFixtureId(fixtureId);
            }
        }

        else
            fixtureId = fixture.getId();


        fixtureRef.child(fixtureId).setValue(fixture, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null && listener !=null)
                    listener.torneoNotificacion(true);
                else if(listener !=null)
                    listener.torneoNotificacion(false);
            }
        });
    }

    public void devolverFixture(TorneoNotificacion listener) {
        fixtureRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Torneo.getInstance().limpiarFixture();
                    for (DataSnapshot fixtureSnapshot : dataSnapshot.getChildren()) {

                        Fixture fixture = fixtureSnapshot.getValue(Fixture.class);
                        Torneo.getInstance().addFixtures(fixture);
                    }
                    listener.torneoNotificacion(true);
                }
                else
                    listener.torneoNotificacion(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.torneoNotificacion(false);
            }
        });

    }

}
