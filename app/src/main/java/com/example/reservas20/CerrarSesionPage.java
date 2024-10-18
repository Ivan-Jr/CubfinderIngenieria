package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CerrarSesionPage extends AppCompatActivity {

    Button btncerrarsesion;
    ImageView imgPerfil;
    TextView txtCarnet;
    TextView txtNombre;

    // URL base de tu servidor
    private static final String BASE_URL = "http://192.168.0.12/reservas_app/"; // Cambia esto por la URL real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_sesion_page);

        // Inicializar vistas
        btncerrarsesion = findViewById(R.id.btn_cerrarSesion);
        imgPerfil = findViewById(R.id.img_perfil);
        txtCarnet = findViewById(R.id.txt_carnet);
        txtNombre = findViewById(R.id.txt_nombre);

        // Obtener el email del intent
        Intent intent = getIntent();
        String emailUsuario = intent.getStringExtra("emailUsuario"); // Asegúrate de enviar el email desde la actividad anterior

        // Obtener datos del usuario
        obtenerDatosUsuario(emailUsuario);

        // Botón de cerrar sesión
        btncerrarsesion.setOnClickListener(view -> {
            Intent intentCerrarSesion = new Intent(CerrarSesionPage.this, MainActivity.class);
            startActivity(intentCerrarSesion);
            finish();
        });
    }

    private void obtenerDatosUsuario(String email) {
        // Configurar Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Usuario> call = apiService.obtenerDatosUsuario(email);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, retrofit2.Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();

                    // Actualiza la UI
                    txtCarnet.setText("Carnet: " + usuario.getCarnet());
                    txtNombre.setText("Nombre: " + usuario.getNombre());

                    // Usar Glide para cargar la imagen de perfil desde la URL almacenada en la base de datos
                    Glide.with(CerrarSesionPage.this)
                            .load(usuario.getImagenPerfil()) // URL de la imagen de perfil
                            .into(imgPerfil); // ImageView donde mostrar la imagen
                } else {
                    Toast.makeText(CerrarSesionPage.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e("CerrarSesionPage", "Error en la solicitud", t);
                Toast.makeText(CerrarSesionPage.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CerrarSesionPage.this, HomePage.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
