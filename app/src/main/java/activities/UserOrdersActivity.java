package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

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

public class UserOrdersActivity extends AppCompatActivity {

    private RecyclerView rvOrders;
    private PedidoAdapter adapter;
    private List<Pedido> todasLasRents = new ArrayList<>();
    private List<Pedido> listaFiltrada = new ArrayList<>();
    private TabLayout tabLayout;
    private BottomNavigationView bottomNav;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);

        rvOrders = findViewById(R.id.rvUserOrders);
        tabLayout = findViewById(R.id.tabLayoutOrders);
        bottomNav = findViewById(R.id.bottom_navigation);

        setupRecyclerView();
        cargarMisPedidos();
        setupBottomNav();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filtrarPedidos(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UserActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_orders);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_products) {
                startActivity(new Intent(this, UserActivity.class));
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
            return id == R.id.nav_orders;
        });
    }

    private void setupRecyclerView() {
        adapter = new PedidoAdapter(listaFiltrada, false, new PedidoAdapter.OnPedidoClickListener() {
            @Override
            public void onEntregadoClick(Pedido pedido) {}

            @Override
            public void onItemClick(Pedido pedido) {
                // Opcional: Ver detalles para el usuario también
            }
        });
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adapter);
    }

    private void cargarMisPedidos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getMisPedidos(idUsuario).enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    todasLasRents.clear();
                    todasLasRents.addAll(response.body());
                    filtrarPedidos(tabLayout.getSelectedTabPosition());
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(UserOrdersActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarPedidos(int tabIndex) {
        listaFiltrada.clear();
        for (Pedido p : todasLasRents) {
            String estado = (p.getEstado() != null) ? p.getEstado().toLowerCase() : "pendiente";
            
            boolean estaTerminado = estado.equals("entregado") || 
                                   estado.equals("entregada") || 
                                   estado.equals("devuelto") ||
                                   estado.equals("devuelta") ||
                                   estado.equals("cerrada") ||
                                   estado.equals("cancelado") ||
                                   estado.equals("cancelada");

            if (tabIndex == 0 && !estaTerminado) {
                listaFiltrada.add(p);
            } else if (tabIndex == 1 && estaTerminado) {
                listaFiltrada.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
