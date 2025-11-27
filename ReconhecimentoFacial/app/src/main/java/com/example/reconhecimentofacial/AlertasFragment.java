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
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class AlertasFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlertasAdapter adapter;
    private List<Alerta> listaAlertas;
    private ProgressBar progressBar;
    private TextView tvSemAlertas;
    private FirebaseFirestore db;
    private ListenerRegistration listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alertas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewAlertas);
        progressBar = view.findViewById(R.id.pbAlertas);
        tvSemAlertas = view.findViewById(R.id.tvSemAlertas);

        db = FirebaseFirestore.getInstance();
        listaAlertas = new ArrayList<>();
        adapter = new AlertasAdapter(getContext(), listaAlertas);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        listenForAlerts();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listener != null) listener.remove(); // Stop listening to avoid leaks
    }

    private void listenForAlerts() {
        progressBar.setVisibility(View.VISIBLE);
        tvSemAlertas.setVisibility(View.GONE);

        listener = db.collection("alertas")
                .addSnapshotListener((snapshots, e) -> {
                    progressBar.setVisibility(View.GONE);
                    if (e != null) {
                        tvSemAlertas.setText("Erro ao carregar alertas.");
                        tvSemAlertas.setVisibility(View.VISIBLE);
                        return;
                    }

                    listaAlertas.clear();

                    if (snapshots != null && !snapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : snapshots) {
                            try {
                                Alerta alerta = doc.toObject(Alerta.class);
                                alerta.setId(doc.getId());
                                listaAlertas.add(alerta);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        tvSemAlertas.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvSemAlertas.setVisibility(View.VISIBLE);
                        tvSemAlertas.setText("Nenhum alerta encontrado.");
                    }
                });
    }
}
