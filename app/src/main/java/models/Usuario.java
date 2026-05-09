package models;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName(value = "id_usuario", alternate = {"id"})
    private int id_usuario;
    private int id_rol;
    private String nombre;
    private String email;
    private String telefono;

    public int getId_usuario() {
        return id_usuario;
    }

    public int getId_rol() {
        return id_rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }
}
