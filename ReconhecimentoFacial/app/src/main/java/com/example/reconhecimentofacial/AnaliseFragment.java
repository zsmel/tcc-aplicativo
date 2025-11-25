package com.example.reconhecimentofacial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.reconhecimentofacial.ui.ColumnChartView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnaliseFragment extends Fragment {

    private ColumnChartView colRegiao, colSexo, colIdade;
    private Spinner spAno, spMes;
    private RadioGroup rgSituacao;
    private TextView tvTotalSelecionado, tvTituloRegiao, tvTituloSexo, tvTituloIdade, tvLinkSite;
    private ImageButton btnAtualizar;
    private DecimalFormat decimalFormat;
    private int[] chartColors;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analise, container, false);

        db = FirebaseFirestore.getInstance();
        decimalFormat = new DecimalFormat("#,###");

        chartColors = new int[]{
                ContextCompat.getColor(requireContext(), R.color.chart_bar_1),
                ContextCompat.getColor(requireContext(), R.color.chart_bar_2),
                ContextCompat.getColor(requireContext(), R.color.chart_bar_3),
                ContextCompat.getColor(requireContext(), R.color.chart_bar_4),
                ContextCompat.getColor(requireContext(), R.color.chart_bar_5)
        };

        colRegiao = v.findViewById(R.id.colRegiao);
        colSexo   = v.findViewById(R.id.colSexo);
        colIdade  = v.findViewById(R.id.colIdade);
        spAno     = v.findViewById(R.id.spAno);
        spMes     = v.findViewById(R.id.spMes);
        rgSituacao= v.findViewById(R.id.rgSituacao);
        btnAtualizar = v.findViewById(R.id.btnAtualizarAnalise);

        tvTotalSelecionado = v.findViewById(R.id.tvTotalSelecionado);
        tvTituloRegiao = v.findViewById(R.id.tvTituloRegiao);
        tvTituloSexo = v.findViewById(R.id.tvTituloSexo);
        tvTituloIdade = v.findViewById(R.id.tvTituloIdade);
        tvLinkSite = v.findViewById(R.id.tvLinkSite);

        spAno.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("2025", "2024", "2023", "2022")));

        spMes.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Todos", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")));

        AdapterView.OnItemSelectedListener rerender = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { renderAll(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        spAno.setOnItemSelectedListener(rerender);
        spMes.setOnItemSelectedListener(rerender);

        rgSituacao.setOnCheckedChangeListener((g, id) -> renderAll());

        tvLinkSite.setOnClickListener(view -> {
            String url = "https://app.powerbi.com/view?r=eyJrIjoiNWQ0NTdlY2UtMTI2NC00MzQ0LWI3MTQtMmYxNmY5NTZlN2VlIiwidCI6ImViMDkwNDIwLTQ0NGMtNDNmNy05MWYyLTRiOGRhNmJmZThlMSJ9";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        btnAtualizar.setOnClickListener(view -> {
            Toast.makeText(getContext(), "Atualizando gráficos...", Toast.LENGTH_SHORT).show();
            renderAll();
        });

        renderAll();
        return v;
    }

    private void renderAll() {
        int selectedId = rgSituacao.getCheckedRadioButtonId();

        if (selectedId == R.id.rbCadastrados) {
            // MODO ONLINE: Busca do Firebase
            carregarDadosDoFirebase();
            // Desabilita spinners pois vamos mostrar o total geral do banco
            spAno.setEnabled(false);
            spMes.setEnabled(false);
        } else {
            // MODO OFFLINE: Dados Estáticos do SINESP
            spAno.setEnabled(true);
            spMes.setEnabled(true);
            carregarDadosEstaticos(selectedId);
        }
    }

    private void carregarDadosDoFirebase() {
        tvTotalSelecionado.setText("Carregando dados do App...");

        db.collection("desaparecidos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            int total = 0;

            // Mapas para contagem
            Map<String, Integer> contSexo = new HashMap<>();
            contSexo.put("Masculino", 0); contSexo.put("Feminino", 0); contSexo.put("Não Informado", 0);

            Map<String, Integer> contRegiao = new HashMap<>();
            contRegiao.put("Norte", 0); contRegiao.put("Nordeste", 0); contRegiao.put("Centro-O.", 0);
            contRegiao.put("Sudeste", 0); contRegiao.put("Sul", 0);

            Map<String, Integer> contIdade = new HashMap<>();
            contIdade.put("0 a 17 anos", 0); contIdade.put("+18 anos", 0);

            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                total++;
                Caso caso = doc.toObject(Caso.class);

                // 1. Contagem Sexo
                String sx = caso.getSexo();
                if(sx == null) sx = "Não Informado";
                if(sx.toLowerCase().contains("masc")) contSexo.put("Masculino", contSexo.get("Masculino")+1);
                else if(sx.toLowerCase().contains("fem")) contSexo.put("Feminino", contSexo.get("Feminino")+1);
                else contSexo.put("Não Informado", contSexo.get("Não Informado")+1);

                // 2. Contagem Região (Baseada no Estado)
                String uf = caso.getEstado_desaparecimento();
                if(uf == null) uf = "";
                if(Arrays.asList("SP", "RJ", "MG", "ES").contains(uf)) contRegiao.put("Sudeste", contRegiao.get("Sudeste")+1);
                else if(Arrays.asList("PR", "SC", "RS").contains(uf)) contRegiao.put("Sul", contRegiao.get("Sul")+1);
                else if(Arrays.asList("DF", "GO", "MT", "MS").contains(uf)) contRegiao.put("Centro-O.", contRegiao.get("Centro-O.")+1);
                else if(Arrays.asList("BA", "SE", "AL", "PE", "PB", "RN", "CE", "PI", "MA").contains(uf)) contRegiao.put("Nordeste", contRegiao.get("Nordeste")+1);
                else contRegiao.put("Norte", contRegiao.get("Norte")+1);

                // 3. Contagem Idade
                int idade = 0;
                try {
                    String idadeStr = caso.getIdadeEpoca();
                    if (idadeStr != null && !idadeStr.isEmpty()) {
                        // Se houver espaços, remove. Ex: "25 " -> "25"
                        idade = Integer.parseInt(idadeStr.trim());
                    }
                } catch (NumberFormatException e) {
                    idade = 0; // Se não for número, conta como 0
                }

                if(idade <= 17) contIdade.put("0 a 17 anos", contIdade.get("0 a 17 anos")+1);
                else contIdade.put("+18 anos", contIdade.get("+18 anos")+1);
            }

            // Atualiza UI
            tvTotalSelecionado.setText("Total Cadastrados no App: " + total);

            colSexo.setData(new float[]{contSexo.get("Masculino"), contSexo.get("Feminino"), contSexo.get("Não Informado")},
                    new String[]{"Masc", "Fem", "N/I"},
                    new int[]{chartColors[0], chartColors[1], chartColors[3]});

            colRegiao.setData(new float[]{contRegiao.get("Norte"), contRegiao.get("Nordeste"), contRegiao.get("Centro-O."), contRegiao.get("Sudeste"), contRegiao.get("Sul")},
                    new String[]{"Norte", "Nordeste", "Centro", "Sudeste", "Sul"}, chartColors);

            colIdade.setData(new float[]{contIdade.get("0 a 17 anos"), contIdade.get("+18 anos")},
                    new String[]{"0-17", "+18"},
                    new int[]{chartColors[2], chartColors[4]});

        }).addOnFailureListener(e -> {
            tvTotalSelecionado.setText("Erro ao carregar dados.");
        });
    }

    private void carregarDadosEstaticos(int selectedSituacaoId) {
        String anoSelecionado = (String) spAno.getSelectedItem();
        if (anoSelecionado == null) anoSelecionado = "2025";

        EstatisticasAnuais dadosDoAno = null;
        String situacaoTexto;

        if (selectedSituacaoId == R.id.rbDesaparecidos) {
            situacaoTexto = "Desaparecidas (SINESP)";
            switch (anoSelecionado) {
                case "2022": dadosDoAno = DadosEstatisticos.dados2022; break;
                case "2023": dadosDoAno = DadosEstatisticos.dados2023; break;
                case "2024": dadosDoAno = DadosEstatisticos.dados2024; break;
                case "2025": dadosDoAno = DadosEstatisticos.dados2025; break;
                default: dadosDoAno = DadosEstatisticos.dados2025; break;
            }
        } else {
            situacaoTexto = "Localizadas (SINESP)";
            switch (anoSelecionado) {
                case "2022": dadosDoAno = DadosEstatisticos.dadosLocalizados2022; break;
                case "2023": dadosDoAno = DadosEstatisticos.dadosLocalizados2023; break;
                case "2024": dadosDoAno = DadosEstatisticos.dadosLocalizados2024; break;
                case "2025": dadosDoAno = DadosEstatisticos.dadosLocalizados2025; break;
                default: dadosDoAno = DadosEstatisticos.dadosLocalizados2025; break;
            }
        }

        if (dadosDoAno != null) {
            tvTotalSelecionado.setText("Total " + situacaoTexto + " (" + anoSelecionado + "): " + decimalFormat.format(dadosDoAno.getTotal()));

            // Renderiza gráficos estáticos
            Map<String, Integer> dadosRegiao = dadosDoAno.getPorRegiao();
            colRegiao.setData(new float[]{dadosRegiao.get("Norte"), dadosRegiao.get("Nordeste"), dadosRegiao.get("Centro-O."), dadosRegiao.get("Sudeste"), dadosRegiao.get("Sul")},
                    new String[]{"Norte", "Nordeste", "Centro", "Sudeste", "Sul"}, chartColors);

            Map<String, Integer> dadosSexo = dadosDoAno.getPorSexo();
            colSexo.setData(new float[]{dadosSexo.get("Masculino"), dadosSexo.get("Feminino"), dadosSexo.get("Não Informado")},
                    new String[]{"Masc", "Fem", "N/I"}, new int[]{chartColors[0], chartColors[1], chartColors[3]});

            Map<String, Integer> dadosIdade = dadosDoAno.getPorFaixaEtaria();
            colIdade.setData(new float[]{dadosIdade.get("0 a 17 anos"), dadosIdade.get("+18 anos")},
                    new String[]{"0-17", "+18"}, new int[]{chartColors[2], chartColors[4]});
        }
    }
}