package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReglamentoPage extends AppCompatActivity {

    Button btncontinuar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamento_page);

        btncontinuar2 = findViewById(R.id.btn_continuar2);

        btncontinuar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReglamentoPage.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}