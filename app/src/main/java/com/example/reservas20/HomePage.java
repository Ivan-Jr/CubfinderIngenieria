package com.example.reservas20;

import static com.example.reservas20.R.drawable.ic_cerrar_sesion;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;

public class HomePage extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;

        // Primera pestaña: Cubiculos
        Intent intentCubiculos = new Intent().setClass(this, CubiculosPage.class);
        spec = tabHost.newTabSpec("Cubiculos").setIndicator("Cubiculos").setContent(intentCubiculos);
        tabHost.addTab(spec);

        // Segunda pestaña: Mesas
        Intent intentMesas = new Intent().setClass(this, MesasPage.class);
        spec = tabHost.newTabSpec("Mesas").setIndicator("Mesas").setContent(intentMesas);
        tabHost.addTab(spec);

        // Tercera pestaña: Casilleros
        Intent intentCasilleros = new Intent().setClass(this, CasillerosPage.class);
        spec = tabHost.newTabSpec("Casilleros").setIndicator("Casilleros").setContent(intentCasilleros);
        tabHost.addTab(spec);

        // Cuarta pestaña: Cerrar Sesion
        Intent intentCerrar = new Intent().setClass(this, CerrarSesionPage.class);
        spec = tabHost.newTabSpec("Cerrar Sesion");
        ImageView iconoCerrarSesion = new ImageView(this);
        iconoCerrarSesion.setImageResource(R.drawable.cerrar_sesion);
        spec.setIndicator(iconoCerrarSesion);
        spec.setContent(intentCerrar);
        tabHost.addTab(spec);

        // Botón de Chat en Vivo
        Button chatButton = findViewById(R.id.chat_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(HomePage.this, ChatActivity.class);
                // Si necesitas pasar un chatPartnerId, hazlo aquí
                chatIntent.putExtra("chatPartnerId", "ID_DEL_CHAT_PARTNER"); // Cambia esto según tu lógica
                startActivity(chatIntent);
            }
        });

        tabHost.setCurrentTab(0);
    }
}
