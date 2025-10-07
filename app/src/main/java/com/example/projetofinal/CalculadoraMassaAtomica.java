package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
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
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculadoraMassaAtomica extends AppCompatActivity {
    ImageView btnMenu;
    private LinearLayout containerIsotopos;
    private EditText etQuantidade;
    private TextView btnGerarCampos, btnCalcular, tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_massa_atomica);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar views
        initViews();

        // Configurar botão voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> {
            finish();
        });

        ImageButton btnTabela = findViewById(R.id.btnTabelaPeriodica);

        btnTabela.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_tabela_periodica, null);
            builder.setView(view);

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        // Configurar menu
        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));

        // Configurar botão gerar campos
        btnGerarCampos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarCamposIsotopos();
            }
        });

        // Configurar botão calcular
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularMassaAtomica();
            }
        });
    }

    private void initViews() {
        containerIsotopos = findViewById(R.id.containerIsotopos);
        etQuantidade = findViewById(R.id.etQuantidade);
        btnGerarCampos = findViewById(R.id.btnGerarCampos);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
    }

    private void gerarCamposIsotopos() {
        String quantidadeStr = etQuantidade.getText().toString();

        if (TextUtils.isEmpty(quantidadeStr)) {
            Toast.makeText(this, "Digite a quantidade de isótopos", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantidade = Integer.parseInt(quantidadeStr);

        if (quantidade <= 0) {
            Toast.makeText(this, "A quantidade deve ser maior que zero", Toast.LENGTH_SHORT).show();
            return;
        }

        // Limpar campos existentes (exceto o primeiro que é de exemplo)
        containerIsotopos.removeAllViews();

        // Gerar novos campos
        for (int i = 0; i < quantidade; i++) {
            addCampoIsotopo(i + 1);
        }
    }

    private void addCampoIsotopo(int numero) {
        // Criar layout do isótopo
        LinearLayout layoutIsotopo = new LinearLayout(this);
        layoutIsotopo.setOrientation(LinearLayout.VERTICAL);
        layoutIsotopo.setBackgroundResource(R.drawable.bg_header);

        // Configurar layout params
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 12); // 12dp bottom margin
        layoutIsotopo.setLayoutParams(layoutParams);
        layoutIsotopo.setPadding(12, 12, 12, 12); // 12dp padding

        // Criar TextView do título
        TextView tvTitulo = new TextView(this);
        tvTitulo.setText("Isótopo " + numero + ":");
        tvTitulo.setTextColor(getResources().getColor(android.R.color.black));

        // CORREÇÃO: Usar setTypeface para definir o estilo em negrito
        tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);

        LinearLayout.LayoutParams tituloParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        tituloParams.setMargins(0, 0, 0, 8); // 8dp bottom margin
        tvTitulo.setLayoutParams(tituloParams);

        // Criar EditText para massa
        EditText etMassa = new EditText(this);
        etMassa.setHint("Massa (u)");
        etMassa.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etMassa.setBackgroundColor(getResources().getColor(android.R.color.white));
        etMassa.setTextColor(getResources().getColor(android.R.color.black));
        etMassa.setPadding(8, 8, 8, 8);
        LinearLayout.LayoutParams massaParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        massaParams.setMargins(0, 0, 0, 8); // 8dp bottom margin
        etMassa.setLayoutParams(massaParams);
        etMassa.setTag("massa_" + numero); // Tag para identificar

        // Criar EditText para abundância
        EditText etAbundancia = new EditText(this);
        etAbundancia.setHint("Abundância (%)");
        etAbundancia.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAbundancia.setBackgroundColor(getResources().getColor(android.R.color.white));
        etAbundancia.setTextColor(getResources().getColor(android.R.color.black));
        etAbundancia.setPadding(8, 8, 8, 8);
        LinearLayout.LayoutParams abundanciaParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        etAbundancia.setLayoutParams(abundanciaParams);
        etAbundancia.setTag("abundancia_" + numero); // Tag para identificar

        // Adicionar views ao layout do isótopo
        layoutIsotopo.addView(tvTitulo);
        layoutIsotopo.addView(etMassa);
        layoutIsotopo.addView(etAbundancia);

        // Adicionar layout do isótopo ao container
        containerIsotopos.addView(layoutIsotopo);
    }
    private void calcularMassaAtomica() {
        int childCount = containerIsotopos.getChildCount();

        if (childCount == 0) {
            Toast.makeText(this, "Gere os campos dos isótopos primeiro", Toast.LENGTH_SHORT).show();
            return;
        }

        double massaAtomicaTotal = 0.0;
        double somaAbundancia = 0.0;
        boolean dadosValidos = true;

        // Coletar dados de todos os isótopos
        for (int i = 0; i < childCount; i++) {
            View child = containerIsotopos.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout isotopoLayout = (LinearLayout) child;

                // Buscar os EditTexts dentro do layout
                EditText etMassa = null;
                EditText etAbundancia = null;

                for (int j = 0; j < isotopoLayout.getChildCount(); j++) {
                    View innerChild = isotopoLayout.getChildAt(j);
                    if (innerChild instanceof EditText) {
                        EditText et = (EditText) innerChild;
                        if (et.getHint().toString().contains("Massa")) {
                            etMassa = et;
                        } else if (et.getHint().toString().contains("Abundância")) {
                            etAbundancia = et;
                        }
                    }
                }

                if (etMassa != null && etAbundancia != null) {
                    String massaStr = etMassa.getText().toString();
                    String abundanciaStr = etAbundancia.getText().toString();

                    if (TextUtils.isEmpty(massaStr) || TextUtils.isEmpty(abundanciaStr)) {
                        Toast.makeText(this, "Preencha todos os campos do isótopo " + (i + 1),
                                Toast.LENGTH_SHORT).show();
                        dadosValidos = false;
                        break;
                    }

                    try {
                        double massa = Double.parseDouble(massaStr);
                        double abundancia = Double.parseDouble(abundanciaStr);

                        // Converter abundância de porcentagem para fração decimal
                        double abundanciaFracao = abundancia / 100.0;

                        massaAtomicaTotal += massa * abundanciaFracao;
                        somaAbundancia += abundancia;

                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Valores inválidos no isótopo " + (i + 1),
                                Toast.LENGTH_SHORT).show();
                        dadosValidos = false;
                        break;
                    }
                }
            }
        }

        if (dadosValidos) {
            // Verificar se a soma das abundâncias é aproximadamente 100%
            if (Math.abs(somaAbundancia - 100.0) > 0.1) {
                Toast.makeText(this,
                        "A soma das abundâncias deve ser 100%. Atual: " + String.format("%.2f", somaAbundancia) + "%",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Exibir resultado
            String resultado = String.format("%.1f u", massaAtomicaTotal);
            tvResultado.setText(resultado);

            Toast.makeText(this, "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }


}