package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NumeroMol extends AppCompatActivity {
    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_numero_mol);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        TextView btnCalculadora = findViewById(R.id.btnCalculadora);

        btnVoltar.setOnClickListener(v -> {
            finish(); // Fecha a tela atual e volta para a anterior
        });

        btnCalculadora.setOnClickListener(v -> {
            Intent i = new Intent(this, CalculadoraNumeroMol.class);
            startActivity(i);
        });

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));
    }
}