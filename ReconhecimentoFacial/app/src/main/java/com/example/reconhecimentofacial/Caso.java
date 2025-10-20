package com.example.reconhecimentofacial;

import com.google.firebase.firestore.Exclude;
import java.util.List;
import java.util.Map;

public class Caso {

    @Exclude
    private String id;

    // --- CORREÇÃO APLICADA AQUI ---
    // Variáveis são String para aceitar tanto números quanto textos do banco.
    private String idadeEpoca;
    private String numero;
    private String numeroBO;
    private String numero_desaparecimento;

    // Campos restantes...
    private double altura;
    private String bairro;
    private String bairro_desaparecimento;
    private String corCabelo;
    private String corOlhos;
    private String corPele;
    private String cpf;
    private String dataDesaparecimento;
    private String dataNascimento;
    private String dataRegistro;
    private String delegaciaRegistro;
    private boolean dirigia_veiculo;
    private boolean doenca_mental;
    private List<Double> embedding;
    private String estado;
    private String estado_desaparecimento;
    private List<String> fotos;
    private String id_comunicante;
    private String localDesaparecimento;
    private String logradouro;
    private String logradouro_desaparecimento;
    private String modelo_veiculo;
    private String municipio;
    private String municipio_desaparecimento;
    private String nomeCompleto;
    private String nomeMae;
    private String nomePai;
    private String objetos;
    private String orgaoExpedidor;
    private boolean perfil_redes;
    private String placa_veiculo;
    private String rg;
    private String sexo;
    private String sinaisParticulares;
    private String tipoCabelo;
    private boolean usavaTelefone;
    private String vestimenta;

    public Caso() {}

    // --- GETTERS E SETTERS "INTELIGENTES" ---
    // Eles garantem que, não importa o que o Firestore envie, tudo será convertido para String.

    @Exclude
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // SETTERS que aceitam qualquer tipo de objeto e convertem para String
    public void setIdadeEpoca(Object idadeEpoca) {
        if (idadeEpoca != null) this.idadeEpoca = String.valueOf(idadeEpoca);
    }
    public void setNumero(Object numero) {
        if (numero != null) this.numero = String.valueOf(numero);
    }
    public void setNumeroBO(Object numeroBO) {
        if (numeroBO != null) this.numeroBO = String.valueOf(numeroBO);
    }
    public void setNumero_desaparecimento(Object numero_desaparecimento) {
        if (numero_desaparecimento != null) this.numero_desaparecimento = String.valueOf(numero_desaparecimento);
    }

    // GETTERS que sempre retornam String
    public String getIdadeEpoca() { return idadeEpoca; }
    public String getNumero() { return numero; }
    public String getNumeroBO() { return numeroBO; }
    public String getNumero_desaparecimento() { return numero_desaparecimento; }

    // O resto dos getters e setters...
    public double getAltura() { return altura; }
    public void setAltura(double altura) { this.altura = altura; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getBairro_desaparecimento() { return bairro_desaparecimento; }
    public void setBairro_desaparecimento(String bairro_desaparecimento) { this.bairro_desaparecimento = bairro_desaparecimento; }
    public String getCorCabelo() { return corCabelo; }
    public void setCorCabelo(String corCabelo) { this.corCabelo = corCabelo; }
    public String getCorOlhos() { return corOlhos; }
    public void setCorOlhos(String corOlhos) { this.corOlhos = corOlhos; }
    public String getCorPele() { return corPele; }
    public void setCorPele(String corPele) { this.corPele = corPele; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getDataDesaparecimento() { return dataDesaparecimento; }
    public void setDataDesaparecimento(String dataDesaparecimento) { this.dataDesaparecimento = dataDesaparecimento; }
    public String getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(String dataRegistro) { this.dataRegistro = dataRegistro; }
    public String getDelegaciaRegistro() { return delegaciaRegistro; }
    public void setDelegaciaRegistro(String delegaciaRegistro) { this.delegaciaRegistro = delegaciaRegistro; }
    public boolean isDirigia_veiculo() { return dirigia_veiculo; }
    public void setDirigia_veiculo(boolean dirigia_veiculo) { this.dirigia_veiculo = dirigia_veiculo; }
    public boolean isDoenca_mental() { return doenca_mental; }
    public void setDoenca_mental(boolean doenca_mental) { this.doenca_mental = doenca_mental; }
    public List<Double> getEmbedding() { return embedding; }
    public void setEmbedding(List<Double> embedding) { this.embedding = embedding; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getEstado_desaparecimento() { return estado_desaparecimento; }
    public void setEstado_desaparecimento(String estado_desaparecimento) { this.estado_desaparecimento = estado_desaparecimento; }
    public List<String> getFotos() { return fotos; }
    public void setFotos(List<String> fotos) { this.fotos = fotos; }
    public String getId_comunicante() { return id_comunicante; }
    public void setId_comunicante(String id_comunicante) { this.id_comunicante = id_comunicante; }
    public String getLocalDesaparecimento() { return localDesaparecimento; }
    public void setLocalDesaparecimento(String localDesaparecimento) { this.localDesaparecimento = localDesaparecimento; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getLogradouro_desaparecimento() { return logradouro_desaparecimento; }
    public void setLogradouro_desaparecimento(String logradouro_desaparecimento) { this.logradouro_desaparecimento = logradouro_desaparecimento; }
    public String getModelo_veiculo() { return modelo_veiculo; }
    public void setModelo_veiculo(String modelo_veiculo) { this.modelo_veiculo = modelo_veiculo; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public String getMunicipio_desaparecimento() { return municipio_desaparecimento; }
    public void setMunicipio_desaparecimento(String municipio_desaparecimento) { this.municipio_desaparecimento = municipio_desaparecimento; }
    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public String getNomeMae() { return nomeMae; }
    public void setNomeMae(String nomeMae) { this.nomeMae = nomeMae; }
    public String getNomePai() { return nomePai; }
    public void setNomePai(String nomePai) { this.nomePai = nomePai; }
    public String getObjetos() { return objetos; }
    public void setObjetos(String objetos) { this.objetos = objetos; }
    public String getOrgaoExpedidor() { return orgaoExpedidor; }
    public void setOrgaoExpedidor(String orgaoExpedidor) { this.orgaoExpedidor = orgaoExpedidor; }
    public boolean isPerfil_redes() { return perfil_redes; }
    public void setPerfil_redes(boolean perfil_redes) { this.perfil_redes = perfil_redes; }
    public String getPlaca_veiculo() { return placa_veiculo; }
    public void setPlaca_veiculo(String placa_veiculo) { this.placa_veiculo = placa_veiculo; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getSinaisParticulares() { return sinaisParticulares; }
    public void setSinaisParticulares(String sinaisParticulares) { this.sinaisParticulares = sinaisParticulares; }
    public String getTipoCabelo() { return tipoCabelo; }
    public void setTipoCabelo(String tipoCabelo) { this.tipoCabelo = tipoCabelo; }
    public boolean isUsavaTelefone() { return usavaTelefone; }
    public void setUsavaTelefone(boolean usavaTelefone) { this.usavaTelefone = usavaTelefone; }
    public String getVestimenta() { return vestimenta; }
    public void setVestimenta(String vestimenta) { this.vestimenta = vestimenta; }
}