package com.example.reconhecimentofacial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FormLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button buttonContinuar = findViewById(R.id.bt_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        Button buttonEntre = findViewById(R.id.bt_entre);
        buttonEntre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormLog.class);
                startActivity(intent);
            }
        });

        TextView textLink = findViewById(R.id.text_cadastro_adm);
        textLink.setPaintFlags(textLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastroAdm.class);
                startActivity(intent);
            }
        });
    }
}