package com.example.reconhecimentofacial;

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

import com.example.reconhecimentofacial.ui.ColumnChartView;

import java.util.Arrays;

public class AnaliseFragment extends Fragment {

    private ColumnChartView colRegiao, colSexo, colIdade;
    private Spinner spAno, spMes;
    private RadioGroup rgSituacao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_analise, container, false);

        colRegiao = v.findViewById(R.id.colRegiao);
        colSexo   = v.findViewById(R.id.colSexo);
        colIdade  = v.findViewById(R.id.colIdade);
        spAno     = v.findViewById(R.id.spAno);
        spMes     = v.findViewById(R.id.spMes);
        rgSituacao= v.findViewById(R.id.rgSituacao);

        spAno.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("2025", "2024", "2023")));

        spMes.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Todos", "Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                        "Jul", "Ago", "Set", "Out", "Nov", "Dez")));

        AdapterView.OnItemSelectedListener rerender = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { renderAll(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        spAno.setOnItemSelectedListener(rerender);
        spMes.setOnItemSelectedListener(rerender);
        rgSituacao.setOnCheckedChangeListener((g, id) -> renderAll());

        renderAll();
        return v;
    }

    private void renderAll() {
        float[] reg = new float[]{14, 22, 9, 26, 11};
        String[] regL = new String[]{"Norte", "Nordeste", "Centro-O.", "Sudeste", "Sul"};
        colRegiao.setData(reg, regL);

        float[] sx = new float[]{18, 24};
        String[] sxL = new String[]{"F", "M"};
        colSexo.setData(sx, sxL);

        float[] id = new float[]{6, 8, 12, 10, 5, 3};
        String[] idL = new String[]{"0-12", "13-17", "18-25", "26-40", "41-60", "60+"};
        colIdade.setData(id, idL);
    }
}
