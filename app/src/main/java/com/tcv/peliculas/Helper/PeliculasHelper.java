package com.tcv.peliculas.Helper;

import android.content.Context;
import android.widget.Toast;

import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.ListaPeliculasActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeliculasHelper {

    public static ArrayList<Pelicula> getAll(final Context context){
        final ArrayList<Pelicula> result = new ArrayList<Pelicula>();
        ApiClient.getInstance().getClient(context).getPeliculas().enqueue(new Callback<List<Pelicula>>() {
            @Override
            public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                result.clear();
                List<Pelicula> peliculasResponse = response.body();
                result.addAll(peliculasResponse);
            }

            @Override
            public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                Toast.makeText(context, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
            }
        });
        return  result;
    }
}
