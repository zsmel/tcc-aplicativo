package com.example.reconhecimentofacial;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FormCadastroComunicante extends AppCompatActivity {

    // UI Components
    private Button btnFinalizar;
    private TextInputEditText etNome, etDataNasc, etSexo, etMae, etPai;
    private TextInputEditText etCpf, etRg, etOrgao;
    private TextInputEditText etCelular, etTelefone, etEmail;
    private TextInputEditText etLogradouro, etNumero, etBairro, etMunicipio, etEstado;

    private FirebaseFirestore db;
    private String idDesaparecidoCriado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_comunicante);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        idDesaparecidoCriado = getIntent().getStringExtra("ID_DESAPARECIDO");
        db = FirebaseFirestore.getInstance();

        initViews();

        btnFinalizar.setOnClickListener(v -> finalizarCadastro());
    }

    private void initViews() {
        btnFinalizar = findViewById(R.id.btnFinalizarTudo);

        etNome = findViewById(R.id.etComNome);
        etDataNasc = findViewById(R.id.etComDataNasc);
        etSexo = findViewById(R.id.etComSexo);
        etMae = findViewById(R.id.etComMae);
        etPai = findViewById(R.id.etComPai);

        etCpf = findViewById(R.id.etComCpf);
        etRg = findViewById(R.id.etComRg);
        etOrgao = findViewById(R.id.etComOrgao);

        etCelular = findViewById(R.id.etComCelular);
        etTelefone = findViewById(R.id.etComTelefone);
        etEmail = findViewById(R.id.etComEmail);

        etLogradouro = findViewById(R.id.etComLogradouro);
        etNumero = findViewById(R.id.etComNumero);
        etBairro = findViewById(R.id.etComBairro);
        etMunicipio = findViewById(R.id.etComMunicipio);
        etEstado = findViewById(R.id.etComEstado);
    }

    private void finalizarCadastro() {
        String nome = getString(etNome);
        String celular = getString(etCelular);

        if (nome.isEmpty() || celular.isEmpty()) {
            Toast.makeText(this, "Nome e Celular são obrigatórios.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnFinalizar.setEnabled(false);

        Map<String, Object> comunicante = new HashMap<>();
        comunicante.put("nomeCompleto", nome);
        comunicante.put("dataNascimento", getString(etDataNasc));
        comunicante.put("sexo", getString(etSexo));
        comunicante.put("nomeMae", getString(etMae));
        comunicante.put("nomePai", getString(etPai));

        comunicante.put("cpf", getString(etCpf));
        comunicante.put("rg", getString(etRg));
        comunicante.put("orgaoExpedidor", getString(etOrgao));

        comunicante.put("celular", celular);
        comunicante.put("telefone", getString(etTelefone));
        comunicante.put("email", getString(etEmail));

        comunicante.put("logradouro", getString(etLogradouro));
        comunicante.put("numero", parseIntOrZero(etNumero));
        comunicante.put("bairro", getString(etBairro));
        comunicante.put("municipio", getString(etMunicipio));
        comunicante.put("estado", getString(etEstado));

        db.collection("comunicante").add(comunicante)
                .addOnSuccessListener(docRef -> {
                    String idComunicante = docRef.getId();

                    if (idDesaparecidoCriado != null && !idDesaparecidoCriado.isEmpty()) {
                        db.collection("desaparecidos").document(idDesaparecidoCriado)
                                .update("id_comunicante", idComunicante)
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(this, "Cadastro finalizado com sucesso!", Toast.LENGTH_LONG).show();
                                    finish();
                                });
                    } else {
                        Toast.makeText(this, "Comunicante salvo!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    btnFinalizar.setEnabled(true);
                    Toast.makeText(this, "Erro ao salvar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getString(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private int parseIntOrZero(TextInputEditText et) {
        String s = getString(et);
        try { return s.isEmpty() ? 0 : Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }
}