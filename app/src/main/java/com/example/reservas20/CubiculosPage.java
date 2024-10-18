package com.example.reservas20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class CubiculosPage extends AppCompatActivity {


    Button detalles1, detalles2, detalles3, detalles4, detalles5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cubiculos_page);


        detalles1 = findViewById(R.id.btn_detalles1);
        detalles2 = findViewById(R.id.btn_detalles2);
        detalles3 = findViewById(R.id.btn_detalles3);
        detalles4 = findViewById(R.id.btn_detalles4);
        detalles5 = findViewById(R.id.btn_detalles5);



        detalles1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CubiculosPage.this, ReservasPage.class);
                startActivity(intent);
                finish();

            }
        });
        detalles2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CubiculosPage.this, ReservasPage.class);
                startActivity(intent);
                finish();

            }
        });
        detalles3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CubiculosPage.this, ReservasPage.class);
                startActivity(intent);
                finish();

            }
        });
        detalles4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CubiculosPage.this, ReservasPage.class);
                startActivity(intent);
                finish();

            }
        });
        detalles5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CubiculosPage.this, ReservasPage.class);
                startActivity(intent);
                finish();

            }
        });
    }






}