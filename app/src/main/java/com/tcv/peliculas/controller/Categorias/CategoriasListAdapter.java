package com.tcv.peliculas.controller.Categorias;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tcv.peliculas.R;
import com.tcv.peliculas.controller.Peliculas.PeliculasListAdapter;
import com.tcv.peliculas.controller.Peliculas.PeliculasViewModel;
import com.tcv.peliculas.model.Categoria;

import java.util.List;

public class CategoriasListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Categoria> categorias;
    private Context context;

    public CategoriasListAdapter(List<Categoria> categorias, Context context) {
        this.categorias = categorias;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoriaViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.categoria_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((CategoriaViewHolder) viewHolder).bind(categorias.get(position));
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    private class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTv;
        private final RecyclerView peliculasRv;

        public CategoriaViewHolder(View itemView) {
            super(itemView);
            tituloTv = itemView.findViewById(R.id.titulo);
            peliculasRv = itemView.findViewById(R.id.peliculas_rv);
        }

        public void bind(final Categoria categoria) {
            PeliculasListAdapter peliculasListAdapter = new PeliculasListAdapter((List)categoria.getPeliculas(), context);
            PeliculasViewModel peliculasViewModel = new PeliculasViewModel(context);

            tituloTv.setText(categoria.getTitulo());
            peliculasRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            peliculasRv.setAdapter(peliculasListAdapter);
            //Glide.with(context).load(categoria.getImagen()).into(imagenIv);
        }
    }
}
