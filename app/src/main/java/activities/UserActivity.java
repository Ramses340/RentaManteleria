package activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ImageButton btnVerCarrito;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);

        tvBienvenida = findViewById(R.id.tvBienvenidaUsuario);
        rvProductos = findViewById(R.id.rvProductos);
        btnVerCarrito = findViewById(R.id.btnVerCarrito);

        String nombre = getIntent().getStringExtra("nombre");
        tvBienvenida.setText("Hola, " + nombre);

        setupRecyclerView();
        cargarProductos();

        btnVerCarrito.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, CartActivity.class);
            startActivity(intent);
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
        Call<List<Producto>> call = apiService.getProductos();

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaProductos.clear();
                    listaProductos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(UserActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(UserActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarAlCarrito(Producto producto) {
        if (idUsuario == 0) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        // Cantidad por defecto 1 desde la lista general
        CarritoRequest request = new CarritoRequest(idUsuario, producto.getCategoria(), producto.getId(), 1);

        Call<Void> call = apiService.agregarAlCarrito(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserActivity.this, producto.getNombre() + " agregado al carrito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Error al agregar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
