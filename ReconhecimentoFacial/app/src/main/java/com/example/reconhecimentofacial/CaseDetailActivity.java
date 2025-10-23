package com.example.reconhecimentofacial;

// Não há 'import android.R;' aqui
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

public class CaseDetailActivity extends AppCompatActivity {

    private TextView textViewNome, textViewStatus;
    private View secaoDadosPessoais, secaoDadosOcorrencia, secaoDadosComunicante, secaoAlertasEmitidos;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        db = FirebaseFirestore.getInstance();
        inicializarComponentes();

        String casoId = getIntent().getStringExtra("CASO_ID");
        if (casoId != null && !casoId.isEmpty()) {
            carregarDadosDoCaso(casoId);
        } else {
            Toast.makeText(this, "Erro: ID do caso não encontrado.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void inicializarComponentes() {
        textViewNome = findViewById(R.id.tvNome);
        textViewStatus = findViewById(R.id.tvStatus);
        secaoDadosPessoais = findViewById(R.id.secDadosPessoais);
        secaoDadosOcorrencia = findViewById(R.id.secDadosOcorrencia);
        secaoDadosComunicante = findViewById(R.id.secDadosComunicante);
        secaoAlertasEmitidos = findViewById(R.id.secAlertasEmitidos);
    }

    private void carregarDadosDoCaso(String casoId) {
        db.collection("desaparecidos").document(casoId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Caso caso = documentSnapshot.toObject(Caso.class);
                        if (caso != null) {
                            textViewNome.setText(caso.getNomeCompleto());
                            textViewStatus.setText(caso.getEstado_desaparecimento());

                            popularSecaoDadosPessoais(caso);
                            popularSecaoDadosOcorrencia(caso);
                            popularSecaoComunicante(caso);
                            popularSecaoAlertas(caso);
                        }
                    } else {
                        Toast.makeText(this, "Caso não encontrado.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao carregar dados: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("Firestore", "Erro ao buscar documento", e);
                });
    }

    private void popularSecaoDadosPessoais(Caso caso) {
        TextView titulo = secaoDadosPessoais.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoDadosPessoais.findViewById(R.id.container);
        View header = secaoDadosPessoais.findViewById(R.id.header);

        titulo.setText("Dados Pessoais");
        container.removeAllViews();
        adicionarCampo(container, "Nome completo:", caso.getNomeCompleto());
        adicionarCampo(container, "Data de nascimento:", caso.getDataNascimento());
        adicionarCampo(container, "Sexo:", caso.getSexo());
        adicionarCampo(container, "CPF:", caso.getCpf());
        adicionarCampo(container, "RG:", caso.getRg());
        adicionarCampo(container, "Órgão expedidor:", caso.getOrgaoExpedidor());
        adicionarCampo(container, "Nome do pai:", caso.getNomePai());
        adicionarCampo(container, "Nome da mãe:", caso.getNomeMae());
        adicionarCampo(container, "Idade à época:", String.valueOf(caso.getIdadeEpoca()));
        adicionarSubtitulo(container, "Informações adicionais");
        adicionarInfoAdicional(container, "É pessoa com doença mental", caso.isDoenca_mental());
        adicionarInfoAdicional(container, "Usava telefone celular quando desapareceu", caso.isUsavaTelefone());
        adicionarInfoAdicional(container, "Dirigia algum veículo quando desapareceu", caso.isDirigia_veiculo());
        if (caso.isDirigia_veiculo()) {
            adicionarCampo(container, "   Placa:", caso.getPlaca_veiculo() + " - Modelo: " + caso.getModelo_veiculo());
        }
        adicionarInfoAdicional(container, "Possui perfil em redes sociais", caso.isPerfil_redes());
        adicionarSubtitulo(container, "Endereço Residencial");
        adicionarCampo(container, "Logradouro:", caso.getLogradouro());
        adicionarCampo(container, "Estado:", caso.getEstado());
        adicionarCampo(container, "Município:", caso.getMunicipio());
        adicionarCampo(container, "Bairro:", caso.getBairro());
        adicionarCampo(container, "Número:", String.valueOf(caso.getNumero()));
        adicionarSubtitulo(container, "Características da Vítima");
        adicionarCampo(container, "Cor da pele:", caso.getCorPele());
        adicionarCampo(container, "Cor dos olhos:", caso.getCorOlhos());
        adicionarCampo(container, "Altura:", String.valueOf(caso.getAltura()));
        adicionarCampo(container, "Sinais particulares:", caso.getSinaisParticulares());
        adicionarCampo(container, "Tipo de cabelo:", caso.getTipoCabelo());
        adicionarCampo(container, "Cor de cabelo:", caso.getCorCabelo());
        adicionarCampo(container, "Vestimenta:", caso.getVestimenta());
        adicionarCampo(container, "Objetos:", caso.getObjetos());
        configurarCliqueSecao(header, container);
    }

    private void popularSecaoDadosOcorrencia(Caso caso) {
        TextView titulo = secaoDadosOcorrencia.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoDadosOcorrencia.findViewById(R.id.container);
        View header = secaoDadosOcorrencia.findViewById(R.id.header);

        titulo.setText("Dados da Ocorrência");
        container.removeAllViews();
        adicionarCampo(container, "Número do B.O.:", String.valueOf(caso.getNumeroBO()));
        adicionarCampo(container, "Data do registro:", caso.getDataRegistro());
        adicionarCampo(container, "Data do desaparecimento:", caso.getDataDesaparecimento());
        adicionarCampo(container, "Delegacia do registro:", caso.getDelegaciaRegistro());
        adicionarSubtitulo(container, "Local do desaparecimento");
        adicionarCampo(container, "Logradouro:", caso.getLogradouro_desaparecimento());
        adicionarCampo(container, "Estado:", caso.getEstado_desaparecimento());
        adicionarCampo(container, "Município:", caso.getMunicipio_desaparecimento());
        adicionarCampo(container, "Bairro:", caso.getBairro_desaparecimento());
        adicionarCampo(container, "Número:", String.valueOf(caso.getNumero_desaparecimento()));
        configurarCliqueSecao(header, container);
    }

    private void popularSecaoComunicante(Caso caso) {
        TextView titulo = secaoDadosComunicante.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoDadosComunicante.findViewById(R.id.container);
        View header = secaoDadosComunicante.findViewById(R.id.header);

        titulo.setText("Dados do Comunicante");
        container.removeAllViews();
        String comunicanteId = caso.getId_comunicante();
        if (comunicanteId == null || comunicanteId.trim().isEmpty()) {
            adicionarCampo(container, "Informação:", "Nenhum comunicante vinculado a este caso.");
            configurarCliqueSecao(header, container);
            return;
        }
        adicionarCampo(container, "Carregando dados...", "");
        db.collection("comunicante").document(comunicanteId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    container.removeAllViews();
                    if (documentSnapshot.exists()) {
                        Comunicante comunicante = documentSnapshot.toObject(Comunicante.class);
                        if (comunicante != null) {
                            adicionarCampo(container, "Nome completo:", comunicante.getNomeCompleto());
                            adicionarCampo(container, "Data de nascimento:", comunicante.getDataNascimento());
                            adicionarCampo(container, "Sexo:", comunicante.getSexo());
                            adicionarCampo(container, "CPF:", comunicante.getCpf());
                            adicionarCampo(container, "RG:", comunicante.getRg());
                            adicionarCampo(container, "Órgão expedidor:", comunicante.getOrgaoExpedidor());
                            adicionarCampo(container, "Nome do pai:", comunicante.getNomePai());
                            adicionarCampo(container, "Nome da mãe:", comunicante.getNomeMae());
                            adicionarSubtitulo(container, "Telefone para contato");
                            adicionarCampo(container, "Celular:", comunicante.getCelular());
                            adicionarCampo(container, "Email para contato:", comunicante.getEmail());
                            adicionarSubtitulo(container, "Endereço Residencial");
                            adicionarCampo(container, "Logradouro:", comunicante.getLogradouro());
                            adicionarCampo(container, "Estado:", comunicante.getEstado());
                            adicionarCampo(container, "Município:", comunicante.getMunicipio());
                            adicionarCampo(container, "Bairro:", comunicante.getBairro());
                            adicionarCampo(container, "Número:", comunicante.getNumero());
                        }
                    } else {
                        adicionarCampo(container, "Aviso:", "Comunicante com ID " + comunicanteId + " não encontrado.");
                    }
                })
                .addOnFailureListener(e -> {
                    container.removeAllViews();
                    adicionarCampo(container, "Erro:", "Falha ao carregar dados do comunicante.");
                    Log.e("Firestore", "Erro ao buscar comunicante", e);
                });
        configurarCliqueSecao(header, container);
    }

