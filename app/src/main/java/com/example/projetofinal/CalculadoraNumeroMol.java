package com.example.projetofinal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculadoraNumeroMol extends AppCompatActivity {

    private EditText etMassa, etMassaMolar;
    private TextView btnCalcular, tvResultado;
    private ImageView btnVoltar, btnMenu;
    private ImageButton btnTabela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_numero_mol);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar views
        initViews();

        // Configurar botão voltar
        btnVoltar.setOnClickListener(v -> finish());

        // Configurar menu
        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));

        // Configurar botão da tabela periódica
        btnTabela.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_tabela_periodica, null);
            builder.setView(view);

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // Configurar botão calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularNumeroMol();
            }
        });
    }

    private void initViews() {
        etMassa = findViewById(R.id.etMassa);
        etMassaMolar = findViewById(R.id.etMassaMolar);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnMenu = findViewById(R.id.btnMenu);
        btnTabela = findViewById(R.id.btnTabelaPeriodica);
    }

    private void calcularNumeroMol() {
        String massaStr = etMassa.getText().toString();
        String massaMolarStr = etMassaMolar.getText().toString();

        // Validar campos vazios
        if (TextUtils.isEmpty(massaStr) || TextUtils.isEmpty(massaMolarStr)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Converter valores para double
            double massa = Double.parseDouble(massaStr);
            double massaMolar = Double.parseDouble(massaMolarStr);

            // Validar massa molar diferente de zero
            if (massaMolar == 0) {
                Toast.makeText(this, "A massa molar não pode ser zero", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calcular número de mol: n = m / M
            double numeroMol = massa / massaMolar;

            // Exibir resultado
            String resultado = String.format("%.4f mol", numeroMol);
            tvResultado.setText(resultado);

            Toast.makeText(this, "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }
}