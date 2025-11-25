package com.example.reconhecimentofacial;

import com.google.firebase.firestore.PropertyName;

public class Alerta {
    private String id;

    // Campos com nomes exatos do Firebase
    private String porcentagem;      // Firebase: "porcentagem"
    private String localizacao;      // Firebase: "localizacao" (já estava certo)
    private String dataEmissao;      // Firebase: "dataEmissao"
    private String vitimaNome;       // Firebase: "vitimaNome"
    private int vitimaIdade;         // Firebase: "vitimaIdade"
    private String status;           // Firebase: "status"
    private String fotoDesaparecido; // Firebase: "fotoDesaparecido"
    private String desaparecidoID;   // Firebase: "desaparecidoID"

    public Alerta() {} // Construtor vazio obrigatório

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // --- Mapeamento Correto ---

    public String getPorcentagem() { return porcentagem; }
    public void setPorcentagem(Object porcentagem) {
        // Aceita String ou Number e converte para String
        this.porcentagem = String.valueOf(porcentagem);
    }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(String dataEmissao) { this.dataEmissao = dataEmissao; }

    public String getVitimaNome() { return vitimaNome; }
    public void setVitimaNome(String vitimaNome) { this.vitimaNome = vitimaNome; }

    public int getVitimaIdade() { return vitimaIdade; }
    public void setVitimaIdade(int vitimaIdade) { this.vitimaIdade = vitimaIdade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFotoDesaparecido() { return fotoDesaparecido; }
    public void setFotoDesaparecido(String fotoDesaparecido) { this.fotoDesaparecido = fotoDesaparecido; }

    public String getDesaparecidoID() { return desaparecidoID; }
    public void setDesaparecidoID(String desaparecidoID) { this.desaparecidoID = desaparecidoID; }
}