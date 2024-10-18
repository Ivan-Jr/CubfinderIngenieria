package com.example.reservas20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesasPage extends AppCompatActivity {

    private EditText etFecha, etHora, etNumPersonas;
    private Button btnReservar;
    private TextView tvError;
    private Calendar calendar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas_page);

        // Inicializar Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etNumPersonas = findViewById(R.id.etNumPersonas);
        btnReservar = findViewById(R.id.btnReservar); // Solo el botón de reservar
        tvError = findViewById(R.id.tvError);

        // Configurar el calendario
        calendar = Calendar.getInstance();

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarYReservar();
            }
        });
    }

    private void mostrarDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MesasPage.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        etFecha.setText(fechaSeleccionada);
                    }
                },
                year, month, day);

        // Restringir selección de fechas anteriores al día actual
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void validarYReservar() {
        String fecha = etFecha.getText().toString();
        String horaStr = etHora.getText().toString();
        String numPersonasStr = etNumPersonas.getText().toString();

        tvError.setVisibility(View.GONE);

        // Validaciones
        if (TextUtils.isEmpty(fecha)) {
            mostrarError("Fecha inválida");
            return;
        }

        if (TextUtils.isEmpty(horaStr)) {
            mostrarError("Hora inválida");
            return;
        }

        if (TextUtils.isEmpty(numPersonasStr)) {
            mostrarError("Número de personas requerido");
            return;
        }

        int hora;
        try {
            hora = Integer.parseInt(horaStr);
        } catch (NumberFormatException e) {
            mostrarError("Hora no válida");
            return;
        }

        // Validar que la hora no sea 0
        if (hora == 0) {
            mostrarError("La hora no puede ser 0");
            return;
        }

        if (hora > 2) {
            mostrarError("La hora no puede ser mayor a 2");
            return;
        }

        int numPersonas;
        try {
            numPersonas = Integer.parseInt(numPersonasStr);
        } catch (NumberFormatException e) {
            mostrarError("Número de personas no válido");
            return;
        }

        // Validar que el número de personas no sea 0
        if (numPersonas == 0) {
            mostrarError("El número de personas no puede ser 0");
            return;
        }

        if (numPersonas > 6) {
            mostrarError("El número de personas no puede ser mayor a 6");
            return;
        }

        // Convertir la fecha de dd/MM/yyyy a yyyy-MM-dd
        String[] partesFecha = fecha.split("/");
        if (partesFecha.length == 3) {
            fecha = partesFecha[2] + "-" + partesFecha[1] + "-" + partesFecha[0]; // yyyy-MM-dd
        }

        // Realizar la reserva en MySQL
        guardarEnMySQL(fecha, hora, numPersonas);
    }

    private void guardarEnMySQL(String fecha, int hora, int numPersonas) {
        // Realizar la reserva a través del API
        Call<ResponseBody> call = apiService.realizarReserva(fecha, String.valueOf(hora), numPersonas);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Limpia los campos después de guardar la reserva
                    limpiarCampos();

                    // Mostrar mensaje de éxito
                    Toast.makeText(MesasPage.this, "Reservado con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir a otra página si es necesario
                    Intent intent = new Intent(MesasPage.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Manejo de errores
                    try {
                        String errorBody = response.errorBody().string();
                        mostrarError("Error al guardar en la base de datos: " + errorBody);
                    } catch (Exception e) {
                        mostrarError("Error desconocido: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mostrarError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarError(String mensaje) {
        tvError.setText(mensaje);
        tvError.setVisibility(View.VISIBLE);
    }

    // Método para limpiar los campos
    private void limpiarCampos() {
        etFecha.setText("");
        etHora.setText("");
        etNumPersonas.setText("");
        tvError.setVisibility(View.GONE);
    }
}
