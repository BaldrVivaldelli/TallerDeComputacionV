package com.tcv.peliculas.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.model.LoginCredentials;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
            if(credencialesExisten()) {
                Intent intent =
                        new Intent(LoginActivity.this,
                        CategoriasActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            } else {
                inicializarVista();
            }
        }

    private boolean credencialesExisten() {
        SharedPreferences sharedPreferences =
        LoginActivity.this.getSharedPreferences(
                getString(R.string.app_name),Context.MODE_PRIVATE);
        return sharedPreferences.contains("usuario");
    }

    private void inicializarVista() {
        final EditText userEditText = (EditText) findViewById(R.id.usuario_et);
        final EditText passwordEditText = (EditText) findViewById(R.id.password_et);
        Button recuperarBtn = (Button) findViewById(R.id.recuperar_btn);
        Button ingresarBtn = (Button) findViewById(R.id.enter_btn);

        ingresarBtn.setOnClickListener(new View.OnClickListener() {

            private int intentos = 0;

            @Override
            public void onClick(View view) {
                final String usuario = userEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                LoginCredentials data = new LoginCredentials(usuario,password);
                ApiClient.getInstance().getClient(getApplicationContext()).login(data.getUser(),data.getPwd()).enqueue(new Callback<com.tcv.peliculas.api.LoginResponse>() {
                    @Override
                    public void onResponse(Call<com.tcv.peliculas.api.LoginResponse> call, Response<com.tcv.peliculas.api.LoginResponse> response) {
                        if (response.body().getData().getUsername().equals(usuario) && response.body().getData().getPassword().equals(password)) {
                            persistirCredenciales("alan","1234");
                            Intent intent = new Intent(LoginActivity.this,
                                    CategoriasActivity.class);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            intentos++;
                            if(intentos == 3) {
                                Toast.makeText(LoginActivity.this,
                                        "Hubo mas de 3 intentos fallidos, cerrando aplicacion.",
                                        Toast.LENGTH_LONG).show();
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "El usuario o contrasenia ingresados es incorrecto...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<com.tcv.peliculas.api.LoginResponse> call, Throwable throwable) {

                    }
                });

            }
        });

        recuperarBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                enviarEmail();
            }
        });
    }

    private void persistirCredenciales(String usuario, String contraseña)
    {
        SharedPreferences sharedPreferences =
                LoginActivity.this.getSharedPreferences(getString(R.string.app_name),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario",usuario);
        editor.commit();
    }

    private void enviarEmail() {
        final EditText usuarioEt = (EditText) findViewById(R.id.usuario_et);
        Intent emailIntent = new Intent (android.content.Intent.ACTION_SEND);
        emailIntent.setType ("simple / texto");
        String emailTo = "user@fakehost.com";
        String app = "soporte@Argentflix.com";
        String subject = "Problema para ingresar a Argenflix";
        String texto = "Mi usuario es  "+usuarioEt.getText().toString()
                +", solicito un blanqueo de clave. ";
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTo);
        emailIntent.putExtra(Intent.EXTRA_CC, app);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, texto);
        startActivity (emailIntent);
    }
}
