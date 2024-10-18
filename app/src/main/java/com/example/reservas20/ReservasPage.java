package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReservasPage extends AppCompatActivity {

    Button btnreservar, btnreportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas_page);

        btnreservar = findViewById(R.id.btn_reservar);
        btnreportar = findViewById(R.id.btn_reportar);

        btnreservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservasPage.this, ReservasPantalla.class);
                startActivity(intent);
                finish();
            }
        });

        btnreportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReservasPage.this, ReglamentoPage.class);
                startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReservasPage.this, HomePage.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}

