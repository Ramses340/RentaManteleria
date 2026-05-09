package activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import adapters.CartAdapter;
import api.ApiClient;
import api.ApiService;
import itson.edu.rentamanteleria.R;
import models.CartItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView tvTotal;
    private Button btnFinalizar;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);

        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotalCart);
        btnFinalizar = findViewById(R.id.btnFinalizarRenta);

        setupRecyclerView();
        cargarCarrito();

        btnFinalizar.setOnClickListener(v -> finalizarRenta());
    }

    private void setupRecyclerView() {
        adapter = new CartAdapter(cartItems, idCarrito -> eliminarItem(idCarrito));
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(adapter);
    }

    private void cargarCarrito() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCarrito(idUsuario).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItems.clear();
                    cartItems.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    calcularTotal();
                }
            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error al cargar carrito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calcularTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrecio_dia() * item.getCantidad();
        }
        tvTotal.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    private void eliminarItem(int idCarrito) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.eliminarDelCarrito(idCarrito).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cargarCarrito();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finalizarRenta() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.finalizarPedido(idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "¡Renta finalizada con éxito!", Toast.LENGTH_LONG).show();
                    finish(); // Cierra el carrito y vuelve a la pantalla de productos
                } else {
                    Toast.makeText(CartActivity.this, "Error al procesar el pedido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
