package com.example.reconhecimentofacial;

public class CasoReal {
    private String nome;
    private String fotoUrl;
    private String cidadeEstado;
    private String dataDesaparecimento;
    private String fonteUrl;

    public CasoReal() {} // Construtor vazio obrigatório para o Firebase

    // Getters e Setters para todos os campos
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public String getCidadeEstado() { return cidadeEstado; }
    public void setCidadeEstado(String cidadeEstado) { this.cidadeEstado = cidadeEstado; }
    public String getDataDesaparecimento() { return dataDesaparecimento; }
    public void setDataDesaparecimento(String dataDesaparecimento) { this.dataDesaparecimento = dataDesaparecimento; }
    public String getFonteUrl() { return fonteUrl; }
    public void setFonteUrl(String fonteUrl) { this.fonteUrl = fonteUrl; }
}