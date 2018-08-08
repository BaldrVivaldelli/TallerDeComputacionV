package com.tcv.peliculas.model;

import java.util.ArrayList;

/**
 * Created by Alumno on 6/6/2018.
 */

public class PelisDelMes {
    private int id;
    private ArrayList<Pelicula> peliculas;

    public PelisDelMes(int id, String titulo){
        this.id = id;
        peliculas = new ArrayList<Pelicula>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(ArrayList<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }
}
