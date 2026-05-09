package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import itson.edu.rentamanteleria.R;
import models.CartItem;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int idCarrito);
    }

    public CartAdapter(List<CartItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvNombre.setText(item.getNombre());
        holder.tvDetalles.setText(item.getCategoria() + " | Cantidad: " + item.getCantidad());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrecio_dia() * item.getCantidad()));

        holder.btnEliminar.setOnClickListener(v -> listener.onDeleteClick(item.getId_carrito()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDetalles, tvPrecio;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCart);
            tvDetalles = itemView.findViewById(R.id.tvDetallesCart);
            tvPrecio = itemView.findViewById(R.id.tvPrecioCart);
            btnEliminar = itemView.findViewById(R.id.btnEliminarCart);
        }
    }
}
