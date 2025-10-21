package com.example.projetofinal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculadoraMassaMolar extends AppCompatActivity {

    private LinearLayout containerElementos;
    private TextView btnCalcular, tvResultado, btnAdicionarCampo;
    private ImageView btnMenu;
    private Double massaAtomicaInicial = null;
    private boolean isPrimeiroElemento = true; // Nova variável para controlar o primeiro elemento

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_massa_molar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        containerElementos = findViewById(R.id.containerElementos);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
        btnAdicionarCampo = findViewById(R.id.btnAdicionarCampo);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));

        ImageButton btnTabela = findViewById(R.id.btnTabelaPeriodica);

        btnTabela.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_tabela_periodica, null);
            builder.setView(view);

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // CORREÇÃO: Verificar e obter a massa atômica recebida
        Double massaAtomicaRecebida = null;
        if (getIntent().hasExtra("massa_atomica")) {
            massaAtomicaRecebida = getIntent().getDoubleExtra("massa_atomica", 0.0);
            if (massaAtomicaRecebida > 0) {
                Toast.makeText(this, "Massa atômica recebida: " + massaAtomicaRecebida + " u", Toast.LENGTH_SHORT).show();
            }
        }

        // adiciona o primeiro campo, passando a massa atômica se existir
        adicionarElemento(massaAtomicaRecebida);

        // ação do botão Adicionar Campo
        btnAdicionarCampo.setOnClickListener(v -> adicionarElemento(null));

        // ação do botão Calcular
        btnCalcular.setOnClickListener(v -> calcularMassaMolar());
    }

    private void adicionarElemento(Double massaAtomica) {
        LinearLayout layoutElemento = new LinearLayout(this);
        layoutElemento.setOrientation(LinearLayout.HORIZONTAL);
        layoutElemento.setBackgroundResource(R.drawable.bg_header);
        layoutElemento.setPadding(8, 8, 8, 8);
        layoutElemento.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) layoutElemento.getLayoutParams()).setMargins(0, 0, 0, 12);

        // campo quantidade
        EditText etQuantidade = new EditText(this);
        etQuantidade.setHint("Quantidade (n)");
        etQuantidade.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etQuantidade.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // campo massa atômica (pode vir preenchido)
        EditText etMassa = new EditText(this);
        etMassa.setHint("Massa atômica (u)");
        etMassa.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMassa.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // CORREÇÃO: Preencher com a massa atômica recebida, se existir
        if (massaAtomica != null && massaAtomica > 0) {
            etMassa.setText(String.valueOf(massaAtomica));
        }

        layoutElemento.addView(etQuantidade);
        layoutElemento.addView(etMassa);

        // Só adiciona o botão X se NÃO for o primeiro elemento
        if (!isPrimeiroElemento) {
            // botão x (remover) - apenas para elementos adicionais
            ImageButton btnRemove = new ImageButton(this);
            btnRemove.setImageResource(android.R.drawable.ic_delete);
            btnRemove.setBackgroundColor(0x00000000);
            btnRemove.setOnClickListener(v -> {
                if (containerElementos.getChildCount() > 1) {
                    containerElementos.removeView(layoutElemento);
                }
            });
            layoutElemento.addView(btnRemove);
        }

        containerElementos.addView(layoutElemento);

        // Marca que o primeiro elemento já foi criado
        isPrimeiroElemento = false;
    }

    private void calcularMassaMolar() {
        int count = containerElementos.getChildCount();
        double massaMolarTotal = 0.0;
        boolean valido = true;

        for (int i = 0; i < count; i++) {
            LinearLayout linha = (LinearLayout) containerElementos.getChildAt(i);

            // Ajuste: o primeiro elemento tem 2 views, os demais têm 3
            EditText etQuantidade = (EditText) linha.getChildAt(0);
            EditText etMassa = (EditText) linha.getChildAt(1);

            String qStr = etQuantidade.getText().toString();
            String mStr = etMassa.getText().toString();

            if (TextUtils.isEmpty(qStr) || TextUtils.isEmpty(mStr)) {
                Toast.makeText(this, "Preencha todos os campos do elemento " + (i + 1), Toast.LENGTH_SHORT).show();
                valido = false;
                break;
            }

            try {
                double n = Double.parseDouble(qStr);
                double m = Double.parseDouble(mStr);
                massaMolarTotal += n * m;
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor inválido no elemento " + (i + 1), Toast.LENGTH_SHORT).show();
                valido = false;
                break;
            }
        }

        if (valido) {
            tvResultado.setText(String.format("%.2f g/mol", massaMolarTotal));
        }
    }
}