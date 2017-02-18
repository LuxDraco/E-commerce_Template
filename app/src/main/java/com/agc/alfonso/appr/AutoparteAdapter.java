package com.agc.alfonso.appr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link android.support.v7.widget.RecyclerView.Adapter} para la lista de elementos
 */

public class AutoparteAdapter extends RecyclerView.Adapter<AutoparteAdapter.AutoparteViewHolder> implements ItemClickListener
{
    private final Context context;
    private ArrayList<Autoparte> items;


    public AutoparteAdapter(Context context, ArrayList<Autoparte> items) {
        this.context = context;
        this.items = new ArrayList<>(items);
        //this.items.clear();
        //this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AutoparteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.articulo_individual, viewGroup, false);
        return new AutoparteViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(AutoparteViewHolder viewHolder, int i) {
        // Item procesado actualmente
        Autoparte currentItem = items.get(i);

        viewHolder.name.setText(currentItem.getNombre());
        viewHolder.rating.setRating((float)currentItem.getRating());
        viewHolder.marca.setText(currentItem.getMarca());
        viewHolder.modelo.setText(currentItem.getModelo());
        viewHolder.year.setText(String.valueOf(currentItem.getYear()));
        viewHolder.precio.setText("$" + String.valueOf(currentItem.getPrecio()));

        if(currentItem.getIdImage() == 0){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReferenceFromUrl("gs://la-refa-e937e.appspot.com");
            StorageReference mountainsRef = storageRef.child(currentItem.getPath());

            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(mountainsRef)
                    .into(viewHolder.image);
        }
        else {
            // Cargar imagen
            Glide.with(context)
                    .load(currentItem.getIdImage())
                    .into(viewHolder.image);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        // Imagen a compartir entre transiciones
        View sharedImage = view.findViewById(R.id.image);
        Detalle_producto.launch((Activity) context, position, sharedImage);
    }

    /**
     * View holder para reciclar elementos
     */
    public static class AutoparteViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Views para un curso
        public final ImageView image;
        public final TextView name;
        public final TextView marca;
        public final TextView modelo;
        public final TextView precio;
        public final RatingBar rating;
        public final TextView year;

        // Interfaz de comunicación
        public ItemClickListener listener;

        public AutoparteViewHolder(View v, ItemClickListener listener) {
            super(v);
            name = (TextView) v.findViewById(R.id.name_lista);
            marca = (TextView) v.findViewById(R.id.marca_lista);
            modelo = (TextView) v.findViewById(R.id.modelo_lista);
            precio = (TextView) v.findViewById(R.id.precio_lista);
            rating = (RatingBar) v.findViewById(R.id.rating_lista);
            image = (ImageView) v.findViewById(R.id.image_lista);
            year = (TextView) v.findViewById(R.id.year_lista);
            v.setOnClickListener(this);

            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}

interface ItemClickListener {
    void onItemClick(View view, int position);
}