package com.tcv.peliculas.Favoritos;

public class FavoritoDatabase {

    private int id;
    private int idpelicula;
    private String usuario;
    private String titulo;
    private String imagen;

    public static final String TABLE_NAME = "favoritos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PELICULA_ID = "idpelicula";
    public static final String COLUMN_USUARIO = "usuario";
    public static final String COLUMN_PELICULA_TITULO = "titulo";
    public static final String COLUMN_PELICULA_IMAGEN= "imagen";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public FavoritoDatabase(int id, int idpelicula, String usuario, String titulo, String imagen) {
        this.id = id;
        this.idpelicula = idpelicula;
        this.usuario = usuario;
        this.titulo = titulo;
        this.imagen = imagen;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PELICULA_ID + " INT,"
                    + COLUMN_USUARIO + " TEXT,"
                    + COLUMN_PELICULA_TITULO + " TEXT,"
                    + COLUMN_PELICULA_IMAGEN + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public int getIdPelicula() {
        return idpelicula;
    }

    public String getTituloPelicula() {
        return titulo;
    }

    public String getImagenPelicula() {
        return imagen;
    }

    public String getUsuario() {
        return usuario;
    }

    public int getId() {
        return id;
    }
}
