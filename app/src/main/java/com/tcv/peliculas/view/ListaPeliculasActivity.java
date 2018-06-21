package com.tcv.peliculas.view;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Peliculas.PeliculasListAdapter;
import com.tcv.peliculas.controller.Peliculas.PeliculasViewModel;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaPeliculasActivity extends AppCompatActivity {

    private RecyclerView peliculasRv;
    private PeliculasListAdapter peliculasAdapter;
    private List<Pelicula> peliculas;
    private PeliculasViewModel peliculasViewModel = new PeliculasViewModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        peliculas = new ArrayList<>();
        peliculasRv = (RecyclerView) findViewById(R.id.peliculas_rv);
        peliculasAdapter = new PeliculasListAdapter(peliculas, this);
        peliculasRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        peliculasRv.setAdapter(peliculasAdapter);
        ApiClient.getClient(this).getPeliculas().enqueue(new Callback<List<Pelicula>>() {
            @Override
            public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                peliculas.clear();
                List<Pelicula> peliculasResponse = response.body();
                peliculas.addAll(peliculasResponse);
                peliculasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                Toast.makeText(ListaPeliculasActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
