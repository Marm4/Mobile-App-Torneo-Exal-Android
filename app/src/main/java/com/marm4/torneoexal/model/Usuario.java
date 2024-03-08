package com.marm4.torneoexal.model;

public class Usuario {
    private String id;
    private String email;
    private Boolean admin;

    public Usuario(String id, String email, Boolean admin) {
        this.id = id;
        this.email = email;
        this.admin = admin;
    }

    public Usuario() {
        admin = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
