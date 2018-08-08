package com.tcv.peliculas.controller.PelisDelMes;


import android.content.Context;
import android.content.SharedPreferences;

import com.tcv.peliculas.R;

public class PeliculasDelMesViewModel {

    private Context context;

    public PeliculasDelMesViewModel(Context context) {
        this.context = context;
    }

    public String getUsername() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_categoria),
                Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuario", "");
    }
}
