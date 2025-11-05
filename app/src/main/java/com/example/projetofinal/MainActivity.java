package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        // REMOVA o EdgeToEdge e configure normalmente
        setContentView(R.layout.activity_main);

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
        LinearLayout itemConcentracao = findViewById(R.id.itemConcentracao);
        LinearLayout itemDensidade = findViewById(R.id.itemDensidade);
        LinearLayout itemNumMol = findViewById(R.id.itemNumMol);
        LinearLayout itemVolGas = findViewById(R.id.itemVolGas);
        LinearLayout itemDiluicao = findViewById(R.id.itemDiluicao);

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
        itemConcentracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraConcentracao();
            }
        });
        itemDensidade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraDensidade();
            }
        });
        itemNumMol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraNumeroMol();
            }
        });
        itemVolGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraVolGas();
            }
        });
        itemDiluicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCalculadoraDiluicao();
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
        }, INTERVALO_CURIOSIDADE, INTERVALO_CURIOSIDADE);
    }

    private void carregarCuriosidade() {
        TextView textViewCuriosidade = findViewById(R.id.textViewCuriosidade);
        if (textViewCuriosidade != null) {
            String curiosidade = curiosidadeHelper.obterProximaCuriosidade();
            textViewCuriosidade.setText(curiosidade);
        }
    }

    private void irParaCalculadoraMassaAtomica() {
        Intent intent = new Intent(this, CalculadoraMassaAtomica.class);
        startActivity(intent);
    }
    private void irParaCalculadoraNumeroMol() {
        Intent intent = new Intent(this, CalculadoraNumeroMol.class);
        startActivity(intent);
    }

    private void irParaCalculadoraMassaMolar() {
        Intent intent = new Intent(this, CalculadoraMassaMolar.class);
        startActivity(intent);
    }

    private void irParaCalculadoraConcentracao() {
        Intent intent = new Intent(this, CalculadoraConcentracao.class);
        startActivity(intent);
    }

    private void irParaCalculadoraDensidade() {
        Intent intent = new Intent(this, CalculadoraDensidade.class);
        startActivity(intent);
    }

    private void irParaCalculadoraVolGas() {
        Intent intent = new Intent(this, CalculadoraVolumeGas.class);
        startActivity(intent);
    }
    private void irParaCalculadoraDiluicao() {
        Intent intent = new Intent(this, CalculadoraDiluicao.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer == null) {
            iniciarTimerCuriosidades();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
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