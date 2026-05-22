package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import adapters.ProductoAdapter;
import api.ApiClient;
import api.ApiService;
import itson.edu.rentamanteleria.R;
import models.CarritoRequest;
import models.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private RecyclerView rvProductos;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos = new ArrayList<>();
    private TextView tvBienvenida;
    private BottomNavigationView bottomNav;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);

        tvBienvenida = findViewById(R.id.tvBienvenidaUsuario);
        rvProductos = findViewById(R.id.rvProductos);
        bottomNav = findViewById(R.id.bottom_navigation);

        String nombre = getIntent().getStringExtra("nombre");
        if (nombre != null) {
            tvBienvenida.setText("Hola, " + nombre);
        }

        setupRecyclerView();
        cargarProductos();
        setupBottomNav();
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_products);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_orders) {
                startActivity(new Intent(this, UserOrdersActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_products;
        });
    }

    private void setupRecyclerView() {
        adapter = new ProductoAdapter(listaProductos, new ProductoAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(Producto producto) {
                agregarAlCarrito(producto);
            }

            @Override
            public void onProductClick(Producto producto) {
                Intent intent = new Intent(UserActivity.this, ProductDetailActivity.class);
                intent.putExtra("producto", producto);
                startActivity(intent);
            }
        });
        rvProductos.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductos.setAdapter(adapter);
    }

    private void cargarProductos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProductos.clear();
                    listaProductos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void agregarAlCarrito(Producto producto) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        CarritoRequest request = new CarritoRequest(idUsuario, producto.getCategoria(), producto.getId(), 1);

        apiService.agregarAlCarrito(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserActivity.this, "Agregado al carrito", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
