package com.tcv.peliculas.model;

import java.util.ArrayList;

/**
 * Created by Alumno on 6/6/2018.
 */

public class Categoria {
    private int id;
    private String titulo;
    private ArrayList<Pelicula> peliculas;

    public Categoria(int id, String titulo){
        this.id = id;
        this.titulo = titulo;
        peliculas = new ArrayList<Pelicula>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(ArrayList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }
}
