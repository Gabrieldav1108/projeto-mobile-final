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

public class CalculadoraDiluicao extends AppCompatActivity {

    private EditText etC1, etV1, etC2, etV2;
    private TextView btnCalcular, tvResultado, tvRelacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_diluicao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etC1 = findViewById(R.id.etC1);
        etV1 = findViewById(R.id.etV1);
        etC2 = findViewById(R.id.etC2);
        etV2 = findViewById(R.id.etV2);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
        tvRelacao = findViewById(R.id.tvRelacao);

        // Configurar botão voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        // Configurar menu
        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));
    }

    private void setupClickListeners() {
        btnCalcular.setOnClickListener(v -> calcularDiluicao());
    }

    private void calcularDiluicao() {
        String c1Str = etC1.getText().toString();
        String v1Str = etV1.getText().toString();
        String c2Str = etC2.getText().toString();
        String v2Str = etV2.getText().toString();

        // Contar quantos campos estão preenchidos
        int camposPreenchidos = 0;
        if (!TextUtils.isEmpty(c1Str)) camposPreenchidos++;
        if (!TextUtils.isEmpty(v1Str)) camposPreenchidos++;
        if (!TextUtils.isEmpty(c2Str)) camposPreenchidos++;
        if (!TextUtils.isEmpty(v2Str)) camposPreenchidos++;

        // Verificar se pelo menos 3 campos estão preenchidos
        if (camposPreenchidos < 3) {
            Toast.makeText(this, "Preencha pelo menos 3 dos 4 valores", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double c1 = TextUtils.isEmpty(c1Str) ? 0 : Double.parseDouble(c1Str);
            double v1 = TextUtils.isEmpty(v1Str) ? 0 : Double.parseDouble(v1Str);
            double c2 = TextUtils.isEmpty(c2Str) ? 0 : Double.parseDouble(c2Str);
            double v2 = TextUtils.isEmpty(v2Str) ? 0 : Double.parseDouble(v2Str);

            // Calcular a variável faltante usando a fórmula C₁V₁ = C₂V₂
            double resultado = 0;
            String variavelCalculada = "";

            if (TextUtils.isEmpty(c1Str)) {
                // Calcular C₁
                if (v1 == 0) {
                    Toast.makeText(this, "V₁ não pode ser zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultado = (c2 * v2) / v1;
                variavelCalculada = "C₁";
                tvRelacao.setText(String.format("C₁ = (C₂ × V₂) / V₁\nC₁ = (%.2f × %.2f) / %.2f", c2, v2, v1));
            } else if (TextUtils.isEmpty(v1Str)) {
                // Calcular V₁
                if (c1 == 0) {
                    Toast.makeText(this, "C₁ não pode ser zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultado = (c2 * v2) / c1;
                variavelCalculada = "V₁";
                tvRelacao.setText(String.format("V₁ = (C₂ × V₂) / C₁\nV₁ = (%.2f × %.2f) / %.2f", c2, v2, c1));
            } else if (TextUtils.isEmpty(c2Str)) {
                // Calcular C₂
                if (v2 == 0) {
                    Toast.makeText(this, "V₂ não pode ser zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultado = (c1 * v1) / v2;
                variavelCalculada = "C₂";
                tvRelacao.setText(String.format("C₂ = (C₁ × V₁) / V₂\nC₂ = (%.2f × %.2f) / %.2f", c1, v1, v2));
            } else if (TextUtils.isEmpty(v2Str)) {
                // Calcular V₂
                if (c2 == 0) {
                    Toast.makeText(this, "C₂ não pode ser zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultado = (c1 * v1) / c2;
                variavelCalculada = "V₂";
                tvRelacao.setText(String.format("V₂ = (C₁ × V₁) / C₂\nV₂ = (%.2f × %.2f) / %.2f", c1, v1, c2));
            }

            // Exibir resultado
            String unidade = variavelCalculada.startsWith("C") ? " mol/L" : " L";
            String resultadoFormatado = String.format("%.4f%s", resultado, unidade);
            tvResultado.setText(resultadoFormatado);

            Toast.makeText(this, variavelCalculada + " calculado com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }
}