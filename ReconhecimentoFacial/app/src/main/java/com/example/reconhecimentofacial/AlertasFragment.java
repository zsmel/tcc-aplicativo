package com.example.reconhecimentofacial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AlertasFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlertasAdapter adapter;
    private List<Alerta> listaAlertas;
    private ProgressBar progressBar;
    private TextView tvSemAlertas;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout (verifique se o nome do arquivo XML está correto)
        return inflater.inflate(R.layout.fragment_alertas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Vincula os componentes do layout
        recyclerView = view.findViewById(R.id.recyclerViewAlertas);
        progressBar = view.findViewById(R.id.pbAlertas);
        tvSemAlertas = view.findViewById(R.id.tvSemAlertas);

        // Inicializa Firebase e Listas
        db = FirebaseFirestore.getInstance();
        listaAlertas = new ArrayList<>();

        // Configura o Adapter
        adapter = new AlertasAdapter(getContext(), listaAlertas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Garante que a lista seja atualizada toda vez que você abrir essa aba
        buscarAlertas();
    }

    private void buscarAlertas() {
        progressBar.setVisibility(View.VISIBLE);
        tvSemAlertas.setVisibility(View.GONE);

        db.collection("alertas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    progressBar.setVisibility(View.GONE);
                    listaAlertas.clear(); // Limpa a lista antiga para não duplicar

                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            // Converte o documento do Firebase para o objeto Alerta
                            try {
                                Alerta alerta = doc.toObject(Alerta.class);
                                alerta.setId(doc.getId()); // Salva o ID do documento
                                listaAlertas.add(alerta);
                            } catch (Exception e) {
                                e.printStackTrace(); // Evita crash se um documento estiver mal formatado
                            }
                        }
                        adapter.notifyDataSetChanged(); // Avisa a lista que chegaram dados
                        recyclerView.setVisibility(View.VISIBLE);
                    } else {
                        // Se não tiver alertas
                        recyclerView.setVisibility(View.GONE);
                        tvSemAlertas.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    tvSemAlertas.setText("Erro ao carregar alertas.");
                    tvSemAlertas.setVisibility(View.VISIBLE);
                });
    }
}