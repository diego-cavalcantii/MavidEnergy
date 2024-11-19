package br.com.mavidenergy.gateways.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadosClimaticosResponseDTO {

    private String nomeCidade;         // Nome da cidade (e.g., "São Paulo")
    private String nomeEstado;         // Nome do estado (e.g., "São Paulo")
    private String clima;              // Descrição do clima (e.g., "few clouds")
    private String temperatura;        // Temperatura atual (e.g., "22.9°C")
    private String sensacaoTermica;    // Sensação térmica (e.g., "23.4°C")
    private String umidade;            // Umidade relativa do ar (e.g., "84%")
    private String velocidadeVento;    // Velocidade do vento (e.g., "6.17 m/s")
    private String direcaoVento;       // Direção do vento (e.g., "Sul")
    private String coberturaNuvens;    // Percentual de cobertura de nuvens (e.g., "20%")
    private String nascerDoSol;        // Horário do nascer do sol (e.g., "06:30")
    private String porDoSol;           // Horário do pôr do sol (e.g., "18:15")
}
