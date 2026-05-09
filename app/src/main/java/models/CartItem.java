package models;

public class CartItem {
    private int id_carrito;
    private int id_producto;
    private String nombre;
    private double precio_dia;
    private String categoria;
    private int cantidad;

    public int getId_carrito() { return id_carrito; }
    public int getId_producto() { return id_producto; }
    public String getNombre() { return nombre; }
    public double getPrecio_dia() { return precio_dia; }
    public String getCategoria() { return categoria; }
    public int getCantidad() { return cantidad; }
}
