package com.example.reconhecimentofacial;

import com.google.firebase.firestore.PropertyName;

public class Alerta {

    private String id;
    private String porcentagem;
    private String localizacao;
    private String dataEmissao;
    private String vitimaNome;
    private int vitimaIdade;
    private String status;
    private String fotoDesaparecido;
    private String desaparecidoID;

    public Alerta() {} // Required for Firestore

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPorcentagem() { return porcentagem; }
    public void setPorcentagem(Object porcentagem) {
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
