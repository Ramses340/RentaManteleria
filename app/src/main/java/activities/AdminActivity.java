package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapters.PedidoAdapter;
import api.ApiClient;
import api.ApiService;
import itson.edu.rentamanteleria.R;
import models.Pedido;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvPedidos;
    private PedidoAdapter adapter;
    private List<Pedido> listaPedidos = new ArrayList<>();
    private TextView tvBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tvBienvenida = findViewById(R.id.tvBienvenidaAdmin);
        rvPedidos = findViewById(R.id.rvPedidos);
        ImageButton btnLogout = findViewById(R.id.btnLogoutAdmin);

        String nombre = getIntent().getStringExtra("nombre");
        if (nombre != null) {
            tvBienvenida.setText("Panel de " + nombre);
        }

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("RentaPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        setupRecyclerView();
        cargarPedidos();
    }

    private void setupRecyclerView() {
        adapter = new PedidoAdapter(listaPedidos, true, new PedidoAdapter.OnPedidoClickListener() {
            @Override
            public void onEntregadoClick(Pedido pedido) {
                cambiarEstado(pedido);
            }

            @Override
            public void onItemClick(Pedido pedido) {
                Intent intent = new Intent(AdminActivity.this, AdminOrderDetailActivity.class);
                intent.putExtra("pedido", pedido);
                startActivity(intent);
            }
        });
        rvPedidos.setLayoutManager(new LinearLayoutManager(this));
        rvPedidos.setAdapter(adapter);
    }

    private void cargarPedidos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getTodosLosPedidos().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPedidos.clear();
                    listaPedidos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cambiarEstado(Pedido pedido) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        
        apiService.actualizarEstadoPedido(pedido.getId_pedido(), "entregado").enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminActivity.this, "Pedido marcado como entregado", Toast.LENGTH_SHORT).show();
                    pedido.setEstado("entregado");
                    adapter.notifyDataSetChanged();
                } else {
                    String errorMsg = "Error " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMsg += ": " + response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AdminActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
