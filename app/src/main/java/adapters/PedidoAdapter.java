package adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import itson.edu.rentamanteleria.R;
import models.Pedido;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private List<Pedido> pedidos;
    private OnPedidoClickListener listener;
    private boolean isAdmin;

    public interface OnPedidoClickListener {
        void onEntregadoClick(Pedido pedido);
        void onItemClick(Pedido pedido);
    }

    public PedidoAdapter(List<Pedido> pedidos, boolean isAdmin, OnPedidoClickListener listener) {
        this.pedidos = pedidos;
        this.isAdmin = isAdmin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        
        holder.tvId.setText("Pedido #" + pedido.getId_pedido());
        holder.tvUsuario.setText("Cliente: " + (pedido.getNombre_usuario() != null ? pedido.getNombre_usuario() : "Usuario #" + pedido.getId_usuario()));
        holder.tvFecha.setText("Fecha: " + (pedido.getFecha() != null ? pedido.getFecha() : "N/A"));
        
        String estado = pedido.getEstado() != null ? pedido.getEstado() : "pendiente";
        holder.tvEstado.setText(estado.toUpperCase());
        
        boolean estaTerminado = estado.equalsIgnoreCase("entregado") || 
                               estado.equalsIgnoreCase("entregada") || 
                               estado.equalsIgnoreCase("devuelto") ||
                               estado.equalsIgnoreCase("devuelta") ||
                               estado.equalsIgnoreCase("cerrada") ||
                               estado.equalsIgnoreCase("cancelado") ||
                               estado.equalsIgnoreCase("cancelada");

        if (estaTerminado) {
            holder.tvEstado.setTextColor(Color.parseColor("#2E7D32"));
            holder.btnEntregado.setVisibility(View.GONE);
        } else {
            holder.tvEstado.setTextColor(Color.parseColor("#D32F2F"));
            holder.btnEntregado.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        }

        StringBuilder detallesStr = new StringBuilder();
        if (pedido.getDetalles() != null && !pedido.getDetalles().isEmpty()) {
            for (Pedido.PedidoDetalle det : pedido.getDetalles()) {
                detallesStr.append("• ").append(det.getCantidad())
                        .append(" ").append(det.getNombre_producto() != null ? det.getNombre_producto() : "Producto")
                        .append("\n");
            }
        } else {
            detallesStr.append("Sin detalles");
        }
        holder.tvDetalles.setText(detallesStr.toString());

        // Clic en el botón
        holder.btnEntregado.setOnClickListener(v -> listener.onEntregadoClick(pedido));
        
        // Clic en toda la tarjeta (solo para admin si lo desea, o para ambos)
        holder.itemView.setOnClickListener(v -> listener.onItemClick(pedido));
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvUsuario, tvFecha, tvEstado, tvDetalles;
        Button btnEntregado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvPedidoId);
            tvUsuario = itemView.findViewById(R.id.tvPedidoUsuario);
            tvFecha = itemView.findViewById(R.id.tvPedidoFecha);
            tvEstado = itemView.findViewById(R.id.tvPedidoEstado);
            tvDetalles = itemView.findViewById(R.id.tvPedidoDetalles);
            btnEntregado = itemView.findViewById(R.id.btnEntregado);
        }
    }
}
