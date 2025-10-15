package com.example.reconhecimentofacial;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConsultaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_consulta, container, false);

        int[] ids = new int[]{
                R.id.btnDetalhes,
                R.id.btnDetalhe02,
                R.id.btnDetalhes03,
                R.id.btnDetalhes04,
                R.id.btnDetalhes05,
                R.id.btnDetalhes06
        };

        View.OnClickListener go = view -> {
            Intent i = new Intent(requireContext(), CaseDetailActivity.class);

            startActivity(i);
        };

        for (int id : ids) {
            View b = v.findViewById(id);
            if (b instanceof Button) b.setOnClickListener(go);
        }

        return v;
    }
}

