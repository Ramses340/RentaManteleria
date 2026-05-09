package models;

public class CarritoRequest {
    private int id_usuario;
    private String categoria;
    private int id_producto;
    private int cantidad;

    public CarritoRequest(int id_usuario, String categoria, int id_producto, int cantidad) {
        this.id_usuario = id_usuario;
        this.categoria = categoria;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
    }
}
