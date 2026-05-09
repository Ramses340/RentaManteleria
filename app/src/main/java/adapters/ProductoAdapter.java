package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import itson.edu.rentamanteleria.R;
import models.Producto;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private List<Producto> productos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAddClick(Producto producto);
        void onProductClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productos, OnItemClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvCategoria.setText(producto.getCategoria());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "$%.2f / día", producto.getPrecio_dia()));
        
        holder.ivProducto.setOnClickListener(v -> listener.onProductClick(producto));
        holder.btnAgregar.setOnClickListener(v -> listener.onAddClick(producto));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCategoria, tvPrecio;
        ImageView ivProducto;
        Button btnAgregar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
            ivProducto = itemView.findViewById(R.id.ivProducto);
            btnAgregar = itemView.findViewById(R.id.btnAgregarCarrito);
        }
    }
}
