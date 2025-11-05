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

public class CalculadoraVolumeGas extends AppCompatActivity {

    private EditText etMols;
    private TextView btnCalcular, tvResultado;
    private ImageView btnVoltar, btnMenu;
    private ImageButton btnTabela;

    // Constante para volume molar em CNTP (L/mol)
    private static final double VOLUME_MOLAR_CNTP = 22.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_volume_gas);
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
                calcularVolumeGas();
            }
        });
    }

    private void initViews() {
        etMols = findViewById(R.id.etMols);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnMenu = findViewById(R.id.btnMenu);
        btnTabela = findViewById(R.id.btnTabelaPeriodica);
    }

    private void calcularVolumeGas() {
        String molsStr = etMols.getText().toString();

        // Validar campo vazio
        if (TextUtils.isEmpty(molsStr)) {
            Toast.makeText(this, "Digite a quantidade de mols", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Converter valor para double
            double mols = Double.parseDouble(molsStr);

            // Validar valor positivo
            if (mols < 0) {
                Toast.makeText(this, "A quantidade de mols não pode ser negativa", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calcular volume: V = n × 22,4 L
            double volume = mols * VOLUME_MOLAR_CNTP;

            // Exibir resultado
            String resultado = String.format("%.2f L", volume);
            tvResultado.setText(resultado);

            Toast.makeText(this, "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite um valor numérico válido", Toast.LENGTH_SHORT).show();
        }
    }
}