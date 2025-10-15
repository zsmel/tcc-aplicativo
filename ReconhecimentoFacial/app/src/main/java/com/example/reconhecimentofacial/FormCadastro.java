package com.example.reconhecimentofacial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FormCadastro extends AppCompatActivity {

    private TextView text_tela_cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button buttonCadastro = findViewById(R.id.bt_finalizar_pub);
        buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormCadastro.this, Monitoramento.class);
                startActivity(intent);
            }
        });
    }

    private void IniciarComponentes(){
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
    }
}