    private void popularSecaoAlertas(Caso caso) {
        TextView titulo = secaoAlertasEmitidos.findViewById(R.id.tvTitulo);
        LinearLayout container = secaoAlertasEmitidos.findViewById(R.id.container);
        View header = secaoAlertasEmitidos.findViewById(R.id.header);

        titulo.setText("Alertas Emitidos");
        container.removeAllViews();
        adicionarCampo(container, "Status:", "Nenhum alerta emitido no momento.");
        configurarCliqueSecao(header, container);
    }

    private void adicionarCampo(LinearLayout container, String rotulo, String valor) {
        if (valor == null || valor.trim().isEmpty() || valor.equals("0")) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.include_edit_group, container, false);
        ((TextView) view.findViewById(R.id.edit_group_title)).setText(rotulo);
        ((TextView) view.findViewById(R.id.edit_group_value)).setText(valor);
        container.addView(view);
    }

    private void adicionarSubtitulo(LinearLayout container, String titulo) {
        TextView tvSubtitulo = new TextView(this);
        tvSubtitulo.setText(titulo);

        // --- CORREÇÃO ---
        tvSubtitulo.setTextAppearance(android.R.style.TextAppearance_Large);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 24, 0, 8);
        tvSubtitulo.setLayoutParams(params);
        container.addView(tvSubtitulo);
    }

    private void adicionarInfoAdicional(LinearLayout container, String texto, boolean isChecked) {
        TextView tvInfo = new TextView(this);
        tvInfo.setText(texto);

        // --- CORREÇÃO 2: Removido o 'this,' ---
        tvInfo.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Medium);

        int icon = isChecked ? R.drawable.ic_checkbox_on : R.drawable.ic_checkbox_off;
        tvInfo.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
        tvInfo.setCompoundDrawablePadding(16);
        container.addView(tvInfo);
    }

    private void configurarCliqueSecao(View header, View container) {
        header.setOnClickListener(v -> {
            container.setVisibility(container.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
    }
}