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

public class CasillerosPage extends AppCompatActivity {

    private TextView txtFechaReserva;  // TextView para mostrar la fecha seleccionada
    private EditText editTiempoUso;     // EditText para ingresar el tiempo de uso
    private Button btnReservar;          // Botón para realizar la reserva
    private Calendar fechaSeleccionada;  // Variable para almacenar la fecha seleccionada
    private ApiService apiService;       // Servicio API para realizar la reserva

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casilleros_page); // Carga del layout

        // Inicializar Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        // Vincular componentes con el layout
        txtFechaReserva = findViewById(R.id.txt_fecha_reserva);
        editTiempoUso = findViewById(R.id.edit_tiempo_uso);
        btnReservar = findViewById(R.id.btn_reservar);

        // Selección de la fecha usando un DatePickerDialog
        txtFechaReserva.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int year = calendario.get(Calendar.YEAR);
            int month = calendario.get(Calendar.MONTH);
            int day = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(CasillerosPage.this,
                    (view, year1, month1, dayOfMonth) -> {
                        fechaSeleccionada = Calendar.getInstance();
                        fechaSeleccionada.set(year1, month1, dayOfMonth);
                        txtFechaReserva.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    }, year, month, day);

            // Restringir la selección de fechas anteriores al día actual
            datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            datePicker.show();
        });

        // Acción para el botón Reservar
        btnReservar.setOnClickListener(v -> {
            String tiempoUsoStr = editTiempoUso.getText().toString();

            // Validar que los campos no estén vacíos
            if (fechaSeleccionada == null) {
                Toast.makeText(CasillerosPage.this, "Por favor, selecciona una fecha", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(tiempoUsoStr)) {
                Toast.makeText(CasillerosPage.this, "Por favor, ingresa el tiempo de uso", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convertir a valores numéricos
            int tiempoUso = Integer.parseInt(tiempoUsoStr);

            // Validar el tiempo de uso (debe estar entre 1 y 2 horas)
            if (tiempoUso < 1 || tiempoUso > 2) {
                Toast.makeText(CasillerosPage.this, "El tiempo de uso debe ser entre 1 y 2 horas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lógica para guardar en MySQL
            guardarEnMySQL(tiempoUso);
        });
    }

    private void guardarEnMySQL(int tiempoUso) {
        // Formatear la fecha en el formato correcto (YYYY-MM-DD)
        String fechaFormateada = String.format("%04d-%02d-%02d",
                fechaSeleccionada.get(Calendar.YEAR),
                fechaSeleccionada.get(Calendar.MONTH) + 1,
                fechaSeleccionada.get(Calendar.DAY_OF_MONTH));

        // Realizar la reserva a través del API
        Call<ResponseBody> call = apiService.realizarReservaCasillero(fechaFormateada, tiempoUso);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CasillerosPage.this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();

                    // Redirigir a la pantalla HomePage
                    Intent intent = new Intent(CasillerosPage.this, HomePage.class);
                    startActivity(intent);
                    finish(); // Finaliza la actividad actual
                } else {
                    // Manejo de errores
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(CasillerosPage.this, "Error al guardar en la base de datos: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(CasillerosPage.this, "Error desconocido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CasillerosPage.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
