package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

public class massa_atomica extends AppCompatActivity {
    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // REMOVA o EdgeToEdge e configure normalmente
        setContentView(R.layout.activity_massa_atomica);

        ImageView btnVoltar = findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> {
            finish(); // Fecha a tela atual e volta para a anterior
        });

        TextView btnCalculadora = findViewById(R.id.btnCalculadora);

        btnCalculadora.setOnClickListener(v -> {
            Intent i = new Intent(this, CalculadoraMassaAtomica.class);
            startActivity(i);
        });

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));
    }
}