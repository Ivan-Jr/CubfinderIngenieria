package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPage extends AppCompatActivity {

    TextInputEditText editTextNombre, editTextEmail, editTextContraseña, editTextCarnet;
    Button btnRegistrarse, btnSubirFoto;
    TextView txtIniciarSesion;
    ImageView imgPerfilRegistro;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;  // Para almacenar la URI de la imagen seleccionada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Inicializar vistas
        editTextNombre = findViewById(R.id.nombreRegistrar); // Asegúrate de tener este campo en tu layout
        editTextEmail = findViewById(R.id.emailRegistrar);
        editTextContraseña = findViewById(R.id.contraseñaRegistrar);
        editTextCarnet = findViewById(R.id.carnetRegistrar);
        btnRegistrarse = findViewById(R.id.btn_Registrarse);
        txtIniciarSesion = findViewById(R.id.txt_IniciaSesion);
        imgPerfilRegistro = findViewById(R.id.img_perfil_registro);
        btnSubirFoto = findViewById(R.id.btn_subir_foto);

        // Redireccionar a la pantalla de inicio de sesión
        txtIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Lógica para seleccionar una imagen de la galería
        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        // Lógica para registrar al usuario
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = String.valueOf(editTextNombre.getText()); // Captura el nombre
                String email = String.valueOf(editTextEmail.getText());
                String contraseña = String.valueOf(editTextContraseña.getText());
                String carnet = String.valueOf(editTextCarnet.getText());

                // Validaciones
                if (TextUtils.isEmpty(nombre)) {
                    Toast.makeText(RegisterPage.this, "Escriba su nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterPage.this, "Escriba su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contraseña)) {
                    Toast.makeText(RegisterPage.this, "Escriba su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(carnet)) {
                    Toast.makeText(RegisterPage.this, "Escriba su carnet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageUri == null) {
                    Toast.makeText(RegisterPage.this, "Seleccione una foto de perfil", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Registrar al usuario en la base de datos MySQL
                registerUser(imageUri.toString(), nombre, carnet, email, contraseña); // Orden correcto
            }
        });
    }

    // Método para abrir el selector de archivos (galería)
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Método que recibe la imagen seleccionada y la muestra en el ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPerfilRegistro.setImageURI(imageUri);
        }
    }

    // Método para registrar al usuario en la base de datos MySQL
    private void registerUser(String imagenPerfil, String nombre, String carnet, String email, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.registrarUsuario(imagenPerfil, nombre, carnet, email, password); // Orden correcto

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterPage.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterPage.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegisterPage.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterPage.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
