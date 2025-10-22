package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MassaMolar extends AppCompatActivity {
    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // REMOVA o EdgeToEdge e configure normalmente
        setContentView(R.layout.activity_massa_molar);

        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        TextView btnCalculadora = findViewById(R.id.btnCalculadora);

        btnVoltar.setOnClickListener(v -> {
            finish(); // Fecha a tela atual e volta para a anterior
        });

        btnCalculadora.setOnClickListener(v -> {
            Intent i = new Intent(this, CalculadoraMassaMolar.class);
            startActivity(i);
        });

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));
    }
}