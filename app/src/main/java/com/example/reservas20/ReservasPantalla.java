package com.example.reservas20;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservasPantalla extends AppCompatActivity {

    private TextView txtFechaReserva;
    private EditText editTiempoUso, editNumPersonas;
    private Button btnContinuar, btnCancelar;
    private Calendar fechaSeleccionada;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_pantalla);

        // Inicializar Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        // Vincular componentes con el layout
        txtFechaReserva = findViewById(R.id.txt_fecha_reserva);
        editTiempoUso = findViewById(R.id.edit_tiempo_uso);
        editNumPersonas = findViewById(R.id.edit_num_personas);
        btnContinuar = findViewById(R.id.btn_continuar);
        btnCancelar = findViewById(R.id.btn_cancelar); // Nuevo botón

        // Selección de la fecha usando un DatePickerDialog
        txtFechaReserva.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(ReservasPantalla.this,
                    (view, year1, month1, dayOfMonth) -> {
                        fechaSeleccionada = Calendar.getInstance();
                        fechaSeleccionada.set(year1, month1, dayOfMonth);
                        txtFechaReserva.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    }, year, month, day);

            // Restringir la selección de fechas anteriores al día actual
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePicker.show();
        });

        // Acciones para el botón Continuar
        btnContinuar.setOnClickListener(v -> {
            String tiempoUsoStr = editTiempoUso.getText().toString();
            String numPersonasStr = editNumPersonas.getText().toString();

            // Validar que los campos no estén vacíos
            if (fechaSeleccionada == null) {
                Toast.makeText(ReservasPantalla.this, "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(tiempoUsoStr) || TextUtils.isEmpty(numPersonasStr)) {
                Toast.makeText(ReservasPantalla.this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir a valores numéricos
            int tiempoUso = Integer.parseInt(tiempoUsoStr);
            int numPersonas = Integer.parseInt(numPersonasStr);

            // Validar el tiempo de uso (debe estar entre 1 y 2 horas)
            if (tiempoUso < 1 || tiempoUso > 2) {
                Toast.makeText(ReservasPantalla.this, "El tiempo de uso debe ser entre 1 y 2 horas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar el número de personas (debe estar entre 3 y 6)
            if (numPersonas < 3) {
                Toast.makeText(ReservasPantalla.this, "El mínimo es 3 personas", Toast.LENGTH_SHORT).show();
            } else if (numPersonas > 6) {
                Toast.makeText(ReservasPantalla.this, "El máximo es 6 personas", Toast.LENGTH_SHORT).show();
            } else {
                // Lógica para guardar en MySQL
                guardarEnMySQL(tiempoUso, numPersonas);
            }
        });

        // Acción para el botón Cancelar
        btnCancelar.setOnClickListener(v -> {
            Intent intent = new Intent(ReservasPantalla.this, ReservasPage.class);
            startActivity(intent);
            finish(); // Finaliza la actividad actual
        });
    }

    private void guardarEnMySQL(int tiempoUso, int numPersonas) {
        // Formatear la fecha en el formato correcto (YYYY-MM-DD)
        String fechaFormateada = String.format("%04d-%02d-%02d",
                fechaSeleccionada.get(Calendar.YEAR),
                fechaSeleccionada.get(Calendar.MONTH) + 1,
                fechaSeleccionada.get(Calendar.DAY_OF_MONTH));

        // Realizar la reserva a través del API
        Call<ResponseBody> call = apiService.realizarReservaCubiculo(fechaFormateada, tiempoUso, numPersonas);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservasPantalla.this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir a la pantalla HomePage
                    Intent intent = new Intent(ReservasPantalla.this, HomePage.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad actual
                } else {
                    // Manejo de errores
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(ReservasPantalla.this, "Error al guardar en la base de datos: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ReservasPantalla.this, "Error desconocido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ReservasPantalla.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
