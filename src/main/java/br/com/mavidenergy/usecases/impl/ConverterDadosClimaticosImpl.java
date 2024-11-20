package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.gateways.responses.DadosClimaticosResponseDTO;
import br.com.mavidenergy.usecases.interfaces.BuscarDadosClimaticos;
import br.com.mavidenergy.usecases.interfaces.ConverteDirecaoVento;
import br.com.mavidenergy.usecases.interfaces.ConverterDadosClimaticos;
import br.com.mavidenergy.usecases.interfaces.ConverterTimesTampParaHora;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConverterDadosClimaticosImpl implements ConverterDadosClimaticos {

    private final ConverterTimesTampParaHora converterTimesTampParaHora;
    private final ConverteDirecaoVento converteDirecaoVento;

    @Override
    public DadosClimaticosResponseDTO converter(Map<String, Object> dadosClimaticos, Cidade cidade) {

        Map<String, Object> response = dadosClimaticos; // Resposta da API


        // Extrai as informações do JSON retornado
        Map<String, Object> weather = ((List<Map<String, Object>>) response.get("weather")).get(0); // Clima
        Map<String, Object> main = (Map<String, Object>) response.get("main"); // Temperatura, umidade, etc.
        Map<String, Object> wind = (Map<String, Object>) response.get("wind"); // Vento
        Map<String, Object> clouds = (Map<String, Object>) response.get("clouds"); // Cobertura de nuvens
        Map<String, Object> sys = (Map<String, Object>) response.get("sys"); // Nascer e pôr do sol

        // Cria o objeto DadosClimaticosResponseDTO
        DadosClimaticosResponseDTO dadosClimaticosResponseDTO = DadosClimaticosResponseDTO.builder()
                .clima(weather.get("description").toString()) // Descrição do clima
                .temperatura(String.format("%.1f°C", (Double.parseDouble(main.get("temp").toString()) - 273.15))) // Temperatura
                .sensacaoTermica(String.format("%.1f°C", (Double.parseDouble(main.get("feels_like").toString()) - 273.15))) // Sensação térmica
                .umidade(main.get("humidity").toString() + "%") // Umidade
                .velocidadeVento(wind.get("speed").toString() + " m/s") // Velocidade do vento
                .direcaoVento(converteDirecaoVento.converter(Integer.parseInt(wind.get("deg").toString()))) // Direção do vento
                .coberturaNuvens(clouds.get("all").toString() + "%") // Cobertura de nuvens
                .nascerDoSol(converterTimesTampParaHora.converter(Long.parseLong(sys.get("sunrise").toString()))) // Nascer do sol
                .porDoSol(converterTimesTampParaHora.converter(Long.parseLong(sys.get("sunset").toString()))) // Pôr do sol
                .nomeCidade(cidade.getNomeCidade()) // Nome da cidade
                .nomeEstado(cidade.getNomeEstado()) // Nome do estado
                .build();

        return dadosClimaticosResponseDTO;
    }
}
