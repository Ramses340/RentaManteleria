package models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class Pedido implements Serializable {
    private int id_pedido;
    private int id_usuario;
    private String nombre_usuario;
    private String fecha;
    private String estado;
    
    // Datos del cliente
    private String direccion;
    private String telefono;
    private String notas;
    private String preferencias;

    @SerializedName(value = "detalles", alternate = {"productos", "items"})
    private List<PedidoDetalle> detalles;

    public int getId_pedido() { return id_pedido; }
    public int getId_usuario() { return id_usuario; }
    public String getNombre_usuario() { return nombre_usuario; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getNotas() { return notas; }
    public String getPreferencias() { return preferencias; }
    public List<PedidoDetalle> getDetalles() { return detalles; }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public static class PedidoDetalle implements Serializable {
        @SerializedName(value = "nombre_producto", alternate = {"nombre", "producto"})
        private String nombre_producto;
        private int cantidad;
        private String categoria;

        public String getNombre_producto() { return nombre_producto; }
        public int getCantidad() { return cantidad; }
        public String getCategoria() { return categoria; }
    }
}
