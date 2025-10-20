package com.example.reconhecimentofacial;

// Importações necessárias para a tela e para o Firebase
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Importar EditText
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class FormCadastroAdm extends AppCompatActivity {

    // 1. Declare as variáveis para cada campo de entrada da sua tela
    private EditText editNome, editDataNascimento, editCPF, editTelefone, editEndereco,
            editInstituicao, editMatricula, editCargo, editEmail, editSenha;
    private Button buttonCadastroAdm;

    // Declare a variável do Firestore
    private FirebaseFirestore db;
    private static final String TAG = "FormCadastroAdm"; // Tag para logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_adm);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 2. Chame o método para inicializar os componentes
        iniciarComponentes();

        // 3. Configure a ação do botão
        buttonCadastroAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pega o texto de cada campo no momento do clique
                String nome = editNome.getText().toString().trim();
                String dataNascimento = editDataNascimento.getText().toString().trim();
                String cpf = editCPF.getText().toString().trim();
                String telefone = editTelefone.getText().toString().trim();
                String endereco = editEndereco.getText().toString().trim();
                String instituicao = editInstituicao.getText().toString().trim();
                String matricula = editMatricula.getText().toString().trim();
                String cargo = editCargo.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();

                // Validação simples para garantir que os campos principais não estão vazios
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(FormCadastroAdm.this, "Preencha nome, email e senha!", Toast.LENGTH_SHORT).show();
                    return; // Interrompe a execução se os campos estiverem vazios
                }

                // Cria o "pacote" de dados (Map) com as informações coletadas
                Map<String, Object> novoAdministrador = new HashMap<>();
                novoAdministrador.put("nomeCompleto", nome);
                novoAdministrador.put("dataNascimento", dataNascimento);
                novoAdministrador.put("CPF", cpf);
                novoAdministrador.put("telefone", telefone);
                novoAdministrador.put("endereco", endereco);
                novoAdministrador.put("instituicao", instituicao);
                novoAdministrador.put("matricula", matricula);
                novoAdministrador.put("cargo", cargo);
                novoAdministrador.put("email", email);
                // ⚠️ AVISO DE SEGURANÇA: Nunca salve senhas em texto puro. O ideal é usar o Firebase Authentication.
                novoAdministrador.put("senha", senha);

                // Envia os dados para o Firestore
                db.collection("administradores")
                        .add(novoAdministrador) // .add() cria um novo documento com ID aleatório
                        .addOnSuccessListener(documentReference -> {
                            // SUCESSO!
                            Toast.makeText(FormCadastroAdm.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Documento salvo com ID: " + documentReference.getId());

                            // Navega para a próxima tela APÓS o sucesso
                            Intent intent = new Intent(FormCadastroAdm.this, Monitoramento.class);
                            startActivity(intent);
                            finish(); // Opcional: fecha a tela de cadastro para o usuário não voltar
                        })
                        .addOnFailureListener(e -> {
                            // ERRO!
                            Toast.makeText(FormCadastroAdm.this, "Erro ao realizar o cadastro.", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Erro ao adicionar documento", e);
                        });
            }
        });
    }

    private void iniciarComponentes() {
        // Inicializa o Firestore
        db = FirebaseFirestore.getInstance();

        // Conecta as variáveis aos IDs exatos do seu arquivo activity_form_cadastro_adm.xml
        editNome = findViewById(R.id.cad_name);
        editDataNascimento = findViewById(R.id.cad_date);
        editCPF = findViewById(R.id.cad_cpf);
        editTelefone = findViewById(R.id.cad_phone);
        editEndereco = findViewById(R.id.cad_address);
        editInstituicao = findViewById(R.id.cad_instituicao);
        editMatricula = findViewById(R.id.cad_matricula);
        editCargo = findViewById(R.id.cad_cargo);
        editEmail = findViewById(R.id.cad_email);
        editSenha = findViewById(R.id.cad_senha);
        buttonCadastroAdm = findViewById(R.id.bt_finalizar_adm);
    }
}