package com.tcv.peliculas.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcv.peliculas.Helper.PersistHelper;
import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.Categorias.CategoriasListAdapter;
import com.tcv.peliculas.controller.Categorias.CategoriasViewModel;
import com.tcv.peliculas.controller.Peliculas.PeliculasListAdapter;
import com.tcv.peliculas.controller.PelisDelMes.PeliculasDelMesListAdapter;
import com.tcv.peliculas.model.Categoria;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.model.PelisDelMes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,  ObservableScrollView.OnScrollChangedListener{

    private RecyclerView categoriasRv;
    private RecyclerView peliculasDelMesRv;
    private CategoriasListAdapter categoriasAdapter;
    private PeliculasDelMesListAdapter pelisDelMesAdapter;
    private ObservableScrollView mScrollView;
    private List<PelisDelMes> pelisDelMes;
    private List<Categoria> categorias;
    private String userName;
    private CategoriasViewModel categoriasViewModel = new CategoriasViewModel(this);
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private static final int LOCATION = 1;
    private View imgContainer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        mScrollView = (ObservableScrollView)findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangedListener(this);
        imgContainer = findViewById(R.id.img_container);
        Toolbar toolbarParallax = (Toolbar) findViewById(R.id.toolbarParallax);
        setSupportActionBar(toolbarParallax);
        if (toolbarParallax != null){

            toolbarParallax.setTitle("Peliculas mas votadas");
        }
        //HashSet<String> preferences = PersistHelper.getPreferencesCollectionByKey("usuario",getString(R.string.app_name),this);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ImageView imagenPerfil  = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imagenPerfil);
        TextView usuario_et = (TextView) navigationView.getHeaderView(0).findViewById(R.id.usuario_et);
        userName = PersistHelper.getPreferencesKeyByValue("usuario",this,getString(R.string.app_name));
        String userProfile = "profile_picture_" + userName;
        String imageProfile = PersistHelper.getPreferencesKeyByValue(userProfile,this,getString(R.string.app_name));



        TextView ciudad = (TextView) navigationView.getHeaderView(0).findViewById(R.id.ciudad_et);
        if (ContextCompat.checkSelfPermission(CategoriasActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CategoriasActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION))
            {
                ActivityCompat.requestPermissions (CategoriasActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION);
            }
            else
            {
                ActivityCompat.requestPermissions (CategoriasActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION);
            }
        }
        else
        {
            LocationManager locationManager = (LocationManager) getSystemService (Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation (LocationManager.NETWORK_PROVIDER);
            try
            {
                ciudad.setText ("Ciudad: " + GetHereLocation(location.getLatitude(), location.getLongitude()));
            }
            catch (Exception e)
            {
                e.printStackTrace ();
                Toast.makeText (CategoriasActivity.this, "Not found!", Toast.LENGTH_SHORT).show();
                ciudad.setText("Ciudad no encontrada");
            }
        }


        if(imageProfile != null){
            Bitmap result = PersistHelper.decodeToBase64(imageProfile);
            RoundedBitmapDrawable circularBitMap = circularBitMap(result);
            imagenPerfil.setImageDrawable(circularBitMap);

        }
        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                showPictureDialog();
            }
        });
        usuario_et.setText(userName);
        navigationView.setNavigationItemSelectedListener(this);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById((R.id.htab_collapse_toolbar));
        collapsingToolbarLayout.setTitle("Favoritos");
        categorias = new ArrayList<>();
        categoriasRv = (RecyclerView) findViewById(R.id.categorias_rv);
        peliculasDelMesRv = (RecyclerView) findViewById(R.id.peliculasDelMes_rv);
        pelisDelMes = new ArrayList<>();
        pelisDelMesAdapter = new PeliculasDelMesListAdapter(pelisDelMes, this);
        peliculasDelMesRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        peliculasDelMesRv.setAdapter(pelisDelMesAdapter);
        peliculasDelMesRv.bringToFront();
        //INSTANCCIO
        categoriasAdapter = new CategoriasListAdapter(categorias, this);
        categoriasRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoriasRv.setAdapter(categoriasAdapter);
        obtenerPelisDelmes();
        obtenerCategorias();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);//zero can be replaced with any action code
        }  else if (id == R.id.nav_gallery){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
        }else if(id == R.id.cerrar_sesion) {
            cerrarSesion();
        }else if(id == R.id.nav_favorite) {
            //navego a favoritos
            Intent intent = new Intent(CategoriasActivity.this,
                    FavoritosActivity.class);
            CategoriasActivity.this.startActivity(intent);
            CategoriasActivity.this.finish();
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


private RoundedBitmapDrawable circularBitMap(Bitmap originalBitmap){
    //creamos el drawable redondeado
    RoundedBitmapDrawable roundedDrawable =
            RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
    //asignamos el CornerRadius
    roundedDrawable.setCornerRadius(originalBitmap.getHeight());
    return roundedDrawable;
}


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView imageView = findViewById(R.id.imagenPerfil);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    //Uri selectedImage = imageReturnedIntent.getData();
                    //imageView.setImageURI(selectedImage);
                    Bitmap photo = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    RoundedBitmapDrawable circularBitMap = circularBitMap(photo);
                    imageView.setImageDrawable(circularBitMap);
                    String base64Imagen = PersistHelper.encodeToBase64(photo);
                    String userProfile = "profile_picture_" + userName;

                    PersistHelper.setPreferencesKeyByValue(userProfile,base64Imagen,getString(R.string.app_name),this);
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    imageView.setImageURI(selectedImage);
                    Uri selectedImageUri = imageReturnedIntent.getData();
                    String selectedImagePath =getPath(selectedImageUri);
                    imageView.setImageURI(selectedImageUri);
                    Bitmap bitmap = null;
                    RoundedBitmapDrawable circularBitMap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImageUri);
                        circularBitMap = circularBitMap(bitmap);
                        imageView.setImageDrawable(circularBitMap);
                    }catch (Exception e){

                    }
                    String base64Imagen = PersistHelper.encodeToBase64(bitmap);
                    String userProfile = "profile_picture_" + userName;
                    PersistHelper.setPreferencesKeyByValue(userProfile,base64Imagen,getString(R.string.app_name),this);

                }
                break;
        }
    }
    private String getPath(Uri uri)
    {
        String[] projection={MediaStore.Images.Media.DATA};
        Cursor cursor=managedQuery(uri,projection,null,null,null);
        int column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Obtener imagen");
        String[] pictureDialogItems = {
                "Camara",
                "Galeria" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case CAMERA:
                                    takePhotoFromCamera();
                                break;
                            case GALLERY:
                                choosePhotoFromGallary();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);//zero can be replaced with any action code
    }

    private void choosePhotoFromGallary(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
    }
    private void obtenerPelisDelmes(){
        ApiClient.getInstance().getClient(this).getPeliculasDelMes().enqueue(new Callback<List<PelisDelMes>>() {
            @Override
            public void onResponse(Call<List<PelisDelMes>> call, Response<List<PelisDelMes>> response) {
                pelisDelMes.clear();
                List<PelisDelMes> categoriasResponse = response.body();
                pelisDelMes.addAll(categoriasResponse);
                pelisDelMesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PelisDelMes>> call, Throwable throwable) {
                Toast.makeText(CategoriasActivity.this, "Ocurrio un error al querer obtener la lista de peliculas.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void obtenerCategorias(){
        ApiClient.getInstance().getClient(this).getCategorias().enqueue(new Callback<List<Categoria>>() {
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

        sharedPreferences.edit().remove("usuario").commit();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private String GetHereLocation (double lat, double lon)
    {
        String curCity = "";
        Geocoder geocoder = new Geocoder (CategoriasActivity.this, Locale.getDefault());
        List<Address> addressList;
        try
        {
            addressList = geocoder.getFromLocation (lat, lon, 1);
            if (addressList.size() > 0)
            {
                curCity = addressList.get(0).getLocality();
            }
            else
            {
                curCity = "City not found!";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return curCity;
    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {

        //Easy version.
        int scrollY = mScrollView.getScrollY();
        // Add parallax effect
        imgContainer.setTranslationY(scrollY * 1.3f);

    }
}