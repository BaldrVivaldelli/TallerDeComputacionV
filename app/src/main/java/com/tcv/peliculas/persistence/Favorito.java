package com.tcv.peliculas.persistence;

public class Favorito {

    private int id;
    private int idpelicula;

    public static final String TABLE_NAME = "favoritos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PELICULA = "idpelicula";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public Favorito(int id, int idpelicula) {
         this.id = id;
         this.idpelicula = idpelicula;
    }

    public static final String CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PELICULA+ " INT,"
                + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

    public int getIdPelicula() {
        return idpelicula;
    }

    public int getId() {
        return id;
    }
}
