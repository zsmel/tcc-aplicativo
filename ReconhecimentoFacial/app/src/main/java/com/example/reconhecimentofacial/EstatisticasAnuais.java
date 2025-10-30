package com.example.reconhecimentofacial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POJO (Plain Old Java Object) para armazenar os dados estatísticos de um único ano.
 */
public class EstatisticasAnuais {

    // --- CONSTANTES PARA AGRUPAR REGIÕES ---
    private static final List<String> NORTE = Arrays.asList("AC", "AP", "AM", "PA", "RO", "RR", "TO");
    private static final List<String> NORDESTE = Arrays.asList("AL", "BA", "CE", "MA", "PB", "PE", "PI", "RN", "SE");
    private static final List<String> CENTRO_OESTE = Arrays.asList("DF", "GO", "MT", "MS");
    private static final List<String> SUDESTE = Arrays.asList("ES", "MG", "RJ", "SP");
    private static final List<String> SUL = Arrays.asList("PR", "RS", "SC");

    // --- Atributos ---
    private int ano;
    private int total;
    private Map<String, Integer> porSexo;
    private Map<String, Integer> porFaixaEtaria;
    private Map<String, Integer> porMes;
    private Map<String, Integer> porUf;

    public EstatisticasAnuais(int ano, int total, Map<String, Integer> porSexo,
                              Map<String, Integer> porFaixaEtaria,
                              Map<String, Integer> porMes,
                              Map<String, Integer> porUf) {
        this.ano = ano;
        this.total = total;
        this.porSexo = porSexo;
        this.porFaixaEtaria = porFaixaEtaria;
        this.porMes = porMes;
        this.porUf = porUf;
    }

    // --- Getters para acessar os dados ---
    public int getAno() {
        return ano;
    }

    public int getTotal() {
        return total;
    }

    public Map<String, Integer> getPorSexo() {
        return porSexo;
    }

    public Map<String, Integer> getPorFaixaEtaria() {
        return porFaixaEtaria;
    }

    public Map<String, Integer> getPorMes() {
        return porMes;
    }

    public Map<String, Integer> getPorUf() {
        return porUf;
    }

    /**
     * Calcula e agrupa os totais de UF por Região.
     * @return Um Map com os totais de cada região.
     */
    public Map<String, Integer> getPorRegiao() {
        Map<String, Integer> porRegiao = new HashMap<>();
        // Inicializa o map
        porRegiao.put("Norte", 0);
        porRegiao.put("Nordeste", 0);
        porRegiao.put("Centro-O.", 0);
        porRegiao.put("Sudeste", 0);
        porRegiao.put("Sul", 0);

        // Itera sobre o map de UFs que já temos
        for (Map.Entry<String, Integer> entry : porUf.entrySet()) {
            String uf = entry.getKey();
            int valor = entry.getValue();

            if (NORTE.contains(uf)) {
                porRegiao.put("Norte", porRegiao.get("Norte") + valor);
            } else if (NORDESTE.contains(uf)) {
                porRegiao.put("Nordeste", porRegiao.get("Nordeste") + valor);
            } else if (CENTRO_OESTE.contains(uf)) {
                porRegiao.put("Centro-O.", porRegiao.get("Centro-O.") + valor);
            } else if (SUDESTE.contains(uf)) {
                porRegiao.put("Sudeste", porRegiao.get("Sudeste") + valor);
            } else if (SUL.contains(uf)) {
                porRegiao.put("Sul", porRegiao.get("Sul") + valor);
            }
        }
        return porRegiao;
    }
}