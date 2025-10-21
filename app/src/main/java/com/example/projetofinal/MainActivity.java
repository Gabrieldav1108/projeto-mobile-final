package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView btnMenu;
    private CuriosidadeHelper curiosidadeHelper;
    private Timer timer;
    private Handler handler;
    private static final long INTERVALO_CURIOSIDADE = 15000; // 15 segundos em milissegundos

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

        // Inicializar o helper do banco de dados de curiosidades
        curiosidadeHelper = new CuriosidadeHelper(this);

        // Inicializar Handler para atualizações na UI
        handler = new Handler();

        // Carregar curiosidade inicial
        carregarCuriosidade();

        // Iniciar timer para atualizar curiosidades automaticamente
        iniciarTimerCuriosidades();

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

    private void iniciarTimerCuriosidades() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Executar na thread principal para atualizar a UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        carregarCuriosidade();
                    }
                });
            }
        }, INTERVALO_CURIOSIDADE, INTERVALO_CURIOSIDADE); // Primeira execução após 15s, depois a cada 15s
    }

    private void carregarCuriosidade() {
        TextView textViewCuriosidade = findViewById(R.id.textViewCuriosidade);
        if (textViewCuriosidade != null) {
            String curiosidade = curiosidadeHelper.obterProximaCuriosidade();
            textViewCuriosidade.setText(curiosidade);

            // Opcional: Mostrar um toast rápido indicando a atualização
            // Toast.makeText(this, "Nova curiosidade!", Toast.LENGTH_SHORT).show();
        }
    }

    private void irParaCalculadoraMassaAtomica() {
        Intent intent = new Intent(this, CalculadoraMassaAtomica.class);
        startActivity(intent);
    }

    private void irParaCalculadoraMassaMolar() {
        Intent intent = new Intent(this, CalculadoraMassaMolar.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reiniciar o timer quando a activity for retomada
        if (timer == null) {
            iniciarTimerCuriosidades();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Parar o timer quando a activity for pausada para economizar recursos
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        // Parar o timer e liberar recursos
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (curiosidadeHelper != null) {
            curiosidadeHelper.close();
        }
        super.onDestroy();
    }
}