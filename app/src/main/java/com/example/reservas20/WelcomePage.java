package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    Button btnContinuar;

    TextView txtReportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        btnContinuar = findViewById(R.id.btn_continuar);
        txtReportar = findViewById(R.id.txt_reporte);

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txtReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, ReportarPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}