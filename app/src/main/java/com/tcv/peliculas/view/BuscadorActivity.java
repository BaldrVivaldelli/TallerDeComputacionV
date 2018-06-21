package com.tcv.peliculas.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Buscador.BuscadorListAdapter;
import com.tcv.peliculas.model.ChildRow;
import com.tcv.peliculas.model.ParentRow;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscadorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, android.widget.SearchView.OnCloseListener {

    private SearchManager searchManager;
    private android.widget.SearchView searchView;
    private BuscadorListAdapter listAdapter;
    private ExpandableListView myList;
    private ArrayList<ParentRow> parentList = new ArrayList<ParentRow>();
    private ArrayList<ParentRow> showTheseParentList = new ArrayList<ParentRow>();
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        parentList = new ArrayList<ParentRow>();
        showTheseParentList = new ArrayList<ParentRow>();

        // The app will crash if display list is not called here.
        displayList();

        // This expands the list.
        expandAll();


    }


    private void loadData() {
        ArrayList<ChildRow> childRows = new ArrayList<ChildRow>();
        ParentRow parentRow = null;
        ArrayList<Pelicula> peliculas = (ArrayList<Pelicula>) obtenerPeliculas();

        for (Pelicula pelicula: peliculas) {
            childRows = new ArrayList<ChildRow>();
            childRows.add(new ChildRow(R.mipmap.ic_launcher_round
                    ,pelicula.getTitulo()));

            parentRow = new ParentRow("First Group", childRows);
            parentList.add(parentRow);
        }
    }

    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.expandGroup(i);
        } //end for (int i = 0; i < count; i++)
    }

    private void displayList() {
        loadData();

        myList = (ExpandableListView) findViewById(R.id.expandableListView_search);
        listAdapter = new BuscadorListAdapter(this, parentList);

        myList.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo
                (searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onClose() {
        listAdapter.filterData("");
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        listAdapter.filterData(query);
        expandAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.filterData(newText);
        expandAll();
        return false;
    }
    private List<Pelicula> peliculas;
    private List<Pelicula> obtenerPeliculas() {
        {
            peliculas = new ArrayList<Pelicula>();
            ApiClient.getClient(this).getPeliculas().enqueue(new Callback<List<Pelicula>>() {

                @Override
                public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                    peliculas.clear();
                    List<Pelicula> peliculasResponse = response.body();
                    peliculas.addAll(peliculasResponse);
                }

                @Override
                public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                    Toast.makeText(BuscadorActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
                }

            });
            return peliculas;
        }
    }
}
