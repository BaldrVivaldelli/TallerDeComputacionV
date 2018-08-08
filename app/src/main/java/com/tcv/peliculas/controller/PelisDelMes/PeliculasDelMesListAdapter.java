package com.tcv.peliculas.controller.PelisDelMes;

import android.content.Context;
import android.graphics.Color;
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
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.model.PelisDelMes;

import java.util.List;

public class PeliculasDelMesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<PelisDelMes> peliculas;
    private Context context;

    public PeliculasDelMesListAdapter(List<PelisDelMes> peliculas, Context context) {
        this.peliculas = peliculas;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PeliculasDelMesHolder(LayoutInflater.from(context)
                .inflate(R.layout.categoria_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((PeliculasDelMesHolder) viewHolder).bind(peliculas.get(position));
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    private class PeliculasDelMesHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTv;
        private final RecyclerView peliculasRv;

        public PeliculasDelMesHolder(View itemView) {
            super(itemView);
            tituloTv = itemView.findViewById(R.id.titulo);
            peliculasRv = itemView.findViewById(R.id.peliculas_rv);
        }

        public void bind(final PelisDelMes pelicula) {
            PeliculasListAdapter peliculasListAdapter = new PeliculasListAdapter((List)pelicula.getPeliculas(), context);
            PeliculasViewModel peliculasViewModel = new PeliculasViewModel(context);

            peliculasRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            peliculasRv.setAdapter(peliculasListAdapter);
        }
    }
}
