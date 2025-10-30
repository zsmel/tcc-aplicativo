package com.example.reconhecimentofacial;

// ... (seus imports existentes)
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import java.text.DecimalFormat;
// ...

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import com.example.reconhecimentofacial.ui.ColumnChartView;

import java.util.Arrays;
import java.util.Map;

public class AnaliseFragment extends Fragment {

    private ColumnChartView colRegiao, colSexo, colIdade;
    private Spinner spAno, spMes;
    private RadioGroup rgSituacao;
    private TextView tvTotalSelecionado, tvTituloRegiao, tvTituloSexo, tvTituloIdade, tvLinkSite;
    private DecimalFormat decimalFormat;
    private int[] chartColors;

    private final String[] MESES_MAP_KEY = {
            null, "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analise, container, false);

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

        tvTotalSelecionado = v.findViewById(R.id.tvTotalSelecionado);
        tvTituloRegiao = v.findViewById(R.id.tvTituloRegiao);
        tvTituloSexo = v.findViewById(R.id.tvTituloSexo);
        tvTituloIdade = v.findViewById(R.id.tvTituloIdade);
        tvLinkSite = v.findViewById(R.id.tvLinkSite);

        spAno.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("2025", "2024", "2023", "2022", "2021", "2020")));

        spMes.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Todos", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                        "Jul", "Ago", "Set", "Out", "Nov", "Dezembro")));

        AdapterView.OnItemSelectedListener rerender = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { renderAll(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        spAno.setOnItemSelectedListener(rerender);
        spMes.setOnItemSelectedListener(rerender);

        rgSituacao.setOnCheckedChangeListener((g, id) -> renderAll());

        // Listener do Link: URL para os "Dados Originais"
        tvLinkSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Manter a URL do Power BI, que é onde estariam os "Dados Originais" detalhados.
                String url = "https://app.powerbi.com/view?r=eyJrIjoiNWQ0NTdlY2UtMTI2NC00MzQ0LWI3MTQtMmYxNmY5NTZlN2VlIiwidCI6ImViMDkwNDIwLTQ0NGMtNDNmNy05MWYyLTRiOGRhNmJmZThlMSJ9";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        renderAll();
        return v;
    }

    private void renderAll() {
        String anoSelecionado = (String) spAno.getSelectedItem();
        if (anoSelecionado == null) anoSelecionado = "2025";

        int selectedSituacaoId = rgSituacao.getCheckedRadioButtonId();

        EstatisticasAnuais dadosDoAno = null;
        String situacaoTexto;

        if (selectedSituacaoId == R.id.rbDesaparecidos) {
            situacaoTexto = "Desaparecidas";
            switch (anoSelecionado) {
                case "2020": dadosDoAno = DadosEstatisticos.dados2020; break;
                case "2021": dadosDoAno = DadosEstatisticos.dados2021; break;
                case "2022": dadosDoAno = DadosEstatisticos.dados2022; break;
                case "2023": dadosDoAno = DadosEstatisticos.dados2023; break;
                case "2024": dadosDoAno = DadosEstatisticos.dados2024; break;
                case "2025": dadosDoAno = DadosEstatisticos.dados2025; break;
            }
        } else if (selectedSituacaoId == R.id.rbLocalizados) {
            situacaoTexto = "Localizadas";
            switch (anoSelecionado) {
                case "2020": dadosDoAno = DadosEstatisticos.dadosLocalizados2020; break;
                case "2021": dadosDoAno = DadosEstatisticos.dadosLocalizados2021; break;
                case "2022": dadosDoAno = DadosEstatisticos.dadosLocalizados2022; break;
                case "2023": dadosDoAno = DadosEstatisticos.dadosLocalizados2023; break;
                case "2024": dadosDoAno = DadosEstatisticos.dadosLocalizados2024; break;
                case "2025": dadosDoAno = DadosEstatisticos.dadosLocalizados2025; break;
            }
        } else {
            situacaoTexto = "Desaparecidas";
            dadosDoAno = DadosEstatisticos.dados2025;
        }

        if (dadosDoAno == null) {
            colRegiao.setData(new float[]{}, new String[]{});
            colSexo.setData(new float[]{}, new String[]{});
            colIdade.setData(new float[]{}, new String[]{});
            tvTotalSelecionado.setText("Dados indisponíveis");
            tvTituloRegiao.setText("Por Região");
            tvTituloSexo.setText("Por Sexo");
            tvTituloIdade.setText("Por Faixa Etária");
            return;
        }

        String totalFormatado = decimalFormat.format(dadosDoAno.getTotal());
        tvTotalSelecionado.setText("Total de Pessoas " + situacaoTexto + " (" + anoSelecionado + "): " + totalFormatado);

        tvTituloRegiao.setText(situacaoTexto + " por Região");
        tvTituloSexo.setText(situacaoTexto + " por Sexo");
        tvTituloIdade.setText(situacaoTexto + " por Faixa Etária");

        Map<String, Integer> dadosRegiao = dadosDoAno.getPorRegiao();
        float[] regValues = new float[]{
                dadosRegiao.get("Norte"),
                dadosRegiao.get("Nordeste"),
                dadosRegiao.get("Centro-O."),
                dadosRegiao.get("Sudeste"),
                dadosRegiao.get("Sul")
        };
        String[] regLabels = new String[]{"Norte", "Nordeste", "Centro-O.", "Sudeste", "Sul"};
        colRegiao.setData(regValues, regLabels, chartColors);

        Map<String, Integer> dadosSexo = dadosDoAno.getPorSexo();
        float[] sxValues = new float[]{
                dadosSexo.get("Masculino"),
                dadosSexo.get("Feminino"),
                dadosSexo.get("Não Informado")
        };
        String[] sxLabels = new String[]{"Masculino", "Feminino", "N/I"};
        int[] sexoColors = new int[]{chartColors[0], chartColors[1], chartColors[3]};
        colSexo.setData(sxValues, sxLabels, sexoColors);

        Map<String, Integer> dadosIdade = dadosDoAno.getPorFaixaEtaria();
        float[] idValues = new float[]{
                dadosIdade.get("0 a 17 anos"),
                dadosIdade.get("+18 anos"),
                dadosIdade.get("N/I")
        };
        String[] idLabels = new String[]{"0-17 anos", "+18 anos", "N/I"};
        int[] idadeColors = new int[]{chartColors[2], chartColors[4], chartColors[0]};
        colIdade.setData(idValues, idLabels, idadeColors);
    }
}