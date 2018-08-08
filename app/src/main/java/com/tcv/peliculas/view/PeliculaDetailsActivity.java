package com.tcv.peliculas.view;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.tcv.peliculas.Helper.PeliculasHelper;
import com.tcv.peliculas.R;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.Helper.DatabaseHelper;

import java.net.URL;

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

        try {
            pelicula = new Gson().fromJson(args.getString("pelicula"), Pelicula.class);
            favorite = dbHelper.getIfIsFavorito(pelicula.getId());
        }catch (Exception e){
            String value = args.getString("pelicula");
            pelicula = PeliculasHelper.getPeliculaById(this,value);
        }




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


        try {
            Glide.with(this).load(pelicula.getLarge_img_url())
                    .placeholder(R.drawable.ic_search)
                    .error(R.drawable.ic_search)
                    .into((ImageView) findViewById(R.id.imagen));

        }catch (Exception e){
            Glide.with(this).load(pelicula.getImagen()).into((ImageView) findViewById(R.id.imagen));
        }

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeliculaDetailsActivity.this, VerPeliculaActivity.class);
                intent.putExtra("link", pelicula.getVideoUrl());
                PeliculaDetailsActivity.this.startActivity(intent);
            }
        });
        if(pelicula.getLarge_img_url() != null) {
            URL url;
            Bitmap bmp;
            try {
                url = new URL(pelicula.getLarge_img_url());
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                imagen.setImageBitmap(bmp);
            }catch (Exception e){
               int i = 0;
            }

        }
        try {
            if(pelicula.getImagen() != 0) {
                imagen.setImageResource(pelicula.getImagen());
            }
        }catch (Exception e){
            int i =  1;
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
                    dbHelper.insertarFavorito(pelicula, null);
                    item.setIcon(R.drawable.favcheck);
                    favorite = true;
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
