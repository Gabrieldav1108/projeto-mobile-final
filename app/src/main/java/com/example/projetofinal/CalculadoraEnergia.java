package com.example.projetofinal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculadoraEnergia extends AppCompatActivity {

    private EditText etMassa, etCalorEspecifico, etTempFinal, etTempInicial;
    private TextView btnCalcular, tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_energia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etMassa = findViewById(R.id.etMassa);
        etCalorEspecifico = findViewById(R.id.etCalorEspecifico);
        etTempFinal = findViewById(R.id.etTempFinal);
        etTempInicial = findViewById(R.id.etTempInicial);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);

        // Configurar botão voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        // Configurar menu
        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));
    }

    private void setupClickListeners() {
        btnCalcular.setOnClickListener(v -> calcularCalor());
    }

    private void calcularCalor() {
        String massaStr = etMassa.getText().toString();
        String calorEspecificoStr = etCalorEspecifico.getText().toString();
        String tempFinalStr = etTempFinal.getText().toString();
        String tempInicialStr = etTempInicial.getText().toString();

        // Validar campos vazios
        if (TextUtils.isEmpty(massaStr) || TextUtils.isEmpty(calorEspecificoStr) ||
                TextUtils.isEmpty(tempFinalStr) || TextUtils.isEmpty(tempInicialStr)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Converter valores para double
            double massa = Double.parseDouble(massaStr);
            double calorEspecifico = Double.parseDouble(calorEspecificoStr);
            double tempFinal = Double.parseDouble(tempFinalStr);
            double tempInicial = Double.parseDouble(tempInicialStr);

            // Calcular variação de temperatura
            double variacaoTemp = tempFinal - tempInicial;

            // Calcular quantidade de calor: Q = m × c × ΔT
            double calor = massa * calorEspecifico * variacaoTemp;

            // Exibir resultado
            String resultado = String.format("Q = %.2f J", calor);
            tvResultado.setText(resultado);

            Toast.makeText(this, "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }
}