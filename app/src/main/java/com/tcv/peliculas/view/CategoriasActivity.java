package com.tcv.peliculas.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Categorias.CategoriasListAdapter;
import com.tcv.peliculas.controller.Categorias.CategoriasViewModel;
import com.tcv.peliculas.model.Categoria;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView categoriasRv;
    private CategoriasListAdapter categoriasAdapter;
    private List<Categoria> categorias;

    private CategoriasViewModel categoriasViewModel = new CategoriasViewModel(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Como es el layout de categorias
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Agarrar el recyclerview de categorias
        categorias = new ArrayList<>();
        categoriasRv = (RecyclerView) findViewById(R.id.categorias_rv);

        //Asociar un adapter de Categoria a ese recyclerview
        categoriasAdapter = new CategoriasListAdapter(categorias, this);
        categoriasRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoriasRv.setAdapter(categoriasAdapter);
        obtenerCategorias();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }  else if (id == R.id.cerrar_sesion) {
            cerrarSesion();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);

        mSearch.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CategoriasActivity.this,
                        BuscadorActivity.class);
                CategoriasActivity.this.startActivity(intent);
                CategoriasActivity.this.finish();
                return true;
            }
        });
        /*
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String titulo) {
                search(titulo);
                return true;
            }
        });
        */
        return super.onCreateOptionsMenu(menu);
    }

    private void search(final String titulo){
        if (!titulo.equals("")){
            ApiClient.getClient(this).getCategorias().enqueue(new Callback<List<Categoria>>() {
                @Override
                public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                    categorias.clear();
                    List<Categoria> categoriasResponse = response.body();
                    List<Categoria> categoriasToRemove = new ArrayList<>();
                    List<Pelicula> peliculasToRemove = new ArrayList<>();
                    try{
                        for (Categoria categoria : categoriasResponse) {
                            int peliculasCount = 0;
                            for (Pelicula pelicula : categoria.getPeliculas()) {
                                //Si la pelicula no contiene el texto buscado en el titulo lo agrega a peliculas a remover.
                                if(!pelicula.getTitulo().toLowerCase().contains(titulo.toLowerCase())){
                                    peliculasCount++;
                                    peliculasToRemove.add(pelicula);
                                }
                            }
                            //Si la categoria no contiene peliculas se agrega a categorias a remover.
                            if(peliculasCount == categoria.getPeliculas().size()){
                                categoriasToRemove.add(categoria);
                            }
                        }

                        //Se remueven las categorias vacias.
                        for (Categoria categoria : categoriasToRemove) {
                            categoriasResponse.remove(categoria);
                        }

                        //Se remueven las peliculas no encontradas.
                        for (Pelicula pelicula : peliculasToRemove) {
                            for (Categoria categoria : categoriasResponse) {
                                if(categoria.getPeliculas().contains(pelicula)){
                                    categoria.getPeliculas().remove(pelicula);
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(CategoriasActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    categorias.addAll(categoriasResponse);
                    categoriasAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<Categoria>> call, Throwable throwable) {
                    Toast.makeText(CategoriasActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            obtenerCategorias();
        }
    }

    private void obtenerCategorias(){
        ApiClient.getClient(this).getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                categorias.clear();
                List<Categoria> categoriasResponse = response.body();
                categorias.addAll(categoriasResponse);
                categoriasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable throwable) {
                Toast.makeText(CategoriasActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
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

    private void cerrarSesion() {
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}