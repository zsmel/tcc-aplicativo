package com.example.reconhecimentofacial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog; // Importante para o pop-up
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CaseDetailActivity extends AppCompatActivity {

    private ImageView imgFoto;
    private TextView tvNome, tvStatus;

    // Referências para as seções (Includes)
    private View secaoDadosPessoais, secaoDadosOcorrencia, secaoDadosComunicante, secaoAlertasEmitidos;

    private View progressBar;
    private View contentLayout;
    private Button btnVoltar, btnEditar, btnExcluir; // Adicionado btnExcluir

    private FirebaseFirestore db;
    private String casoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = FirebaseFirestore.getInstance();

        initViews();

        casoId = getIntent().getStringExtra("CASO_ID");

        if (casoId == null) {
            Toast.makeText(this, "Erro: ID do caso não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Ações dos Botões
        btnVoltar.setOnClickListener(v -> finish());

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(CaseDetailActivity.this, EditCaseActivity.class);
            intent.putExtra("CASO_ID", casoId);
            startActivity(intent);
        });

        // Ação do Botão Excluir
        btnExcluir.setOnClickListener(v -> confirmarExclusao());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (casoId != null) {
            carregarDadosDoCaso(casoId);
        }
    }

    private void initViews() {
        imgFoto = findViewById(R.id.imgFoto);
        tvNome = findViewById(R.id.tvNome);
        tvStatus = findViewById(R.id.tvStatus);

        secaoDadosPessoais = findViewById(R.id.secDadosPessoais);
        secaoDadosOcorrencia = findViewById(R.id.secDadosOcorrencia);
        secaoDadosComunicante = findViewById(R.id.secDadosComunicante);
        secaoAlertasEmitidos = findViewById(R.id.secAlertasEmitidos);

        progressBar = findViewById(R.id.progressBarDetail);
        contentLayout = findViewById(R.id.contentLayout);

        btnVoltar = findViewById(R.id.btnVoltar);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir); // Vincula o botão
    }

    private void confirmarExclusao() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Registro")
                .setMessage("Tem certeza que deseja excluir este desaparecido? Essa ação não pode ser desfeita.")
                .setPositiveButton("Sim, Excluir", (dialog, which) -> deletarDoFirebase())
                .setNegativeButton("Cancelar", null) // Fecha o diálogo sem fazer nada
                .show();
    }

    private void deletarDoFirebase() {
        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE); // Esconde os dados enquanto deleta

        db.collection("desaparecidos").document(casoId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Registro excluído com sucesso.", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a tela e volta para a lista
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    contentLayout.setVisibility(View.VISIBLE); // Mostra os dados de volta se der erro
                    Toast.makeText(this, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void carregarDadosDoCaso(String casoId) {
        db.collection("desaparecidos").document(casoId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    if (contentLayout != null) contentLayout.setVisibility(View.VISIBLE);

                    if (documentSnapshot.exists()) {
                        Caso caso = documentSnapshot.toObject(Caso.class);
                        if (caso != null) {
                            preencherCabecalho(caso);
                            popularSecaoDadosPessoais(caso);
                            popularSecaoDadosOcorrencia(caso);
                            popularSecaoComunicante(caso);
                            popularSecaoAlertas(caso);
                        }
                    } else {
                        Toast.makeText(this, "Caso não encontrado (pode ter sido excluído).", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Erro ao carregar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void preencherCabecalho(Caso caso) {
        tvNome.setText(caso.getNomeCompleto());
        tvStatus.setText(caso.getEstado_desaparecimento());

        List<String> fotos = caso.getFotos();
        if (fotos != null && !fotos.isEmpty()) {
            String urlFoto = fotos.get(0);
            Glide.with(this)
                    .load(urlFoto)
                    .placeholder(R.drawable.ic_placeholder_person)
                    .error(R.drawable.ic_placeholder_person)
                    .circleCrop()
                    .into(imgFoto);
        } else {
            imgFoto.setImageResource(R.drawable.ic_placeholder_person);
        }
    }

    // --- MÉTODOS DE POPULAR SEÇÕES ---

    private void popularSecaoDadosPessoais(Caso caso) {
        TextView titulo = secaoDadosPessoais.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoDadosPessoais.findViewById(R.id.container);
        View header = secaoDadosPessoais.findViewById(R.id.header);

        titulo.setText("Dados Pessoais");
        container.removeAllViews();

        adicionarCampo(container, "Nome Completo", caso.getNomeCompleto());
        adicionarCampo(container, "Data Nascimento", caso.getDataNascimento());
        adicionarCampo(container, "Idade à época", String.valueOf(caso.getIdadeEpoca()));
        adicionarCampo(container, "CPF", caso.getCpf());
        adicionarCampo(container, "RG", caso.getRg());
        adicionarCampo(container, "Sexo", caso.getSexo());

        adicionarSubtitulo(container, "Físico");
        adicionarCampo(container, "Altura", caso.getAltura() > 0 ? caso.getAltura() + " cm" : "-");
        adicionarCampo(container, "Pele", caso.getCorPele());
        adicionarCampo(container, "Cabelo", caso.getCorCabelo() + " (" + caso.getTipoCabelo() + ")");
        adicionarCampo(container, "Olhos", caso.getCorOlhos());
        adicionarCampo(container, "Sinais", caso.getSinaisParticulares());

        adicionarSubtitulo(container, "Endereço Residencial");
        adicionarCampo(container, "Logradouro", caso.getLogradouro());
        adicionarCampo(container, "Bairro", caso.getBairro());
        adicionarCampo(container, "Município/UF", caso.getMunicipio() + " - " + caso.getEstado());

        configurarCliqueSecao(header, container);
    }

    private void popularSecaoDadosOcorrencia(Caso caso) {
        TextView titulo = secaoDadosOcorrencia.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoDadosOcorrencia.findViewById(R.id.container);
        View header = secaoDadosOcorrencia.findViewById(R.id.header);

        titulo.setText("Dados da Ocorrência");
        container.removeAllViews();

        adicionarCampo(container, "Data Desaparecimento", caso.getDataDesaparecimento());
        adicionarCampo(container, "Data Registro", caso.getDataRegistro());
        adicionarCampo(container, "B.O.", caso.getNumeroBO());

        adicionarSubtitulo(container, "Local do Fato");
        adicionarCampo(container, "Logradouro", caso.getLogradouro_desaparecimento());
        adicionarCampo(container, "Bairro", caso.getBairro_desaparecimento());
        adicionarCampo(container, "Município/UF", caso.getMunicipio_desaparecimento() + " - " + caso.getEstado_desaparecimento());

        adicionarSubtitulo(container, "Outros");
        adicionarCampo(container, "Vestimenta", caso.getVestimenta());
        adicionarCampo(container, "Objetos", caso.getObjetos());

        configurarCliqueSecao(header, container);
    }

    private void popularSecaoComunicante(Caso caso) {
        TextView titulo = secaoDadosComunicante.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoDadosComunicante.findViewById(R.id.container);
        View header = secaoDadosComunicante.findViewById(R.id.header);

        titulo.setText("Dados do Comunicante");
        container.removeAllViews();

        String idComunicante = caso.getId_comunicante();
        if (idComunicante == null || idComunicante.isEmpty()) {
            adicionarCampo(container, "Status", "Não informado.");
        } else {
            db.collection("comunicante").document(idComunicante).get()
                    .addOnSuccessListener(doc -> {
                        container.removeAllViews();
                        if (doc.exists()) {
                            Comunicante com = doc.toObject(Comunicante.class);
                            if (com != null) {
                                adicionarCampo(container, "Nome", com.getNomeCompleto());
                                adicionarCampo(container, "Celular", com.getCelular());
                                adicionarCampo(container, "Telefone", com.getTelefone());
                                adicionarCampo(container, "Email", com.getEmail());
                                adicionarCampo(container, "Endereço", com.getLogradouro() + ", " + com.getNumero());
                            }
                        } else {
                            adicionarCampo(container, "Erro", "Comunicante não encontrado.");
                        }
                    });
            adicionarCampo(container, "Aguarde", "Carregando dados...");
        }
        configurarCliqueSecao(header, container);
    }

    private void popularSecaoAlertas(Caso caso) {
        TextView titulo = secaoAlertasEmitidos.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoAlertasEmitidos.findViewById(R.id.container);
        View header = secaoAlertasEmitidos.findViewById(R.id.header);

        titulo.setText("Alertas Emitidos");
        container.removeAllViews();
        adicionarCampo(container, "Status", "Nenhum alerta emitido.");
        configurarCliqueSecao(header, container);
    }

    // --- MÉTODOS AUXILIARES DE UI ---

    private void adicionarCampo(LinearLayout container, String rotulo, String valor) {
        if (valor == null || valor.trim().isEmpty() || valor.equals("0") || valor.equals("-")) return;

        View view = getLayoutInflater().inflate(R.layout.include_edit_group, container, false);

        TextView tvLabel = view.findViewById(R.id.edit_group_title);
        TextView tvValue = view.findViewById(R.id.edit_group_value);

        tvLabel.setText(rotulo);
        tvValue.setText(valor);

        container.addView(view);
    }

    private void adicionarSubtitulo(LinearLayout container, String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setTextSize(14);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setPadding(0, 24, 0, 8);
        tv.setTextColor(getResources().getColor(R.color.purple_500));
        container.addView(tv);
    }

    private void configurarCliqueSecao(View header, View container) {
        header.setOnClickListener(v -> {
            boolean isVisible = container.getVisibility() == View.VISIBLE;
            container.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            ImageView arrow = header.findViewById(R.id.ivArrow);
            if (arrow != null) {
                arrow.animate().rotation(isVisible ? 0 : 180).setDuration(200).start();
            }
        });
    }
}