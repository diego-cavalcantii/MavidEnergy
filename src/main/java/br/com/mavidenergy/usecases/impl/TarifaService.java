package br.com.mavidenergy.usecases.impl;


import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TarifaService {

    public Map<String, Object> calcularTarifa(double consumoKwh, double tarifaKwh, String bandeira, double icmsPercent, double pisPercent, double cofinsPercent, double taxaDistribuicao, double taxaEnergia) {
        double valorEnergia = consumoKwh * tarifaKwh;
        double valorBandeira = 0;
        String corBandeira = "Verde";

        switch (bandeira.toLowerCase()) {
            case "verde":
                valorBandeira = 0;
                corBandeira = "Verde";
                break;
            case "amarela":
                valorBandeira = 0.02 * consumoKwh;
                corBandeira = "Amarela";
                break;
            case "vermelha":
                valorBandeira = 0.05 * consumoKwh;
                corBandeira = "Vermelha";
                break;
            default:
                throw new IllegalArgumentException("Bandeira tarifária inválida");
        }

        double impostoIcms = valorEnergia * (icmsPercent / 100);
        double impostoPis = valorEnergia * (pisPercent / 100);
        double impostoCofins = valorEnergia * (cofinsPercent / 100);
        double valorDistribuicao = consumoKwh * taxaDistribuicao;
        double valorEnergiaTotal = consumoKwh * taxaEnergia;

        double valorSemDesconto = valorEnergia + valorBandeira + impostoIcms + impostoPis + impostoCofins + valorDistribuicao + valorEnergiaTotal;

        Map<String, Object> results = new HashMap<>();
        results.put("valorSemDesconto", Math.round(valorSemDesconto * 100.0) / 100.0);
        results.put("corBandeira", corBandeira);
        return results;
    }

    public Map<String, Double> calcularEconomia(double valorSemDesconto, double percentualEconomia) {
        double economia = valorSemDesconto * (percentualEconomia / 100);
        double valorComDesconto = valorSemDesconto - economia;
        Map<String, Double> economiaResultados = new HashMap<>();
        economiaResultados.put("economia", Math.round(economia * 100.0) / 100.0);
        economiaResultados.put("valorComDesconto", Math.round(valorComDesconto * 100.0) / 100.0);
        return economiaResultados;
    }
}