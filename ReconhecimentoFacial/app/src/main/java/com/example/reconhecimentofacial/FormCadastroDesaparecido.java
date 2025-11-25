package com.example.reconhecimentofacial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FormCadastroDesaparecido extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALERIA = 2;

    // UI Components
    private ImageView ivFoto;
    private Button btnProximo, btnFoto, btnGaleria;
    private ProgressBar progressBar;
    private LinearLayout containerVeiculo;

    private TextInputEditText etNome, etDataNasc, etIdade, etCpf, etRg, etOrgao;
    private TextInputEditText etAltura, etPele, etCabeloCor, etCabeloTipo, etOlhos, etSinais;
    private TextInputEditText etMae, etPai;
    private TextInputEditText etLogradouro, etNumero, etBairro, etMunicipio;
    private TextInputEditText etDataDesap, etDataReg, etBo, etDelegacia;
    private TextInputEditText etLogradouroDesap, etNumeroDesap, etBairroDesap, etMunicipioDesap;
    private TextInputEditText etVestimenta, etObjetos;
    private TextInputEditText etVeiculoModelo, etVeiculoPlaca;

    private Spinner spSexo, spEstado, spEstadoDesap;
    private CheckBox cbDoencaMental, cbUsavaTelefone, cbRedesSociais, cbDirigiaVeiculo;

    private Bitmap fotoBitmap;
    private Uri fotoUri;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    private static final String[] SEXOS = {"Masculino", "Feminino", "Não Informado"};
    private static final String[] ESTADOS = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_desaparecido);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        initViews();
        setupListeners();
    }

    private void initViews() {
        ivFoto = findViewById(R.id.ivFotoDesaparecido);
        btnProximo = findViewById(R.id.btnProximo);
        btnFoto = findViewById(R.id.btnTirarFoto);
        btnGaleria = findViewById(R.id.btnGaleria);
        progressBar = findViewById(R.id.progressBarCadastro);
        containerVeiculo = findViewById(R.id.containerVeiculo);

        etNome = findViewById(R.id.etNome);
        etDataNasc = findViewById(R.id.etDataNasc);
        etIdade = findViewById(R.id.etIdade);
        etCpf = findViewById(R.id.etCpf);
        etRg = findViewById(R.id.etRg);
        etOrgao = findViewById(R.id.etOrgao);

        etAltura = findViewById(R.id.etAltura);
        etPele = findViewById(R.id.etPele);
        etCabeloCor = findViewById(R.id.etCabeloCor);
        etCabeloTipo = findViewById(R.id.etCabeloTipo);
        etOlhos = findViewById(R.id.etOlhos);
        etSinais = findViewById(R.id.etSinais);

        etMae = findViewById(R.id.etMae);
        etPai = findViewById(R.id.etPai);

        etLogradouro = findViewById(R.id.etLogradouro);
        etNumero = findViewById(R.id.etNumero);
        etBairro = findViewById(R.id.etBairro);
        etMunicipio = findViewById(R.id.etMunicipio);

        etDataDesap = findViewById(R.id.etDataDesap);
        etDataReg = findViewById(R.id.etDataReg);
        etBo = findViewById(R.id.etBo);
        etDelegacia = findViewById(R.id.etDelegacia);

        etLogradouroDesap = findViewById(R.id.etLogradouroDesap);
        etNumeroDesap = findViewById(R.id.etNumeroDesap);
        etBairroDesap = findViewById(R.id.etBairroDesap);
        etMunicipioDesap = findViewById(R.id.etMunicipioDesap);

        etVestimenta = findViewById(R.id.etVestimenta);
        etObjetos = findViewById(R.id.etObjetos);

        cbDoencaMental = findViewById(R.id.cbDoencaMental);
        cbUsavaTelefone = findViewById(R.id.cbUsavaTelefone);
        cbRedesSociais = findViewById(R.id.cbRedesSociais);
        cbDirigiaVeiculo = findViewById(R.id.cbDirigiaVeiculo);

        etVeiculoModelo = findViewById(R.id.etVeiculoModelo);
        etVeiculoPlaca = findViewById(R.id.etVeiculoPlaca);

        spSexo = findViewById(R.id.spSexo);
        spEstado = findViewById(R.id.spEstado);
        spEstadoDesap = findViewById(R.id.spEstadoDesap);

        ArrayAdapter<String> adapterSexo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, SEXOS);
        spSexo.setAdapter(adapterSexo);

        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ESTADOS);
        spEstado.setAdapter(adapterEstados);
        spEstadoDesap.setAdapter(adapterEstados);

        spEstado.setSelection(24);
        spEstadoDesap.setSelection(24);
    }

    private void setupListeners() {
        btnFoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_GALERIA);
        });

        cbDirigiaVeiculo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            containerVeiculo.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        btnProximo.setOnClickListener(v -> salvar());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    fotoBitmap = (Bitmap) extras.get("data");
                    fotoUri = null;
                    ivFoto.setImageBitmap(fotoBitmap);
                }
            } else if (requestCode == REQUEST_GALERIA) {
                fotoUri = data.getData();
                fotoBitmap = null;
                ivFoto.setImageURI(fotoUri);
            }
        }
    }

    private void salvar() {
        if (getString(etNome).isEmpty()) {
            Toast.makeText(this, "O Nome Completo é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnProximo.setEnabled(false);

        if (fotoBitmap != null || fotoUri != null) {
            fazerUploadFoto();
        } else {
            salvarDadosFirestore(null);
        }
    }

    private void fazerUploadFoto() {
        String nomeArquivo = UUID.randomUUID().toString() + ".jpg";
        StorageReference refFoto = storageRef.child("fotos_desaparecidos/" + nomeArquivo);
        UploadTask uploadTask;

        if (fotoBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            uploadTask = refFoto.putBytes(baos.toByteArray());
        } else {
            uploadTask = refFoto.putFile(fotoUri);
        }

        uploadTask.addOnSuccessListener(taskSnapshot -> refFoto.getDownloadUrl().addOnSuccessListener(uri -> {
            salvarDadosFirestore(uri.toString());
        })).addOnFailureListener(e -> {
            Toast.makeText(this, "Erro foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnProximo.setEnabled(true);
        });
    }

    private void salvarDadosFirestore(String urlFoto) {
        Map<String, Object> caso = new HashMap<>();

        caso.put("nomeCompleto", getString(etNome));
        caso.put("dataNascimento", getString(etDataNasc));
        caso.put("idadeEpoca", parseIntOrZero(etIdade));
        caso.put("cpf", getString(etCpf));
        caso.put("rg", getString(etRg));
        caso.put("orgaoExpedidor", getString(etOrgao));
        caso.put("sexo", spSexo.getSelectedItem().toString());

        caso.put("altura", parseIntOrZero(etAltura));
        caso.put("corPele", getString(etPele));
        caso.put("corCabelo", getString(etCabeloCor));
        caso.put("tipoCabelo", getString(etCabeloTipo));
        caso.put("corOlhos", getString(etOlhos));
        caso.put("sinaisParticulares", getString(etSinais));

        caso.put("nomeMae", getString(etMae));
        caso.put("nomePai", getString(etPai));

        caso.put("logradouro", getString(etLogradouro));
        caso.put("numero", parseIntOrZero(etNumero));
        caso.put("bairro", getString(etBairro));
        caso.put("municipio", getString(etMunicipio));
        caso.put("estado", spEstado.getSelectedItem().toString());

        caso.put("dataDesaparecimento", getString(etDataDesap));
        caso.put("dataRegistro", getString(etDataReg));
        caso.put("numeroBO", getString(etBo));
        caso.put("delegaciaRegistro", getString(etDelegacia));

        caso.put("logradouro_desaparecimento", getString(etLogradouroDesap));
        caso.put("numero_desaparecimento", parseIntOrZero(etNumeroDesap));
        caso.put("bairro_desaparecimento", getString(etBairroDesap));
        caso.put("municipio_desaparecimento", getString(etMunicipioDesap));
        caso.put("estado_desaparecimento", spEstadoDesap.getSelectedItem().toString());

        caso.put("vestimenta", getString(etVestimenta));
        caso.put("objetos", getString(etObjetos));

        caso.put("doenca_mental", cbDoencaMental.isChecked());
        caso.put("usavaTelefone", cbUsavaTelefone.isChecked());
        caso.put("perfil_redes", cbRedesSociais.isChecked());
        caso.put("dirigia_veiculo", cbDirigiaVeiculo.isChecked());

        caso.put("modelo_veiculo", getString(etVeiculoModelo));
        caso.put("placa_veiculo", getString(etVeiculoPlaca));

        if (urlFoto != null) {
            caso.put("fotos", Collections.singletonList(urlFoto));
        } else {
            caso.put("fotos", new ArrayList<>());
        }
        caso.put("id_comunicante", "");
        caso.put("estado_desaparecimento_status", "Em andamento");

        db.collection("desaparecidos").add(caso)
                .addOnSuccessListener(docRef -> {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(FormCadastroDesaparecido.this, FormCadastroComunicante.class);
                    intent.putExtra("ID_DESAPARECIDO", docRef.getId());
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnProximo.setEnabled(true);
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