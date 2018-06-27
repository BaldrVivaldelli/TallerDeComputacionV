package com.tcv.peliculas.controller.Favoritos;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import com.tcv.peliculas.DAO.FavoritoDAO;
import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.PeliculaDetailsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritosListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FavoritoDAO> favoritos;
    private Context context;

    public FavoritosListAdapter(List<FavoritoDAO> favoritos, Context context) {
        this.favoritos = favoritos;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavoritoViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.favorito_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ((FavoritoViewHolder) viewHolder).bind(favoritos.get(position));
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    private class FavoritoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tituloTv;
        private final ImageView imagenTv;

        public FavoritoViewHolder(View itemView) {
            super(itemView);
            tituloTv = itemView.findViewById(R.id.titulo);
            imagenTv = itemView.findViewById(R.id.imagen);
        }

        public void bind(final FavoritoDAO favorito) {
            tituloTv.setText(favorito.getTituloPelicula());
            Glide.with(context).load(favorito.getImagenPelicula()).into(imagenTv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiClient.getInstance().getClient(context).getPeliculas().enqueue(new Callback<List<Pelicula>> () {
                        @Override
                        public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                            List<Pelicula>  peliculas = response.body();
                            Pelicula pelicula = null;
                            for (Pelicula p : peliculas) {
                                if(p.getId() == favorito.getIdPelicula()){
                                    pelicula = p;
                                    break;
                                }
                            }
                            Intent intent = new Intent(context, PeliculaDetailsActivity.class);
                            intent.putExtra("pelicula", new Gson().toJson(pelicula));
                            context.startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<List<Pelicula>>  call, Throwable throwable) {
                            Toast.makeText(context, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
