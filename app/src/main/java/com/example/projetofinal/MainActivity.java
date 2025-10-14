package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout itemMassaAtomica = findViewById(R.id.itemMassaAtomica);
        LinearLayout itemMassaMolar = findViewById(R.id.itemMassaMolar);

        // Adicionar o listener de clique
        itemMassaAtomica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraMassaAtomica();
            }
        });
        itemMassaMolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraMassaMolar();
            }
        });

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(MainActivity.this, v));
    }

    private void irParaCalculadoraMassaAtomica() {
        Intent intent = new Intent(this, CalculadoraMassaAtomica.class);
        startActivity(intent);
    }

    private void irParaCalculadoraMassaMolar() {
        Intent intent = new Intent(this, CalculadoraMassaMolar.class);
        startActivity(intent);
    }
}