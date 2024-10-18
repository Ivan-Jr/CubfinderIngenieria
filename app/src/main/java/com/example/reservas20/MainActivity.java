package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextContraseña;
    Button btnIniciarSesion;
    TextView txtregistrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.email);
        editTextContraseña = findViewById(R.id.contraseña);
        btnIniciarSesion = findViewById(R.id.btn_IniciarSesion);
        txtregistrate = findViewById(R.id.txt_Registrate);

        // Redirigir a la página de registro
        txtregistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

        // Iniciar sesión
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(editTextEmail.getText());
                String contraseña = String.valueOf(editTextContraseña.getText());

                // Validaciones
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Escriba su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(contraseña)) {
                    Toast.makeText(MainActivity.this, "Escriba su contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lógica para iniciar sesión usando la base de datos MySQL
                loginUser(email, contraseña);
            }
        });
    }

    // Método para autenticar al usuario en la base de datos MySQL
    private void loginUser(String email, String password) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.iniciarSesion(email, password); // Asegúrate de que tu ApiService tenga este método

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseString = response.body().string(); // Convertir la respuesta a string
                        JSONObject jsonResponse = new JSONObject(responseString);

                        // Verificar si hay error en la respuesta
                        if (jsonResponse.has("error")) {
                            Toast.makeText(MainActivity.this, jsonResponse.getString("error"), Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Verificar si el inicio de sesión fue exitoso
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(MainActivity.this, "Inicio de Sesión exitoso", Toast.LENGTH_SHORT).show();
                            // Redirigir a la HomePage
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Email o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException | IOException e) {
                        Toast.makeText(MainActivity.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error de inicio de sesión. Verifique sus credenciales.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, WelcomePage.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
