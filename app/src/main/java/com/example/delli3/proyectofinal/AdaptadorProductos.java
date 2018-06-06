package com.example.delli3.proyectofinal;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ProductosViewHolder> {

    private ArrayList<Productos> productos;
    private OnProductosClickListener clickListener;
    private Activity activity;

    public AdaptadorProductos(Activity activity, ArrayList<Productos> productos, OnProductosClickListener clickListener){
        this.activity = activity;
        this.productos=productos;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_productos,parent,false);
        return new ProductosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorProductos.ProductosViewHolder holder, int position) {
        final Productos p = productos.get(position);
        //holder.foto.setImageResource();
        //holder.foto.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        Picasso.get().load(p.foto).resize(50, 50).centerCrop().into(holder.foto);
        holder.titulo.setText(p.titulo);
        holder.precio.setText(p.precio);

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onProductoClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductosViewHolder extends RecyclerView.ViewHolder{
        private ImageView foto;
        private TextView titulo;
        private TextView precio;
        private View v;

        public ProductosViewHolder(View itemView){
            super(itemView);
            v = itemView;
            foto = v.findViewById(R.id.imgCard);
            titulo = v.findViewById(R.id.tvTituloProducto);
            precio = v.findViewById(R.id.tvPrecio);
        }

    }

    public interface OnProductosClickListener{
        void onProductoClick(Productos e);
    }
}
