package models;

public class ReservacionRequest {
    private int id_usuario;
    private String nombre_evento;
    private String fecha_entrega;
    private String fecha_devolucion;
    private String fecha_evento;

    public ReservacionRequest(int id_usuario, String nombre_evento, String fecha_entrega, String fecha_devolucion) {
        this.id_usuario = id_usuario;
        this.nombre_evento = nombre_evento;
        this.fecha_entrega = fecha_entrega;
        this.fecha_devolucion = fecha_devolucion;
        this.fecha_evento = fecha_entrega; // Usamos la misma de entrega por defecto
    }

    public int getId_usuario() { return id_usuario; }
    public String getNombre_evento() { return nombre_evento; }
    public String getFecha_entrega() { return fecha_entrega; }
    public String getFecha_devolucion() { return fecha_devolucion; }
    public String getFecha_evento() { return fecha_evento; }
}
