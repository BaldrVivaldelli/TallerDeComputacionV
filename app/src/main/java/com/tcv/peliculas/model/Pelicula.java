package com.tcv.peliculas.model;


import java.util.List;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private int imagen;
    private int imagenMini;
    private String lanzamiento;
    private int duracion;
    private List<String> artistas;
    private double puntuacion;
    private String large_img_url;

    public String getSmall_img_url() {
        return small_img_url;
    }

    public void setSmall_img_url(String small_img_url) {
        this.small_img_url = small_img_url;
    }

    private String small_img_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setLanzamiento(String lanzamiento) {
        this.lanzamiento = lanzamiento;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setArtistas(List<String> artistas) {
        this.artistas = artistas;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getLarge_img_url() {
        return large_img_url;
    }

    public void setLarge_img_url(String large_img_url) {
        this.large_img_url = large_img_url;
    }

    private String videoUrl;


    public Pelicula(String titulo,
                    String genero, int imagen) {
        this.titulo = titulo;
        this.genero = genero;
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public int getImagen() {
        return imagen;
    }

    public int getId() {
        return id;
    }

    public String getLanzamiento() {
        return lanzamiento;
    }

    public List<String> getArtistas() {
        return artistas;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public int getImagenMini() {
        return imagenMini;
    }

    public void setImagenMini(int imagenMini) {
        this.imagenMini = imagenMini;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}