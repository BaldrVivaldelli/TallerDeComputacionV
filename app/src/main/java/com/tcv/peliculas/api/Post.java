package com.tcv.peliculas.api;

/**
 * Created by nirav on 21/02/16.
 */
public class Post {

    private String postTitle;

    private String postSubTitle;

    private String videoDetails;

    private String videoUrl;
    private String id;

//el tema es que ger hizo mal las categorias para tener que agregarlo vas a tener que agregarle
    //al modelo de pelicula el tag URL_Imagen

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostSubTitle() {
        return postSubTitle;
    }

    public void setPostSubTitle(String postSubTitle) {
        this.postSubTitle = postSubTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Post(String postTitle, String postSubTitle, String videoDetails, String videoUrl, String id) {
        this.postTitle = postTitle;
        this.postSubTitle = postSubTitle;
        this.videoDetails = videoDetails;
        this.videoUrl = videoUrl;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(String videoDetails) {
        this.videoDetails = videoDetails;
    }

}
