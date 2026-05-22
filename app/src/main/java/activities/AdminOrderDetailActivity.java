package activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import itson.edu.rentamanteleria.R;
import models.Pedido;

public class AdminOrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        Pedido pedido = (Pedido) getIntent().getSerializableExtra("pedido");

        if (pedido == null) {
            finish();
            return;
        }

        TextView tvId = findViewById(R.id.tvDetailPedidoId);
        TextView tvNombre = findViewById(R.id.tvDetailNombre);
        TextView tvTelefono = findViewById(R.id.tvDetailTelefono);
        TextView tvDireccion = findViewById(R.id.tvDetailDireccion);
        TextView tvNotas = findViewById(R.id.tvDetailNotas);
        TextView tvPreferencias = findViewById(R.id.tvDetailPreferencias);
        TextView tvItems = findViewById(R.id.tvDetailItems);
        Button btnVolver = findViewById(R.id.btnVolverAdmin);

        tvId.setText("Detalle Pedido #" + pedido.getId_pedido());
        tvNombre.setText("Nombre: " + (pedido.getNombre_usuario() != null ? pedido.getNombre_usuario() : "N/A"));
        tvTelefono.setText("Teléfono: " + (pedido.getTelefono() != null ? pedido.getTelefono() : "N/A"));
        tvDireccion.setText("Dirección: " + (pedido.getDireccion() != null ? pedido.getDireccion() : "N/A"));
        tvNotas.setText("Notas: " + (pedido.getNotas() != null ? pedido.getNotas() : "N/A"));
        tvPreferencias.setText("Preferencias: " + (pedido.getPreferencias() != null ? pedido.getPreferencias() : "N/A"));

        StringBuilder sb = new StringBuilder();
        if (pedido.getDetalles() != null) {
            for (Pedido.PedidoDetalle det : pedido.getDetalles()) {
                sb.append("• ").append(det.getCantidad()).append("x ")
                  .append(det.getNombre_producto()).append(" (").append(det.getCategoria()).append(")\n");
            }
        }
        tvItems.setText(sb.toString());

        btnVolver.setOnClickListener(v -> finish());
    }
}
