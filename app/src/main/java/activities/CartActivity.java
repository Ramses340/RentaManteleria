package activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import adapters.CartAdapter;
import api.ApiClient;
import api.ApiService;
import itson.edu.rentamanteleria.R;
import models.CartItem;
import models.ReservacionRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView rvCart;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView tvTotal;
    private Button btnFinalizar, btnFechaEntrega, btnFechaDevolucion;
    private EditText etNombreEvento;
    private BottomNavigationView bottomNav;
    private int idUsuario;
    private String fechaEntrega = "", fechaDevolucion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);

        rvCart = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotalCart);
        btnFinalizar = findViewById(R.id.btnFinalizarRenta);
        btnFechaEntrega = findViewById(R.id.btnFechaEntrega);
        btnFechaDevolucion = findViewById(R.id.btnFechaDevolucion);
        etNombreEvento = findViewById(R.id.etNombreEvento);
        bottomNav = findViewById(R.id.bottom_navigation);

        setupRecyclerView();
        cargarCarrito();
        setupBottomNav();

        btnFechaEntrega.setOnClickListener(v -> mostrarCalendario(true));
        btnFechaDevolucion.setOnClickListener(v -> mostrarCalendario(false));
        btnFinalizar.setOnClickListener(v -> realizarRentaReal());
    }

    private void mostrarCalendario(boolean esEntrega) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
            if (esEntrega) {
                fechaEntrega = fecha;
                btnFechaEntrega.setText("Entrega: " + fecha);
            } else {
                fechaDevolucion = fecha;
                btnFechaDevolucion.setText("Devolución: " + fecha);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_cart);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_products) {
                startActivity(new Intent(this, UserActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(this, UserOrdersActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return id == R.id.nav_cart;
        });
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

    private void realizarRentaReal() {
        String nombreEvento = etNombreEvento.getText().toString().trim();

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nombreEvento.isEmpty() || fechaEntrega.isEmpty() || fechaDevolucion.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los detalles", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Enviando reservación...");
        progress.setCancelable(false);
        progress.show();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ReservacionRequest request = new ReservacionRequest(idUsuario, nombreEvento, fechaEntrega, fechaDevolucion);

        apiService.crearReservacion(idUsuario, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progress.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "¡Renta confirmada con éxito!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CartActivity.this, UserOrdersActivity.class));
                    finish();
                } else {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : "Sin error detallado";
                        Log.e("API_ERROR", "Error: " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CartActivity.this, "Error al procesar la renta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progress.dismiss();
                Log.e("API_ERROR", "Fail: " + t.getMessage());
                Toast.makeText(CartActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
