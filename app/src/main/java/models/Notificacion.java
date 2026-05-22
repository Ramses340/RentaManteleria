package models;

public class Notificacion {
    private int id_notificacion;
    private int id_usuario;
    private String titulo;
    private String mensaje;
    private String tipo;
    private boolean leida;
    private String created_at;

    public int getId_notificacion() { return id_notificacion; }
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public String getTipo() { return tipo; }
    public boolean isLeida() { return leida; }
}
