package com.example.reconhecimentofacial;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * Classe final para armazenar todos os dados estáticos (hardcoded)
 * extraídos do painel do SINESP.
 */
public final class DadosEstatisticos {

    // Previne que esta classe seja instanciada
    private DadosEstatisticos() {}

    // --- DECLARAÇÃO DOS DADOS DE PESSOAS DESAPARECIDAS ---
    public static final EstatisticasAnuais dados2020;
    public static final EstatisticasAnuais dados2021;
    public static final EstatisticasAnuais dados2022;
    public static final EstatisticasAnuais dados2023;
    public static final EstatisticasAnuais dados2024;
    public static final EstatisticasAnuais dados2025;
    public static final List<EstatisticasAnuais> todosOsDados;

    // --- DECLARAÇÃO DOS DADOS DE PESSOAS LOCALIZADAS ---
    public static final EstatisticasAnuais dadosLocalizados2020;
    public static final EstatisticasAnuais dadosLocalizados2021;
    public static final EstatisticasAnuais dadosLocalizados2022;
    public static final EstatisticasAnuais dadosLocalizados2023;
    public static final EstatisticasAnuais dadosLocalizados2024;
    public static final EstatisticasAnuais dadosLocalizados2025;
    public static final List<EstatisticasAnuais> todosOsDadosLocalizados;


