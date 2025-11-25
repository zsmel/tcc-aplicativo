package com.example.reconhecimentofacial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FormLog extends AppCompatActivity {

    private EditText edit_email, edit_senha;
    private Button bt_login;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_log);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        IniciarComponentes();

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString().trim();
                String senha = edit_senha.getText().toString().trim();

                if (email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(FormLog.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    AutenticarUsuario(email, senha);
                }
            }
        });
    }

    private void AutenticarUsuario(String email, String senha) {
        // Mostra o loading e bloqueia o botão
        progressBar.setVisibility(View.VISIBLE);
        bt_login.setEnabled(false);

        // Consulta no Firestore: Coleção 'administradores'
        // Procura onde 'email' é igual ao digitado E 'senha' é igual a digitada
        db.collection("administradores")
                .whereEqualTo("email", email)
                .whereEqualTo("senha", senha)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();

                        // Se a lista não estiver vazia, achou o usuário!
                        if (document != null && !document.isEmpty()) {
                            logarComSucesso();
                        } else {
                            // Consulta funcionou, mas não achou ninguém com esse email/senha
                            falhaLogin("Email ou senha incorretos.");
                        }
                    } else {
                        // Erro de conexão ou no Firebase
                        falhaLogin("Erro ao acessar o banco de dados.");
                    }
                });
    }

    private void logarComSucesso() {
        // Delay visual pequeno apenas para o usuário ver que processou
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(FormLog.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FormLog.this, Monitoramento.class);
            startActivity(intent);
            finish(); // Fecha a tela de login para não voltar nela com o botão voltar
        }, 1000);
    }

    private void falhaLogin(String motivo) {
        progressBar.setVisibility(View.GONE);
        bt_login.setEnabled(true);
        Toast.makeText(FormLog.this, motivo, Toast.LENGTH_SHORT).show();
    }

    private void IniciarComponentes() {
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_login = findViewById(R.id.bt_login);
        progressBar = findViewById(R.id.progressbar);
        db = FirebaseFirestore.getInstance();
    }
}