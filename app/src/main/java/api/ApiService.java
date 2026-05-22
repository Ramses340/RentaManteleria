package api;

import java.util.List;

import models.LoginRequest;
import models.RegisterRequest;
import models.Producto;
import models.Usuario;
import models.CarritoRequest;
import models.CartItem;
import models.Notificacion;
import models.Pedido;
import models.ReservacionRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("api/productos")
    Call<List<Producto>> getProductos();

    @GET("api/productos/{categoria}/{id}")
    Call<Producto> getProductoDetalle(@Path("categoria") String categoria, @Path("id") int id);

    @POST("api/auth/login")
    Call<Usuario> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<Usuario> register(@Body RegisterRequest request);

    @POST("api/carrito")
    Call<Void> agregarAlCarrito(@Body CarritoRequest request);

    @GET("api/carrito/{id_usuario}")
    Call<List<CartItem>> getCarrito(@Path("id_usuario") int idUsuario);

    @DELETE("api/carrito/{id_carrito}")
    Call<Void> eliminarDelCarrito(@Path("id_carrito") int idCarrito);

    @POST("api/pedidos/{id_usuario}")
    Call<Void> finalizarPedido(@Path("id_usuario") int idUsuario);

    @POST("api/reservaciones/{id_usuario}")
    Call<Void> crearReservacion(@Path("id_usuario") int idUsuario, @Body models.ReservacionRequest request);

    @GET("api/pedidos/usuario/{id_usuario}")
    Call<List<Pedido>> getMisPedidos(@Path("id_usuario") int idUsuario);

    // Rutas para el Administrador/Operador
    @GET("api/pedidos")
    Call<List<Pedido>> getTodosLosPedidos();

    @PATCH("api/pedidos/{id_pedido}/estado")
    Call<Void> actualizarEstadoPedido(@Path("id_pedido") int idPedido, @Query("estado") String nuevoEstado);

    // Perfil de Usuario
    @GET("api/usuarios/{id_usuario}")
    Call<Usuario> getPerfil(@Path("id_usuario") int idUsuario);

    @PATCH("api/usuarios/{id_usuario}")
    Call<Void> actualizarPerfil(@Path("id_usuario") int idUsuario, @Body Usuario usuario);

    // Notificaciones
    @GET("api/notificaciones/usuario/{id_usuario}")
    Call<List<Notificacion>> getNotificaciones(@Path("id_usuario") int idUsuario);

    @PATCH("api/notificaciones/{id_notificacion}/leida")
    Call<Void> marcarLeida(@Path("id_notificacion") int idNotificacion);
}
