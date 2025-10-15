package com.example.reconhecimentofacial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class EditCaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_case);

        Button btnCancelar = findViewById(R.id.btnCancelar);
        Button btnSalvar   = findViewById(R.id.btnSalvarEdicao);

        // Único listener: fecha a tela pegando a Activity a partir da View (sem usar this/dispatcher)
        View.OnClickListener close = new View.OnClickListener() {
            @Override public void onClick(View v) {
                Activity a = (Activity) v.getContext();
                a.finish();
            }
        };

        btnCancelar.setOnClickListener(close);
        btnSalvar.setOnClickListener(close);
    }
}