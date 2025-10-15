package com.example.reconhecimentofacial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CaseDetailActivity extends AppCompatActivity {

    private SectionHolder sDadosPessoais, sDadosOcorrencia, sDadosComunicante, sAlertas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);

        // Cabeçalho (mock ou via Intent)
        String nome = getIntent().getStringExtra("nome");
        String status = getIntent().getStringExtra("status");
        int foto = getIntent().getIntExtra("fotoResId", 0);

        TextView tvNome = findViewById(R.id.tvNome);
        TextView tvStatus = findViewById(R.id.tvStatus);
        if (nome != null) tvNome.setText(nome);
        if (status != null) tvStatus.setText(status);

        if (foto != 0) {
            ImageView img = findViewById(R.id.imgFoto);
            img.setImageResource(foto);
        }

        // Conteúdos (mock formatado conforme protótipo)
        String dadosPessoais =
                "Nome completo: Melissa Muniz Bandeira\n" +
                        "Data de nascimento: 00/00/0000\n" +
                        "Sexo: F/M\n" +
                        "CPF: 000.000.000-00\n" +
                        "RG: 00.000.000-0\n" +
                        "Órgão expedidor: XX\n" +
                        "Nome do pai: xxxxxxxx\n" +
                        "Nome da mãe: xxxxxxxx\n" +
                        "Idade à época do desaparecimento: 00\n\n" +
                        "Informações adicionais:\n" +
                        "□ É pessoa com doença mental\n" +
                        "□ Usava telefone celular quando desapareceu\n" +
                        "■ Dirigia algum veículo quando desapareceu\n" +
                        "   Placa: XXXX - Modelo: XXXX\n" +
                        "□ Possui perfil em redes sociais\n\n" +
                        "Endereço Residencial\n" +
                        "Logradouro: xxxxxxxx\n" +
                        "Estado: xx\n" +
                        "Município: xxxxxxxx\n" +
                        "Bairro: xxxxxxxx\n" +
                        "Número: 0000\n\n" +
                        "Características da Vítima\n" +
                        "Cor da pele: xxxxxxxx\n" +
                        "Cor dos olhos: xxxxxxxx\n" +
                        "Altura: 0,00\n" +
                        "Sinais particulares: xxxxxxxx\n" +
                        "Tipo de cabelo: xxxxxxxx\n" +
                        "Cor de cabelo: xxxxxxxx\n" +
                        "Vestuário: xxxxxxxx\n" +
                        "Objetos: xxxxxxxx";

        String dadosOcorrencia =
                "Número do B.O.: 000000000000\n" +
                        "Data do registro da ocorrência: 00/00/0000\n" +
                        "Data do desaparecimento: 00/00/0000\n" +
                        "Delegacia do registro: xxxxxxxx\n\n" +
                        "Local do desaparecimento:\n" +
                        "Logradouro: xxxxxxxx\n" +
                        "Estado: xx\n" +
                        "Município: xxxxxxxx\n" +
                        "Bairro: xxxxxxxx\n" +
                        "Número: 0000";

        String dadosComunicante =
                "Nome completo: Melissa Muniz Bandeira\n" +
                        "Data de nascimento: 00/00/0000\n" +
                        "Sexo: F/M\n" +
                        "CPF: 000.000.000-00\n" +
                        "RG: 00.000.000-0\n" +
                        "Órgão expedidor: XX\n" +
                        "Nome do pai: xxxxxxxx\n" +
                        "Nome da mãe: xxxxxxxx\n\n" +
                        "Telefone para contato:\n" +
                        "Celular: (00) 00000-0000\n" +
                        "Email para contato: xxxxx@xxxx.com\n\n" +
                        "Endereço Residencial\n" +
                        "Logradouro: xxxxxxxx\n" +
                        "Estado: xx\n" +
                        "Município: xxxxxxxx\n" +
                        "Bairro: xxxxxxxx\n" +
                        "Número: 0000";

        String alertas =
                "ID #000001                       em 27/02/2025\n" +
                        "— Alerta 1: Difusão redes sociais\n" +
                        "— Alerta 2: Notificação regional";

        // Bind das seções
        sDadosPessoais   = bindSection(R.id.secDadosPessoais, "Dados Pessoais", dadosPessoais);
        sDadosOcorrencia = bindSection(R.id.secDadosOcorrencia, "Dados da Ocorrência", dadosOcorrencia);
        sDadosComunicante = bindSection(R.id.secDadosComunicante, "Dados do Comunicante", dadosComunicante);
        sAlertas          = bindSection(R.id.secAlertasEmitidos, "Alertas Emitidos", alertas);

        // Ações
        findViewById(R.id.btnEditar).setOnClickListener(v ->
                startActivity(new Intent(this, EditCaseActivity.class)));
        findViewById(R.id.btnSalvar).setOnClickListener(v ->
                Toast.makeText(this, "Mock: alterações salvas.", Toast.LENGTH_SHORT).show());
    }

    private SectionHolder bindSection(int includeId, String titulo, String corpoMock) {
        View root = findViewById(includeId);
        TextView tvTitulo = root.findViewById(R.id.tvTitulo);
        ImageView ivArrow = root.findViewById(R.id.ivArrow);
        LinearLayout container = root.findViewById(R.id.container);
        View header = root.findViewById(R.id.header);

        tvTitulo.setText(titulo);

        TextView content = new TextView(this);
        content.setText(corpoMock);
        content.setTextColor(0xFF333333);
        container.addView(content);

        header.setOnClickListener(v -> toggle(container, ivArrow));
        return new SectionHolder(container, ivArrow);
    }

    private void toggle(LinearLayout container, ImageView arrow) {
        boolean expand = container.getVisibility() != View.VISIBLE;
        container.setVisibility(expand ? View.VISIBLE : View.GONE);
        arrow.animate().rotation(expand ? 180f : 0f).setDuration(120).start();
    }

    static class SectionHolder {
        final LinearLayout container;
        final ImageView arrow;
        SectionHolder(LinearLayout c, ImageView a) { container = c; arrow = a; }
    }
}
