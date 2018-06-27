package com.tcv.peliculas.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.tcv.peliculas.DAO.FavoritoDAO;
import com.tcv.peliculas.Helper.DatabaseHelper;
import com.tcv.peliculas.R;
import com.tcv.peliculas.controller.Favoritos.FavoritosListAdapter;
import com.tcv.peliculas.controller.Favoritos.FavoritosViewModel;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {
    private RecyclerView favoritosRv;
    private FavoritosListAdapter favoritosListAdapter;
    private List<FavoritoDAO> favoritos;
    private FavoritosViewModel favoritosViewModel = new FavoritosViewModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        favoritos = dbHelper.getAllFavoritos();
        //Agarrar el recyclerview de categorias
        favoritosRv = (RecyclerView) findViewById(R.id.favoritos_rv);

        //Asociar un adapter de Categoria a ese recyclerview
        favoritosListAdapter = new FavoritosListAdapter(favoritos, this);
        favoritosRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        favoritosRv.setAdapter(favoritosListAdapter);
        favoritosListAdapter.notifyDataSetChanged();
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
