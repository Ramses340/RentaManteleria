package models;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName(value = "id_usuario", alternate = {"id"})
    private int id_usuario;
    private int id_rol;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String notas;
    private String preferencias;
    private String imagen;

    public int getId_usuario() { return id_usuario; }
    public int getId_rol() { return id_rol; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getDireccion() { return direccion; }
    public String getNotas() { return notas; }
    public String getPreferencias() { return preferencias; }
    public String getImagen() { return imagen; }

    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setNotas(String notas) { this.notas = notas; }
    public void setPreferencias(String preferencias) { this.preferencias = preferencias; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