    // --- BLOCO DE INICIALIZAÇÃO ESTÁTICA ---
    static {

        // ========================================================
        // === DADOS DE PESSOAS DESAPARECIDAS (JÁ EXISTIAM) ===
        // ========================================================

        // --- DADOS 2020 ---
        Map<String, Integer> sexo2020 = new HashMap<>();
        sexo2020.put("Masculino", 39237);
        sexo2020.put("Feminino", 23532);
        sexo2020.put("Não Informado", 382);
        Map<String, Integer> faixa2020 = new HashMap<>();
        faixa2020.put("+18 anos", 41984);
        faixa2020.put("0 a 17 anos", 19365);
        faixa2020.put("N/I", 1802);
        Map<String, Integer> mes2020 = new HashMap<>();
        mes2020.put("Janeiro", 7002); mes2020.put("Fevereiro", 6687); mes2020.put("Março", 5656);
        mes2020.put("Abril", 3605); mes2020.put("Maio", 3938); mes2020.put("Junho", 4289);
        mes2020.put("Julho", 4569); mes2020.put("Agosto", 4930); mes2020.put("Setembro", 5312);
        mes2020.put("Outubro", 5590); mes2020.put("Novembro", 5788); mes2020.put("Dezembro", 5785);
        Map<String, Integer> uf2020 = new HashMap<>();
        uf2020.put("AC", 196); uf2020.put("AL", 339); uf2020.put("AM", 763); uf2020.put("AP", 95);
        uf2020.put("BA", 1478); uf2020.put("CE", 1723); uf2020.put("DF", 2050); uf2020.put("ES", 1804);
        uf2020.put("GO", 2557); uf2020.put("MA", 671); uf2020.put("MG", 6863); uf2020.put("MS", 295);
        uf2020.put("MT", 1720); uf2020.put("PA", 592); uf2020.put("PB", 375); uf2020.put("PE", 2586);
        uf2020.put("PI", 105); uf2020.put("PR", 5411); uf2020.put("RJ", 3350); uf2020.put("RN", 244);
        uf2020.put("RO", 632); uf2020.put("RR", 93); uf2020.put("RS", 6439); uf2020.put("SC", 3410);
        uf2020.put("SE", 334); uf2020.put("SP", 18834); uf2020.put("TO", 192);
        dados2020 = new EstatisticasAnuais(2020, 63151, sexo2020, faixa2020, mes2020, uf2020);

        // --- DADOS 2021 ---
        Map<String, Integer> sexo2021 = new HashMap<>();
        sexo2021.put("Masculino", 43088);
        sexo2021.put("Feminino", 23740);
        sexo2021.put("Não Informado", 534);
        Map<String, Integer> faixa2021 = new HashMap<>();
        faixa2021.put("+18 anos", 46703);
        faixa2021.put("0 a 17 anos", 18633);
        faixa2021.put("N/I", 2026);
        Map<String, Integer> mes2021 = new HashMap<>();
        mes2021.put("Janeiro", 6178); mes2021.put("Fevereiro", 5372); mes2021.put("Março", 5431);
        mes2021.put("Abril", 5037); mes2021.put("Maio", 5380); mes2021.put("Junho", 5217);
        mes2021.put("Julho", 5204); mes2021.put("Agosto", 5632); mes2021.put("Setembro", 5688);
        mes2021.put("Outubro", 5977); mes2021.put("Novembro", 5871); mes2021.put("Dezembro", 6375);
        Map<String, Integer> uf2021 = new HashMap<>();
        uf2021.put("AC", 316); uf2021.put("AL", 427); uf2021.put("AM", 620); uf2021.put("AP", 213);
        uf2021.put("BA", 1526); uf2021.put("CE", 1883); uf2021.put("DF", 2146); uf2021.put("ES", 2081);
        uf2021.put("GO", 2921); uf2021.put("MA", 976); uf2021.put("MG", 6849); uf2021.put("MS", 348);
        uf2021.put("MT", 1924); uf2021.put("PA", 854); uf2021.put("PB", 509); uf2021.put("PE", 2486);
        uf2021.put("PI", 336); uf2021.put("PR", 5712); uf2021.put("RJ", 4043); uf2021.put("RN", 456);
        uf2021.put("RO", 948); uf2021.put("RR", 251); uf2021.put("RS", 6672); uf2021.put("SC", 3750);
        uf2021.put("SE", 367); uf2021.put("SP", 18399); uf2021.put("TO", 349);
        dados2021 = new EstatisticasAnuais(2021, 67362, sexo2021, faixa2021, mes2021, uf2021);

        // --- DADOS 2022 ---
        Map<String, Integer> sexo2022 = new HashMap<>();
        sexo2022.put("Masculino", 49933);
        sexo2022.put("Feminino", 26268);
        sexo2022.put("Não Informado", 567);
        Map<String, Integer> faixa2022 = new HashMap<>();
        faixa2022.put("+18 anos", 53098);
        faixa2022.put("0 a 17 anos", 21786);
        faixa2022.put("N/I", 1884);
        Map<String, Integer> mes2022 = new HashMap<>();
        mes2022.put("Janeiro", 6336); mes2022.put("Fevereiro", 5780); mes2022.put("Março", 6534);
        mes2022.put("Abril", 6061); mes2022.put("Maio", 6183); mes2022.put("Junho", 6158);
        mes2022.put("Julho", 6262); mes2022.put("Agosto", 6517); mes2022.put("Setembro", 6491);
        mes2022.put("Outubro", 7028); mes2022.put("Novembro", 6950); mes2022.put("Dezembro", 6468);
        Map<String, Integer> uf2022 = new HashMap<>();
        uf2022.put("AC", 380); uf2022.put("AL", 627); uf2022.put("AM", 852); uf2022.put("AP", 321);
        uf2022.put("BA", 3332); uf2022.put("CE", 2217); uf2022.put("DF", 2400); uf2022.put("ES", 2331);
        uf2022.put("GO", 3334); uf2022.put("MA", 842); uf2022.put("MG", 6790); uf2022.put("MS", 420);
        uf2022.put("MT", 2186); uf2022.put("PA", 952); uf2022.put("PB", 529); uf2022.put("PE", 2699);
        uf2022.put("PI", 461); uf2022.put("PR", 5778); uf2022.put("RJ", 5255); uf2022.put("RN", 621);
        uf2022.put("RO", 736); uf2022.put("RR", 499); uf2022.put("RS", 7140); uf2022.put("SC", 4010);
        uf2022.put("SE", 625); uf2022.put("SP", 20806); uf2022.put("TO", 525);
        dados2022 = new EstatisticasAnuais(2022, 76768, sexo2022, faixa2022, mes2022, uf2022);

        // --- DADOS 2023 ---
        Map<String, Integer> sexo2023 = new HashMap<>();
        sexo2023.put("Masculino", 50496);
        sexo2023.put("Feminino", 26860);
        sexo2023.put("Não Informado", 749);
        Map<String, Integer> faixa2023 = new HashMap<>();
        faixa2023.put("+18 anos", 55572);
        faixa2023.put("0 a 17 anos", 20445);
        faixa2023.put("N/I", 2088);
        Map<String, Integer> mes2023 = new HashMap<>();
        mes2023.put("Janeiro", 6721); mes2023.put("Fevereiro", 5961); mes2023.put("Março", 6862);
        mes2023.put("Abril", 6309); mes2023.put("Maio", 6420); mes2023.put("Junho", 6122);
        mes2023.put("Julho", 6187); mes2023.put("Agosto", 6468); mes2023.put("Setembro", 6494);
        mes2023.put("Outubro", 6578); mes2023.put("Novembro", 7258); mes2023.put("Dezembro", 6725);
        Map<String, Integer> uf2023 = new HashMap<>();
        uf2023.put("AC", 439); uf2023.put("AL", 626); uf2023.put("AM", 1108); uf2023.put("AP", 351);
        uf2023.put("BA", 3538); uf2023.put("CE", 2435); uf2023.put("DF", 2793); uf2023.put("ES", 2426);
        uf2023.put("GO", 3438); uf2023.put("MA", 919); uf2023.put("MG", 7016); uf2023.put("MS", 354);
        uf2023.put("MT", 2419); uf2023.put("PA", 1058); uf2023.put("PB", 783); uf2023.put("PE", 2864);
        uf2023.put("PI", 664); uf2023.put("PR", 5670); uf2023.put("RJ", 5615); uf2023.put("RN", 629);
        uf2023.put("RO", 906); uf2023.put("RR", 518); uf2023.put("RS", 7533); uf2023.put("SC", 4155);
        uf2023.put("SE", 629); uf2023.put("SP", 18421); uf2023.put("TO", 578);
        dados2023 = new EstatisticasAnuais(2023, 78105, sexo2023, faixa2023, mes2023, uf2023);

        // --- DADOS 2024 ---
        Map<String, Integer> sexo2024 = new HashMap<>();
        sexo2024.put("Masculino", 52064);
        sexo2024.put("Feminino", 28864);
        sexo2024.put("Não Informado", 585);
        Map<String, Integer> faixa2024 = new HashMap<>();
        faixa2024.put("+18 anos", 57220);
        faixa2024.put("0 a 17 anos", 22109);
        faixa2024.put("N/I", 2184);
        Map<String, Integer> mes2024 = new HashMap<>();
        mes2024.put("Janeiro", 6724); mes2024.put("Fevereiro", 6359); mes2024.put("Março", 6880);
        mes2024.put("Abril", 6881); mes2024.put("Maio", 6724); mes2024.put("Junho", 6540);
        mes2024.put("Julho", 6354); mes2024.put("Agosto", 6919); mes2024.put("Setembro", 6981);
        mes2024.put("Outubro", 7113); mes2024.put("Novembro", 6985); mes2024.put("Dezembro", 7053);
        Map<String, Integer> uf2024 = new HashMap<>();
        uf2024.put("AC", 412); uf2024.put("AL", 686); uf2024.put("AM", 968); uf2024.put("AP", 448);
        uf2024.put("BA", 4066); uf2024.put("CE", 2873); uf2024.put("DF", 2248); uf2024.put("ES", 2618);
        uf2024.put("GO", 3618); uf2024.put("MA", 910); uf2024.put("MG", 6838); uf2024.put("MS", 391);
        uf2024.put("MT", 2271); uf2024.put("PA", 1068); uf2024.put("PB", 886); uf2024.put("PE", 2900);
        uf2024.put("PI", 616); uf2024.put("PR", 5971); uf2024.put("RJ", 6047); uf2024.put("RN", 706);
        uf2024.put("RO", 952); uf2024.put("RR", 528); uf2024.put("RS", 8156); uf2024.put("SC", 4224);
        uf2024.put("SE", 788); uf2024.put("SP", 19966); uf2024.put("TO", 558);
        dados2024 = new EstatisticasAnuais(2024, 81513, sexo2024, faixa2024, mes2024, uf2024);

        // --- DADOS 2025 (Parcial) ---
        Map<String, Integer> sexo2025 = new HashMap<>();
        sexo2025.put("Masculino", 39768);
        sexo2025.put("Feminino", 22007);
        sexo2025.put("Não Informado", 436);
        Map<String, Integer> faixa2025 = new HashMap<>();
        faixa2025.put("+18 anos", 43491);
        faixa2025.put("0 a 17 anos", 17457);
        faixa2025.put("N/I", 1263);
        Map<String, Integer> mes2025 = new HashMap<>();
        mes2025.put("Janeiro", 7019); mes2025.put("Fevereiro", 6493); mes2025.put("Março", 7551);
        mes2025.put("Abril", 6941); mes2025.put("Maio", 6896); mes2025.put("Junho", 6688);
        mes2025.put("Julho", 6834); mes2025.put("Agosto", 7201); mes2025.put("Setembro", 6588);
        Map<String, Integer> uf2025 = new HashMap<>();
        uf2025.put("AC", 290); uf2025.put("AL", 528); uf2025.put("AM", 729); uf2025.put("AP", 296);
        uf2025.put("BA", 2915); uf2025.put("CE", 1919); uf2025.put("DF", 1610); uf2025.put("ES", 1804);
        uf2025.put("GO", 2669); uf2025.put("MA", 837); uf2025.put("MG", 6736); uf2025.put("MS", 299);
        uf2025.put("MT", 1564); uf2025.put("PA", 867); uf2025.put("PB", 688); uf2025.put("PE", 2038);
        uf2025.put("PI", 544); uf2025.put("PR", 4695); uf2025.put("RJ", 4184); uf2025.put("RN", 557);
        uf2025.put("RO", 766); uf2025.put("RR", 428); uf2025.put("RS", 5611); uf2025.put("SC", 3111);
        uf2025.put("SE", 557); uf2025.put("SP", 15510); uf2025.put("TO", 459);
        dados2025 = new EstatisticasAnuais(2025, 62211, sexo2025, faixa2025, mes2025, uf2025);

        todosOsDados = Collections.unmodifiableList(
                Arrays.asList(dados2020, dados2021, dados2022, dados2023, dados2024, dados2025)
        );


        // ===============================================
        // === DADOS DE PESSOAS LOCALIZADAS (NOVOS) ===
        // ===============================================

        // --- DADOS LOCALIZADOS 2020 ---
        Map<String, Integer> sexoLoc2020 = new HashMap<>();
        sexoLoc2020.put("Masculino", 21938);
        sexoLoc2020.put("Feminino", 14232);
        sexoLoc2020.put("Não Informado", 1391);
        Map<String, Integer> faixaLoc2020 = new HashMap<>();
        faixaLoc2020.put("+18 anos", 24277);
        faixaLoc2020.put("0 a 17 anos", 11636);
        faixaLoc2020.put("N/I", 1648);
        Map<String, Integer> mesLoc2020 = new HashMap<>();
        mesLoc2020.put("Janeiro", 3884); mesLoc2020.put("Fevereiro", 3219); mesLoc2020.put("Março", 2205);
        mesLoc2020.put("Abril", 2455); mesLoc2020.put("Maio", 2871); mesLoc2020.put("Junho", 2731);
        mesLoc2020.put("Julho", 2932); mesLoc2020.put("Agosto", 3132); mesLoc2020.put("Setembro", 3305);
        mesLoc2020.put("Outubro", 3265); mesLoc2020.put("Novembro", 3265); mesLoc2020.put("Dezembro", 3162);
        Map<String, Integer> ufLoc2020 = new HashMap<>();
        ufLoc2020.put("AC", 96); ufLoc2020.put("AL", 20); ufLoc2020.put("AM", 613); ufLoc2020.put("AP", 4);
        ufLoc2020.put("BA", 567); ufLoc2020.put("CE", 979); ufLoc2020.put("DF", 1810); ufLoc2020.put("ES", 311);
        ufLoc2020.put("GO", 769); ufLoc2020.put("MA", 71); ufLoc2020.put("MG", 4446); ufLoc2020.put("MS", 855);
        ufLoc2020.put("MT", 1680); ufLoc2020.put("PA", 28); ufLoc2020.put("PB", 149); ufLoc2020.put("PE", 781);
        ufLoc2020.put("PI", 8); ufLoc2020.put("PR", 3509); ufLoc2020.put("RJ", 1332); ufLoc2020.put("RN", 8);
        ufLoc2020.put("RO", 10); ufLoc2020.put("RR", 8); ufLoc2020.put("RS", 6050); ufLoc2020.put("SC", 3849);
        ufLoc2020.put("SE", 212); ufLoc2020.put("SP", 9379); ufLoc2020.put("TO", 15);
        dadosLocalizados2020 = new EstatisticasAnuais(2020, 37561, sexoLoc2020, faixaLoc2020, mesLoc2020, ufLoc2020);

        // --- DADOS LOCALIZADOS 2021 ---
        Map<String, Integer> sexoLoc2021 = new HashMap<>();
        sexoLoc2021.put("Masculino", 23140);
        sexoLoc2021.put("Feminino", 14314);
        sexoLoc2021.put("Não Informado", 1685);
        Map<String, Integer> faixaLoc2021 = new HashMap<>();
        faixaLoc2021.put("+18 anos", 26093);
        faixaLoc2021.put("0 a 17 anos", 11064);
        faixaLoc2021.put("N/I", 1982);
        Map<String, Integer> mesLoc2021 = new HashMap<>();
        mesLoc2021.put("Janeiro", 3482); mesLoc2021.put("Fevereiro", 3158); mesLoc2021.put("Março", 2996);
        mesLoc2021.put("Abril", 2890); mesLoc2021.put("Maio", 3414); mesLoc2021.put("Junho", 3230);
        mesLoc2021.put("Julho", 3115); mesLoc2021.put("Agosto", 3250); mesLoc2021.put("Setembro", 3324);
        mesLoc2021.put("Outubro", 3437); mesLoc2021.put("Novembro", 3281); mesLoc2021.put("Dezembro", 3562);
        Map<String, Integer> ufLoc2021 = new HashMap<>();
        ufLoc2021.put("AC", 227); ufLoc2021.put("AL", 22); ufLoc2021.put("AM", 478); ufLoc2021.put("AP", 8);
        ufLoc2021.put("BA", 463); ufLoc2021.put("CE", 821); ufLoc2021.put("DF", 1722); ufLoc2021.put("ES", 382);
        ufLoc2021.put("GO", 928); ufLoc2021.put("MA", 125); ufLoc2021.put("MG", 4472); ufLoc2021.put("MS", 784);
        ufLoc2021.put("MT", 2101); ufLoc2021.put("PA", 510); ufLoc2021.put("PB", 231); ufLoc2021.put("PE", 788);
        ufLoc2021.put("PI", 15); ufLoc2021.put("PR", 3589); ufLoc2021.put("RJ", 1559); ufLoc2021.put("RN", 6);
        ufLoc2021.put("RO", 25); ufLoc2021.put("RR", 207); ufLoc2021.put("RS", 6497); ufLoc2021.put("SC", 4091);
        ufLoc2021.put("SE", 96); ufLoc2021.put("SP", 8829); ufLoc2021.put("TO", 153);
        dadosLocalizados2021 = new EstatisticasAnuais(2021, 39139, sexoLoc2021, faixaLoc2021, mesLoc2021, ufLoc2021);

        // --- DADOS LOCALIZADOS 2022 ---
        Map<String, Integer> sexoLoc2022 = new HashMap<>();
        sexoLoc2022.put("Masculino", 26464);
        sexoLoc2022.put("Feminino", 16224);
        sexoLoc2022.put("Não Informado", 2003);
        Map<String, Integer> faixaLoc2022 = new HashMap<>();
        faixaLoc2022.put("+18 anos", 29828);
        faixaLoc2022.put("0 a 17 anos", 12489);
        faixaLoc2022.put("N/I", 2374);
        Map<String, Integer> mesLoc2022 = new HashMap<>();
        mesLoc2022.put("Janeiro", 3809); mesLoc2022.put("Fevereiro", 3203); mesLoc2022.put("Março", 3790);
        mesLoc2022.put("Abril", 3530); mesLoc2022.put("Maio", 4322); mesLoc2022.put("Junho", 3552);
        mesLoc2022.put("Julho", 3641); mesLoc2022.put("Agosto", 4097); mesLoc2022.put("Setembro", 3778);
        mesLoc2022.put("Outubro", 3748); mesLoc2022.put("Novembro", 3517); mesLoc2022.put("Dezembro", 3597);
        Map<String, Integer> ufLoc2022 = new HashMap<>();
        ufLoc2022.put("AC", 239); ufLoc2022.put("AL", 69); ufLoc2022.put("AM", 433); ufLoc2022.put("AP", 6);
        ufLoc2022.put("BA", 763); ufLoc2022.put("CE", 1255); ufLoc2022.put("DF", 1541); ufLoc2022.put("ES", 426);
        ufLoc2022.put("GO", 1321); ufLoc2022.put("MA", 106); ufLoc2022.put("MG", 4415); ufLoc2022.put("MS", 1189);
        ufLoc2022.put("MT", 740); ufLoc2022.put("PA", 575); ufLoc2022.put("PB", 187); ufLoc2022.put("PE", 885);
        ufLoc2022.put("PI", 21); ufLoc2022.put("PR", 3753); ufLoc2022.put("RJ", 2042); ufLoc2022.put("RN", 169);
        ufLoc2022.put("RO", 8); ufLoc2022.put("RR", 242); ufLoc2022.put("RS", 7607); ufLoc2022.put("SC", 4294);
        ufLoc2022.put("SE", 579); ufLoc2022.put("SP", 11506); ufLoc2022.put("TO", 320);
        dadosLocalizados2022 = new EstatisticasAnuais(2022, 44691, sexoLoc2022, faixaLoc2022, mesLoc2022, ufLoc2022);

        // --- DADOS LOCALIZADOS 2023 ---
        Map<String, Integer> sexoLoc2023 = new HashMap<>();
        sexoLoc2023.put("Masculino", 31321);
        sexoLoc2023.put("Feminino", 18353);
        sexoLoc2023.put("Não Informado", 2366);
        Map<String, Integer> faixaLoc2023 = new HashMap<>();
        faixaLoc2023.put("+18 anos", 36116);
        faixaLoc2023.put("0 a 17 anos", 13131);
        faixaLoc2023.put("N/I", 2793);
        Map<String, Integer> mesLoc2023 = new HashMap<>();
        mesLoc2023.put("Janeiro", 4763); mesLoc2023.put("Fevereiro", 3962); mesLoc2023.put("Março", 4781);
        mesLoc2023.put("Abril", 4169); mesLoc2023.put("Maio", 4394); mesLoc2023.put("Junho", 4079);
        mesLoc2023.put("Julho", 4339); mesLoc2023.put("Agosto", 4634); mesLoc2023.put("Setembro", 4219);
        mesLoc2023.put("Outubro", 4261); mesLoc2023.put("Novembro", 4208); mesLoc2023.put("Dezembro", 4225);
        Map<String, Integer> ufLoc2023 = new HashMap<>();
        ufLoc2023.put("AC", 280); ufLoc2023.put("AL", 84); ufLoc2023.put("AM", 549); ufLoc2023.put("AP", 137);
        ufLoc2023.put("BA", 791); ufLoc2023.put("CE", 1582); ufLoc2023.put("DF", 1687); ufLoc2023.put("ES", 375);
        ufLoc2023.put("GO", 1535); ufLoc2023.put("MA", 132); ufLoc2023.put("MG", 4915); ufLoc2023.put("MS", 1714);
        ufLoc2023.put("MT", 870); ufLoc2023.put("PA", 624); ufLoc2023.put("PB", 351); ufLoc2023.put("PE", 932);
        ufLoc2023.put("PI", 210); ufLoc2023.put("PR", 3669); ufLoc2023.put("RJ", 2276); ufLoc2023.put("RN", 282);
        ufLoc2023.put("RO", 39); ufLoc2023.put("RR", 237); ufLoc2023.put("RS", 7344); ufLoc2023.put("SC", 4120);
        ufLoc2023.put("SE", 588); ufLoc2023.put("SP", 16412); ufLoc2023.put("TO", 305);
        dadosLocalizados2023 = new EstatisticasAnuais(2023, 52040, sexoLoc2023, faixaLoc2023, mesLoc2023, ufLoc2023);

        // --- DADOS LOCALIZADOS 2024 ---
        Map<String, Integer> sexoLoc2024 = new HashMap<>();
        sexoLoc2024.put("Masculino", 32792);
        sexoLoc2024.put("Feminino", 19975);
        sexoLoc2024.put("Não Informado", 2714);
        Map<String, Integer> faixaLoc2024 = new HashMap<>();
        faixaLoc2024.put("+18 anos", 37998);
        faixaLoc2024.put("0 a 17 anos", 14336);
        faixaLoc2024.put("N/I", 3147);
        Map<String, Integer> mesLoc2024 = new HashMap<>();
        mesLoc2024.put("Janeiro", 4855); mesLoc2024.put("Fevereiro", 4309); mesLoc2024.put("Março", 4591);
        mesLoc2024.put("Abril", 4900); mesLoc2024.put("Maio", 4614); mesLoc2024.put("Junho", 4651);
        mesLoc2024.put("Julho", 4555); mesLoc2024.put("Agosto", 4676); mesLoc2024.put("Setembro", 4514);
        mesLoc2024.put("Outubro", 4895); mesLoc2024.put("Novembro", 4460); mesLoc2024.put("Dezembro", 4459);
        Map<String, Integer> ufLoc2024 = new HashMap<>();
        ufLoc2024.put("AC", 372); ufLoc2024.put("AL", 73); ufLoc2024.put("AM", 584); ufLoc2024.put("AP", 222);
        ufLoc2024.put("BA", 876); ufLoc2024.put("CE", 1784); ufLoc2024.put("DF", 2147); ufLoc2024.put("ES", 452);
        ufLoc2024.put("GO", 1857); ufLoc2024.put("MA", 198); ufLoc2024.put("MG", 5231); ufLoc2024.put("MS", 1582);
        ufLoc2024.put("MT", 966); ufLoc2024.put("PA", 764); ufLoc2024.put("PB", 580); ufLoc2024.put("PE", 982);
        ufLoc2024.put("PI", 264); ufLoc2024.put("PR", 3898); ufLoc2024.put("RJ", 2668); ufLoc2024.put("RN", 359);
        ufLoc2024.put("RO", 44); ufLoc2024.put("RR", 225); ufLoc2024.put("RS", 8039); ufLoc2024.put("SC", 4391);
        ufLoc2024.put("SE", 718); ufLoc2024.put("SP", 15969); ufLoc2024.put("TO", 236);
        dadosLocalizados2024 = new EstatisticasAnuais(2024, 55481, sexoLoc2024, faixaLoc2024, mesLoc2024, ufLoc2024);

        // --- DADOS LOCALIZADOS 2025 (Parcial) ---
        Map<String, Integer> sexoLoc2025 = new HashMap<>();
        sexoLoc2025.put("Masculino", 25053);
        sexoLoc2025.put("Feminino", 15292);
        sexoLoc2025.put("Não Informado", 1900);
        Map<String, Integer> faixaLoc2025 = new HashMap<>();
        faixaLoc2025.put("+18 anos", 28747);
        faixaLoc2025.put("0 a 17 anos", 11389);
        faixaLoc2025.put("N/I", 2109);
        Map<String, Integer> mesLoc2025 = new HashMap<>();
        mesLoc2025.put("Janeiro", 4815); mesLoc2025.put("Fevereiro", 4359); mesLoc2025.put("Março", 4883);
        mesLoc2025.put("Abril", 4468); mesLoc2025.put("Maio", 4767); mesLoc2025.put("Junho", 4494);
        mesLoc2025.put("Julho", 4770); mesLoc2025.put("Agosto", 4926); mesLoc2025.put("Setembro", 4763);
        Map<String, Integer> ufLoc2025 = new HashMap<>();
        ufLoc2025.put("AC", 199); ufLoc2025.put("AL", 250); ufLoc2025.put("AM", 464); ufLoc2025.put("AP", 182);
        ufLoc2025.put("BA", 599); ufLoc2025.put("CE", 1147); ufLoc2025.put("DF", 1535); ufLoc2025.put("ES", 282);
        ufLoc2025.put("GO", 1340); ufLoc2025.put("MA", 183); ufLoc2025.put("MG", 5065); ufLoc2025.put("MS", 819);
        ufLoc2025.put("MT", 872); ufLoc2025.put("PA", 562); ufLoc2025.put("PB", 610); ufLoc2025.put("PE", 740);
        ufLoc2025.put("PI", 263); ufLoc2025.put("PR", 3267); ufLoc2025.put("RJ", 1799); ufLoc2025.put("RN", 334);
        ufLoc2025.put("RO", 38); ufLoc2025.put("RR", 196); ufLoc2025.put("RS", 5772); ufLoc2025.put("SC", 3318);
        ufLoc2025.put("SE", 483); ufLoc2025.put("SP", 11735); ufLoc2025.put("TO", 181);
        dadosLocalizados2025 = new EstatisticasAnuais(2025, 42245, sexoLoc2025, faixaLoc2025, mesLoc2025, ufLoc2025);

        todosOsDadosLocalizados = Collections.unmodifiableList(
                Arrays.asList(dadosLocalizados2020, dadosLocalizados2021, dadosLocalizados2022, dadosLocalizados2023, dadosLocalizados2024, dadosLocalizados2025)
        );
    }
}