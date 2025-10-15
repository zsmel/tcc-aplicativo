package com.example.reconhecimentofacial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FormLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_log);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button buttonLogin = findViewById(R.id.bt_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLog.this, Monitoramento.class);
                startActivity(intent);
            }
        });
    }
}