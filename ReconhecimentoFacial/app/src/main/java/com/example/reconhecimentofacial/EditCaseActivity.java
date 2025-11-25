package com.example.reconhecimentofacial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditCaseActivity extends AppCompatActivity {

    // Desaparecido
    private TextInputEditText etNome, etStatus, etDataNasc, etIdade, etCpf, etRg, etOrgao, etSexo;
    private TextInputEditText etAltura, etPele, etCabelo, etOlhos, etSinais;
    private TextInputEditText etMae, etPai, etLogradouro, etMunicipio;
    private TextInputEditText etDataDesap, etLogradouroDesap, etMunicipioDesap, etEstadoDesap;

    private CheckBox cbDoenca, cbTelefone, cbRedes, cbVeiculo;

    // Comunicante
    private TextInputEditText etComNome, etComCelular, etComEmail, etComEndereco;

    private Button btnSalvar, btnCancelar;
    private ProgressBar progressBar;

    private FirebaseFirestore db;
    private String casoId;
    private String comunicanteId; // Para guardar o ID do comunicante

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_case);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        casoId = getIntent().getStringExtra("CASO_ID");

        initViews();

        if (casoId != null) {
            carregarDados();
        } else {
            Toast.makeText(this, "Erro: ID inválido.", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnCancelar.setOnClickListener(v -> finish());
        btnSalvar.setOnClickListener(v -> salvarTudo());
    }

    private void initViews() {
        // Desaparecido
        etNome = findViewById(R.id.etEditNome);
        etStatus = findViewById(R.id.etEditStatus);
        etDataNasc = findViewById(R.id.etEditDataNasc);
        etIdade = findViewById(R.id.etEditIdade);
        etCpf = findViewById(R.id.etEditCpf);
        etRg = findViewById(R.id.etEditRg);
        etOrgao = findViewById(R.id.etEditOrgao);
        etSexo = findViewById(R.id.etEditSexo);

        etAltura = findViewById(R.id.etEditAltura);
        etPele = findViewById(R.id.etEditPele);
        etCabelo = findViewById(R.id.etEditCabelo);
        etOlhos = findViewById(R.id.etEditOlhos);
        etSinais = findViewById(R.id.etEditSinais);

        etMae = findViewById(R.id.etEditMae);
        etPai = findViewById(R.id.etEditPai);
        etLogradouro = findViewById(R.id.etEditLogradouro);
        etMunicipio = findViewById(R.id.etEditMunicipio);

        etDataDesap = findViewById(R.id.etEditDataDesap);
        etLogradouroDesap = findViewById(R.id.etEditLogradouroDesap);
        etMunicipioDesap = findViewById(R.id.etEditMunicipioDesap);
        etEstadoDesap = findViewById(R.id.etEditEstadoDesap);

        cbDoenca = findViewById(R.id.cbEditDoenca);
        cbTelefone = findViewById(R.id.cbEditTelefone);
        cbRedes = findViewById(R.id.cbEditRedes);
        cbVeiculo = findViewById(R.id.cbEditVeiculo);

        // Comunicante
        etComNome = findViewById(R.id.etEditComNome);
        etComCelular = findViewById(R.id.etEditComCelular);
        etComEmail = findViewById(R.id.etEditComEmail);
        etComEndereco = findViewById(R.id.etEditComEndereco);

        btnSalvar = findViewById(R.id.btnSalvarEdicao);
        btnCancelar = findViewById(R.id.btnCancelarEdicao);
        progressBar = findViewById(R.id.pbEdicao);
    }

    private void carregarDados() {
        progressBar.setVisibility(View.VISIBLE);

        // 1. Busca o Desaparecido
        db.collection("desaparecidos").document(casoId).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Caso caso = doc.toObject(Caso.class);
                        if (caso != null) {
                            preencherDesaparecido(caso);

                            // 2. Se tiver comunicante, busca ele também
                            comunicanteId = caso.getId_comunicante();
                            if (comunicanteId != null && !comunicanteId.isEmpty()) {
                                carregarComunicante(comunicanteId);
                            } else {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void carregarComunicante(String id) {
        db.collection("comunicante").document(id).get()
                .addOnSuccessListener(doc -> {
                    progressBar.setVisibility(View.GONE);
                    if (doc.exists()) {
                        Comunicante com = doc.toObject(Comunicante.class);
                        if (com != null) {
                            etComNome.setText(com.getNomeCompleto());
                            etComCelular.setText(com.getCelular());
                            etComEmail.setText(com.getEmail());
                            etComEndereco.setText(com.getLogradouro() + ", " + com.getNumero());
                        }
                    }
                })
                .addOnFailureListener(e -> progressBar.setVisibility(View.GONE));
    }

    private void preencherDesaparecido(Caso c) {
        setText(etNome, c.getNomeCompleto());
        setText(etStatus, c.getEstado_desaparecimento());
        setText(etDataNasc, c.getDataNascimento());
        etIdade.setText(String.valueOf(c.getIdadeEpoca()));
        setText(etCpf, c.getCpf());
        setText(etRg, c.getRg());
        setText(etOrgao, c.getOrgaoExpedidor());
        setText(etSexo, c.getSexo());

        etAltura.setText(String.valueOf(c.getAltura()));
        setText(etPele, c.getCorPele());
        setText(etCabelo, c.getCorCabelo());
        setText(etOlhos, c.getCorOlhos());
        setText(etSinais, c.getSinaisParticulares());

        setText(etMae, c.getNomeMae());
        setText(etPai, c.getNomePai());
        setText(etLogradouro, c.getLogradouro());
        setText(etMunicipio, c.getMunicipio());

        setText(etDataDesap, c.getDataDesaparecimento());
        setText(etLogradouroDesap, c.getLogradouro_desaparecimento());
        setText(etMunicipioDesap, c.getMunicipio_desaparecimento());
        setText(etEstadoDesap, c.getEstado_desaparecimento());

        cbDoenca.setChecked(c.isDoenca_mental());
        cbTelefone.setChecked(c.isUsavaTelefone());
        cbRedes.setChecked(c.isPerfil_redes());
        cbVeiculo.setChecked(c.isDirigia_veiculo());
    }

    private void salvarTudo() {
        if (getString(etNome).isEmpty()) {
            Toast.makeText(this, "Nome é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(false);

        // 1. Atualiza Desaparecido
        Map<String, Object> updDesap = new HashMap<>();
        updDesap.put("nomeCompleto", getString(etNome));
        updDesap.put("estado_desaparecimento", getString(etStatus));
        updDesap.put("dataNascimento", getString(etDataNasc));
        updDesap.put("idadeEpoca", parseIntOrZero(etIdade));
        updDesap.put("cpf", getString(etCpf));
        updDesap.put("rg", getString(etRg));
        updDesap.put("orgaoExpedidor", getString(etOrgao));
        updDesap.put("sexo", getString(etSexo));

        updDesap.put("altura", parseIntOrZero(etAltura));
        updDesap.put("corPele", getString(etPele));
        updDesap.put("corCabelo", getString(etCabelo));
        updDesap.put("corOlhos", getString(etOlhos));
        updDesap.put("sinaisParticulares", getString(etSinais));

        updDesap.put("nomeMae", getString(etMae));
        updDesap.put("nomePai", getString(etPai));
        updDesap.put("logradouro", getString(etLogradouro));
        updDesap.put("municipio", getString(etMunicipio));

        updDesap.put("dataDesaparecimento", getString(etDataDesap));
        updDesap.put("logradouro_desaparecimento", getString(etLogradouroDesap));
        updDesap.put("municipio_desaparecimento", getString(etMunicipioDesap));
        updDesap.put("estado_desaparecimento", getString(etEstadoDesap));

        updDesap.put("doenca_mental", cbDoenca.isChecked());
        updDesap.put("usavaTelefone", cbTelefone.isChecked());
        updDesap.put("perfil_redes", cbRedes.isChecked());
        updDesap.put("dirigia_veiculo", cbVeiculo.isChecked());

        db.collection("desaparecidos").document(casoId).update(updDesap)
                .addOnSuccessListener(aVoid -> {
                    // 2. Se tiver comunicante, atualiza ele também
                    if (comunicanteId != null && !comunicanteId.isEmpty()) {
                        Map<String, Object> updCom = new HashMap<>();
                        updCom.put("nomeCompleto", getString(etComNome));
                        updCom.put("celular", getString(etComCelular));
                        updCom.put("email", getString(etComEmail));
                        updCom.put("logradouro", getString(etComEndereco)); // Simplificado para exemplo

                        db.collection("comunicante").document(comunicanteId).update(updCom)
                                .addOnSuccessListener(a -> finalizar())
                                .addOnFailureListener(e -> finalizar()); // Finaliza mesmo se falhar o comunicante
                    } else {
                        finalizar();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSalvar.setEnabled(true);
                    Toast.makeText(this, "Erro ao atualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void finalizar() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Dados salvos!", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Auxiliares
    private String getString(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void setText(TextInputEditText et, String txt) {
        if (txt != null) et.setText(txt);
    }

    private int parseIntOrZero(TextInputEditText et) {
        String s = getString(et);
        try { return s.isEmpty() ? 0 : Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}