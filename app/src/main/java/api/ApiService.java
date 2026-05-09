package api;

import java.util.List;

import models.LoginRequest;
import models.RegisterRequest;
import models.Producto;
import models.Usuario;
import models.CarritoRequest;
import models.CartItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
}
