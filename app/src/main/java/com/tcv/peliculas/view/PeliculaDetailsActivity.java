package com.tcv.peliculas.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.persistence.DatabaseHelper;

public class PeliculaDetailsActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    boolean favorite = false;
    Pelicula pelicula;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula_details);

        dbHelper = new DatabaseHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle args = getIntent().getExtras();
        pelicula = new Gson().fromJson(args.getString("pelicula"), Pelicula.class);

        favorite = dbHelper.getIfIsFavorito(pelicula.getId());

        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(pelicula.getTitulo());

        TextView genero = (TextView) findViewById(R.id.genero);
        genero.setText(pelicula.getGenero());

        TextView duracion = (TextView) findViewById(R.id.duracion);
        duracion.setText(String.valueOf(pelicula.getDuracion()));

        TextView lanzamiento = (TextView) findViewById(R.id.lanzamiento);
        lanzamiento.setText(pelicula.getLanzamiento());

        TextView puntuacion = (TextView) findViewById(R.id.puntuacion);
        puntuacion.setText( String.valueOf(pelicula.getPuntuacion()));

        TextView artistas = (TextView) findViewById(R.id.artistas_principales);
        artistas.setText(pelicula.getArtistas().toString());

        ImageView imagen = (ImageView) findViewById(R.id.imagen);
        Glide.with(this).load(pelicula.getImagen()).into((ImageView) findViewById(R.id.imagen));
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeliculaDetailsActivity.this, VerPeliculaActivity.class);
                intent.putExtra("link", pelicula.getVideoUrl());
                PeliculaDetailsActivity.this.startActivity(intent);
            }
        });
        if(pelicula.getImagen() != 0) {
            imagen.setImageResource(pelicula.getImagen());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

        MenuItem mFav = menu.findItem(R.id.action_fav);

        if(favorite) {
            mFav.setIcon(R.drawable.favcheck);
        }
        else{
            mFav.setIcon(R.drawable.fav);
        }

        mFav.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(favorite) {
                    dbHelper.deleteFavorito(pelicula.getId(), null);
                    item.setIcon(R.drawable.fav);
                    favorite = false;
                }
                else{
                    dbHelper.insertarFavorito(pelicula.getId(), null);
                    item.setIcon(R.drawable.favcheck);
                    favorite = true;
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
