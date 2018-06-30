package com.tcv.peliculas.Helper;

import android.content.Context;
import android.widget.Toast;

import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.model.Pelicula;

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
    public static Pelicula getPeliculaById(final Context context, String nombre){
        Pelicula result = null;
        ArrayList<Pelicula> peliculas = (ArrayList<Pelicula>) getAllSync(context);
        for (Pelicula p: peliculas){
            String nombrePelicula = p.getTitulo();
            if(nombrePelicula.equals(p.getTitulo())){
                result = new Pelicula(p.getTitulo(),p.getGenero(),p.getImagen());
                result.setArtistas(p.getArtistas());
                result.setLarge_img_url(p.getLarge_img_url());
                result.setSmall_img_url(p.getSmall_img_url());
            };
        }
        return result;
    }
    private static synchronized  List<Pelicula> getAllSync(final Context context){
        ArrayList<Pelicula> peliculas = new ArrayList<Pelicula>();
        Call<List<Pelicula>> request;
        List<Pelicula> response;
        request = ApiClient.getInstance().getClient(context).getPeliculas();
        try {
            peliculas.clear();
            response= request.execute().body();
            List<Pelicula> peliculasResponse = response;
            peliculas.addAll(peliculasResponse);
        }catch (Exception e){
            int i = 0 ;
        }
        return peliculas;
    }
}
