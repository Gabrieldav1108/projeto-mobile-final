package com.example.projetofinal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraEletrons extends AppCompatActivity {

    private EditText etNumeroAtomico;
    private TextView btnCalcular, tvDistribuicaoResumida, tvCamadaValencia, tvEletronsValencia;
    private LinearLayout tabelaDistribuicao;

    // Ordem de preenchimento dos subníveis (Diagrama de Linus Pauling)
    private final String[] SUB_NIVEIS = {"1s", "2s", "2p", "3s", "3p", "4s", "3d", "4p", "5s", "4d", "5p", "6s", "4f", "5d", "6p", "7s", "5f", "6d", "7p"};

    // Capacidade máxima de elétrons por subnível
    private final int[] CAPACIDADE_SUB_NIVEL = {2, 2, 6, 2, 6, 2, 10, 6, 2, 10, 6, 2, 14, 10, 6, 2, 14, 10, 6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculadora_eletrons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etNumeroAtomico = findViewById(R.id.etNumeroAtomico);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvDistribuicaoResumida = findViewById(R.id.tvDistribuicaoResumida);
        tvCamadaValencia = findViewById(R.id.tvCamadaValencia);
        tvEletronsValencia = findViewById(R.id.tvEletronsValencia);
        tabelaDistribuicao = findViewById(R.id.tabelaDistribuicao);

        // Configurar botão voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        // Configurar menu
        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> MenuHelper.showPopupMenu(this, v));
    }

    private void setupClickListeners() {
        btnCalcular.setOnClickListener(v -> calcularEletronsValencia());
    }

    private void calcularEletronsValencia() {
        String zStr = etNumeroAtomico.getText().toString();

        // Validar campo vazio
        if (TextUtils.isEmpty(zStr)) {
            Toast.makeText(this, "Digite o número atômico", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int numeroAtomico = Integer.parseInt(zStr);

            // Validar número atômico
            if (numeroAtomico < 1 || numeroAtomico > 118) {
                Toast.makeText(this, "Número atômico deve estar entre 1 e 118", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calcular distribuição eletrônica
            ResultadoDistribuicao resultado = calcularDistribuicaoEletronica(numeroAtomico);

            // Exibir resultados
            tvDistribuicaoResumida.setText(resultado.distribuicaoResumida);
            tvCamadaValencia.setText("Camada " + resultado.camadaValencia);
            tvEletronsValencia.setText(resultado.eletronsValencia + " elétrons de valência");

            // Preencher tabela detalhada
            preencherTabelaDistribuicao(resultado.passosDistribuicao);

            Toast.makeText(this, "Cálculo realizado com sucesso!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Digite um número válido", Toast.LENGTH_SHORT).show();
        }
    }

    private ResultadoDistribuicao calcularDistribuicaoEletronica(int numeroAtomico) {
        StringBuilder distribuicaoResumida = new StringBuilder();
        int eletronsRestantes = numeroAtomico;
        int camadaValencia = 0;
        int eletronsValencia = 0;

        List<PassoDistribuicao> passos = new ArrayList<>();

        // Distribuir elétrons nos subníveis
        for (int i = 0; i < SUB_NIVEIS.length && eletronsRestantes > 0; i++) {
            String subNivel = SUB_NIVEIS[i];
            int capacidade = CAPACIDADE_SUB_NIVEL[i];
            int eletronsNoSubnivel = Math.min(eletronsRestantes, capacidade);

            // Adicionar à distribuição resumida
            if (distribuicaoResumida.length() > 0) {
                distribuicaoResumida.append(" ");
            }
            distribuicaoResumida.append(subNivel).append("²".replace("²", String.valueOf(eletronsNoSubnivel)));

            // Calcular número da camada
            int numeroCamada = Character.getNumericValue(subNivel.charAt(0));

            // Atualizar camada de valência
            if (numeroCamada > camadaValencia) {
                camadaValencia = numeroCamada;
                eletronsValencia = 0;
            }

            // Se está na última camada, somar elétrons de valência
            if (numeroCamada == camadaValencia) {
                eletronsValencia += eletronsNoSubnivel;
            }

            // Adicionar passo à lista
            passos.add(new PassoDistribuicao(
                    i + 1,
                    subNivel,
                    capacidade,
                    eletronsNoSubnivel,
                    eletronsRestantes - eletronsNoSubnivel
            ));

            eletronsRestantes -= eletronsNoSubnivel;
        }

        return new ResultadoDistribuicao(
                distribuicaoResumida.toString(),
                camadaValencia,
                eletronsValencia,
                passos
        );
    }

    private void preencherTabelaDistribuicao(List<PassoDistribuicao> passos) {
        tabelaDistribuicao.removeAllViews();

        for (PassoDistribuicao passo : passos) {
            View linhaTabela = LayoutInflater.from(this).inflate(R.layout.linha_tabela_distribuicao, null);

            TextView tvOrdem = linhaTabela.findViewById(R.id.tvOrdem);
            TextView tvSubnivel = linhaTabela.findViewById(R.id.tvSubnivel);
            TextView tvCapacidade = linhaTabela.findViewById(R.id.tvCapacidade);
            TextView tvPreenchido = linhaTabela.findViewById(R.id.tvPreenchido);

            tvOrdem.setText(String.valueOf(passo.ordem));
            tvSubnivel.setText(passo.subnivel);
            tvCapacidade.setText(String.valueOf(passo.capacidade));
            tvPreenchido.setText(passo.eletronsPreenchidos + " e⁻ (restam: " + passo.eletronsRestantes + ")");

            tabelaDistribuicao.addView(linhaTabela);
        }
    }

    // Classes para armazenar os resultados
    private static class ResultadoDistribuicao {
        String distribuicaoResumida;
        int camadaValencia;
        int eletronsValencia;
        List<PassoDistribuicao> passosDistribuicao;

        ResultadoDistribuicao(String distribuicaoResumida, int camadaValencia,
                              int eletronsValencia, List<PassoDistribuicao> passosDistribuicao) {
            this.distribuicaoResumida = distribuicaoResumida;
            this.camadaValencia = camadaValencia;
            this.eletronsValencia = eletronsValencia;
            this.passosDistribuicao = passosDistribuicao;
        }
    }

    private static class PassoDistribuicao {
        int ordem;
        String subnivel;
        int capacidade;
        int eletronsPreenchidos;
        int eletronsRestantes;

        PassoDistribuicao(int ordem, String subnivel, int capacidade,
                          int eletronsPreenchidos, int eletronsRestantes) {
            this.ordem = ordem;
            this.subnivel = subnivel;
            this.capacidade = capacidade;
            this.eletronsPreenchidos = eletronsPreenchidos;
            this.eletronsRestantes = eletronsRestantes;
        }
    }
}