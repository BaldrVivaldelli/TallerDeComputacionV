package com.tcv.peliculas.persistence;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "argenflix_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Favorito.CREATE_TABLE);
        initialize(db);
    }

    private void initialize(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertarFavorito(int peliculaId, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Favorito.COLUMN_PELICULA, peliculaId);
        insertContent(values, db);
    }

    public void deleteFavorito(int peliculaId, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        String[] whereArgs = new String[] { String.valueOf(peliculaId) };
        deleteContent(whereArgs, db);
    }

    private void insertContent(ContentValues values, SQLiteDatabase db) {
        boolean closeDb = false;
        if(db == null) {
            db = this.getWritableDatabase();
            closeDb = true;
        }
        db.insert(Favorito.TABLE_NAME, null, values);
        if(closeDb)
            db.close();
    }

    private void deleteContent(String[] whereArgs, SQLiteDatabase db) {
        boolean closeDb = false;
        if(db == null) {
            db = this.getWritableDatabase();
            closeDb = true;
        }
        db.delete(Favorito.TABLE_NAME, Favorito.COLUMN_PELICULA + "=?", whereArgs);
        if(closeDb)
            db.close();
    }

    public boolean getIfIsFavorito(int idPelicula) {
        //1º
        SQLiteDatabase db = this.getReadableDatabase();

        //2º

        Cursor cursor = db.rawQuery("SELECT COUNT(" + Favorito.COLUMN_ID + ") AS fav FROM " + Favorito.TABLE_NAME + " WHERE " +
                Favorito.COLUMN_PELICULA + "=?;",
                new String[]{String.valueOf(idPelicula)});

        //3º
        int result = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            result = cursor.getColumnIndex("fav");
        }

        // close the db connection
        cursor.close();
        db.close();
        return (result > 0);
    }

    public List<Favorito> getAllFavoritos() {
        //1º
        SQLiteDatabase db = this.getReadableDatabase();

        //2º
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                Favorito.TABLE_NAME, null);

        //3º
        List<Favorito> favoritos = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                favoritos.add(new Favorito(
                        cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndex(Favorito.COLUMN_PELICULA))));
            } while (cursor.moveToNext());

            // close the db connection
            cursor.close();
            db.close();
        }
        return favoritos;
    }
}
