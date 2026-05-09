package models;

import java.io.Serializable;

public class Producto implements Serializable {
    private int id;
    private String nombre;
    private double precio_dia;
    private String categoria;
    private String imagen;
    
    // Campos extra para detalles
    private String tipo;
    private String material;
    private int cantidad_disponible;
    private String medida;
    private String color;
    private String tipo_tela;

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio_dia() { return precio_dia; }
    public String getCategoria() { return categoria; }
    public String getImagen() { return imagen; }
    
    public String getTipo() { return tipo; }
    public String getMaterial() { return material; }
    public int getCantidad_disponible() { return cantidad_disponible; }
    public String getMedida() { return medida; }
    public String getColor() { return color; }
    public String getTipo_tela() { return tipo_tela; }
}
