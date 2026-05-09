package activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import api.ApiClient;
import api.ApiService;
import itson.edu.rentamanteleria.R;
import models.CarritoRequest;
import models.Producto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProducto;
    private TextView tvNombre, tvPrecio, tvExtra, tvDisponible, tvCantidad;
    private Button btnRestar, btnSumar, btnAgregar;
    
    private Producto producto;
    private int cantidad = 1;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        idUsuario = getSharedPreferences("RentaPrefs", MODE_PRIVATE).getInt("id_usuario", 0);
        producto = (Producto) getIntent().getSerializableExtra("producto");

        if (producto == null) {
            finish();
            return;
        }

        ivProducto = findViewById(R.id.ivDetailProducto);
        tvNombre = findViewById(R.id.tvDetailNombre);
        tvPrecio = findViewById(R.id.tvDetailPrecio);
        tvExtra = findViewById(R.id.tvDetailExtra);
        tvDisponible = findViewById(R.id.tvDetailDisponible);
        tvCantidad = findViewById(R.id.tvCantidadSeleccionada);
        btnRestar = findViewById(R.id.btnRestar);
        btnSumar = findViewById(R.id.btnSumar);
        btnAgregar = findViewById(R.id.btnDetailAgregar);

        // Primero mostramos lo que tenemos de la lista general
        mostrarDatos();
        
        // Inmediatamente pedimos los detalles completos al servidor
        cargarDetallesCompletos();

        btnRestar.setOnClickListener(v -> {
            if (cantidad > 1) {
                cantidad--;
                actualizarVistaCantidad();
            }
        });

        btnSumar.setOnClickListener(v -> {
            if (producto != null && cantidad < producto.getCantidad_disponible()) {
                cantidad++;
                actualizarVistaCantidad();
            } else {
                Toast.makeText(this, "Cantidad máxima alcanzada", Toast.LENGTH_SHORT).show();
            }
        });

        btnAgregar.setOnClickListener(v -> agregarAlCarrito());
    }

    private void cargarDetallesCompletos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProductoDetalle(producto.getCategoria(), producto.getId()).enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizamos el objeto producto con toda la info de la tabla real
                    producto = response.body();
                    mostrarDatos();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Log.e("API_ERROR", "Error cargando detalles: " + t.getMessage());
            }
        });
    }

    private void mostrarDatos() {
        tvNombre.setText(producto.getNombre());
        tvPrecio.setText(String.format(Locale.getDefault(), "$%.2f / día", producto.getPrecio_dia()));
        tvDisponible.setText("Disponibles: " + producto.getCantidad_disponible());

        StringBuilder detalles = new StringBuilder();
        if ("cristaleria".equals(producto.getCategoria())) {
            detalles.append("Tipo: ").append(producto.getTipo() != null ? producto.getTipo() : "N/A").append("\n");
            detalles.append("Material: ").append(producto.getMaterial() != null ? producto.getMaterial() : "N/A");
        } else {
            detalles.append("Medida: ").append(producto.getMedida() != null ? producto.getMedida() : "N/A").append("\n");
            detalles.append("Color: ").append(producto.getColor() != null ? producto.getColor() : "N/A").append("\n");
            detalles.append("Material: ").append(producto.getMaterial() != null ? producto.getMaterial() : "N/A").append("\n");
            detalles.append("Tela: ").append(producto.getTipo_tela() != null ? producto.getTipo_tela() : "N/A");
        }
        tvExtra.setText(detalles.toString());
    }

    private void actualizarVistaCantidad() {
        tvCantidad.setText(String.valueOf(cantidad));
    }

    private void agregarAlCarrito() {
        if (idUsuario == 0) {
            Toast.makeText(this, "Error: Inicia sesión de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        CarritoRequest request = new CarritoRequest(idUsuario, producto.getCategoria(), producto.getId(), cantidad);

        apiService.agregarAlCarrito(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Agregado al carrito", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
