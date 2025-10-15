package com.example.reconhecimentofacial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FormCadastroAdm extends AppCompatActivity {

    private TextView text_tela_cadastro_adm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_adm);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button buttonCadastroAdm = findViewById(R.id.bt_finalizar_adm);
        buttonCadastroAdm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormCadastroAdm.this, Monitoramento.class);
                startActivity(intent);
            }
        });
    }

    private void IniciarComponentes(){
        text_tela_cadastro_adm = findViewById(R.id.text_tela_cadastro_adm);
    }
}