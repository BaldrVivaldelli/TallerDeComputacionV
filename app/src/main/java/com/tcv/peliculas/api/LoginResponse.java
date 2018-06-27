package com.tcv.peliculas.api;


import com.tcv.peliculas.model.User;

public class LoginResponse {

    private String status;
    private User data;


    public LoginResponse(String status, User data) {
        this.status = status;
        this.data = data;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
