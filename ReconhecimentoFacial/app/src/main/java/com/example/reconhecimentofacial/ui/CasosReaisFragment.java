package com.example.reconhecimentofacial.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconhecimentofacial.CasoReal;
import com.example.reconhecimentofacial.CasosReaisAdapter;
import com.example.reconhecimentofacial.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CasosReaisFragment extends Fragment {

    private RecyclerView recyclerView;
    private CasosReaisAdapter adapter;
    private List<CasoReal> listaCasosReais;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_casos_reais, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewCasosReais);
        db = FirebaseFirestore.getInstance();
        listaCasosReais = new ArrayList<>();
        adapter = new CasosReaisAdapter(getContext(), listaCasosReais);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Configura o clique no botão "Ver Fonte"
        adapter.setOnFonteClickListener(position -> {
            String url = listaCasosReais.get(position).getFonteUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        buscarCasosReais();
    }

    private void buscarCasosReais() {
        db.collection("casos_reais").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                listaCasosReais.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    listaCasosReais.add(document.toObject(CasoReal.class));
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}