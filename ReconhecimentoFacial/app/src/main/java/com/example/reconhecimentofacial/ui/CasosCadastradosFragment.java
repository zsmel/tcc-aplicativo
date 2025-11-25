package com.example.reconhecimentofacial.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.reconhecimentofacial.CaseDetailActivity;
import com.example.reconhecimentofacial.Caso;
import com.example.reconhecimentofacial.CasosAdapter;
import com.example.reconhecimentofacial.FormCadastroDesaparecido;
import com.example.reconhecimentofacial.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CasosCadastradosFragment extends Fragment {

    private RecyclerView recyclerView;
    private CasosAdapter adapter;
    private List<Caso> listaCasosVisiveis;
    private List<Caso> listaCompletaCasos;
    private FirebaseFirestore db;
    private TextView textViewListaVazia;
    private ProgressBar progressBar;
    private EditText editTextSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_casos_cadastrados, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewCasos);
        textViewListaVazia = view.findViewById(R.id.textViewListaVazia);
        progressBar = view.findViewById(R.id.progressBar);
        editTextSearch = view.findViewById(R.id.editTextSearch);

        // Ação do Botão Novo
        view.findViewById(R.id.btnCadastrarNovo).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FormCadastroDesaparecido.class);
            startActivity(intent);
        });

        db = FirebaseFirestore.getInstance();
        listaCasosVisiveis = new ArrayList<>();
        listaCompletaCasos = new ArrayList<>();

        adapter = new CasosAdapter(getContext(), listaCasosVisiveis);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnDetalhesClickListener(position -> {
            Caso casoSelecionado = listaCasosVisiveis.get(position);
            abrirDetalhesDoCaso(casoSelecionado.getId());
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                filtrarLista(s.toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        buscarCasosDoFirestore();
    }

    private void filtrarLista(String texto) {
        List<Caso> listaFiltrada = new ArrayList<>();
        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaCompletaCasos);
        } else {
            for (Caso caso : listaCompletaCasos) {
                if (caso.getNomeCompleto() != null && caso.getNomeCompleto().toLowerCase().contains(texto.toLowerCase())) {
                    listaFiltrada.add(caso);
                }
            }
        }

        if (listaFiltrada.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewListaVazia.setText("Nenhum desaparecido com esse nome encontrado.");
            textViewListaVazia.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewListaVazia.setVisibility(View.GONE);
        }
        adapter.updateList(listaFiltrada);
    }

    private void buscarCasosDoFirestore() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("desaparecidos").get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful() && task.getResult() != null) {
                listaCompletaCasos.clear();
                listaCasosVisiveis.clear();

                if (task.getResult().isEmpty()) {
                    textViewListaVazia.setText("Não há nenhum desaparecido cadastrado.");
                    textViewListaVazia.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Caso caso = document.toObject(Caso.class);
                        caso.setId(document.getId());
                        listaCompletaCasos.add(caso);
                    }
                    listaCasosVisiveis.addAll(listaCompletaCasos);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewListaVazia.setVisibility(View.GONE);
                }
            } else {
                textViewListaVazia.setText("Erro ao carregar dados.");
                textViewListaVazia.setVisibility(View.VISIBLE);
                Log.w("Firestore", "Erro ao buscar documentos.", task.getException());
            }
        });
    }

    private void abrirDetalhesDoCaso(String casoId) {
        if (getActivity() == null) return;
        Intent intent = new Intent(getActivity(), CaseDetailActivity.class);
        intent.putExtra("CASO_ID", casoId);
        startActivity(intent);
    }
}