package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.gateways.responses.CidadeResponseDTO;
import br.com.mavidenergy.gateways.responses.DadosClimaticosResponseDTO;
import br.com.mavidenergy.usecases.interfaces.BuscarCidade;
import br.com.mavidenergy.usecases.interfaces.ConverteDirecaoVento;
import br.com.mavidenergy.usecases.interfaces.ConverterTimesTampParaHora;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cidades")
public class CidadeController {

    private final BuscarCidade buscarCidade;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ConverterTimesTampParaHora converterTimesTampParaHora;
    private final ConverteDirecaoVento converteDirecaoVento;

    @GetMapping
    public ResponseEntity<List<CidadeResponseDTO>> exibiTodasAsCiDades() {
        List<Cidade> cidades = buscarCidade.buscarTodos();

        List<CidadeResponseDTO> cidadeResponseDTOS = cidades.stream().map(cidade ->
                CidadeResponseDTO.builder()
                        .nomeCidade(cidade.getNomeCidade())
                        .nomeEstado(cidade.getNomeEstado())
                        .siglaEstado(cidade.getSiglaEstado())
                        .build()).toList();

        return ResponseEntity.ok(cidadeResponseDTOS);
    }

    @GetMapping("/{cidadeId}/dados-climaticos")
    public ResponseEntity<DadosClimaticosResponseDTO> buscarDadoClimaticoPorCidade(@PathVariable String cidadeId) {
        // Busca a cidade pelo ID
        Cidade cidade = buscarCidade.buscarPorId(cidadeId);

        // Monta a URL da API do OpenWeather
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&lang=pt",
                cidade.getLatitude(),
                cidade.getLongitude(),
                "626134ecb5b009e6383d175d3fc17150" // Substitua pela sua chave real
        );


        // Faz a requisição para a API
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

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

        // Retorna o objeto DTO
        return ResponseEntity.ok(dadosClimaticosResponseDTO);
    }


    @GetMapping("/{cidadeId}")
    public String buscarCidadePorId(@PathVariable  String cidadeId) {
        Cidade cidade = buscarCidade.buscarPorId(cidadeId);

        String mensagem = cidade.getLongitude() + " " + cidade.getLatitude();
        return mensagem;
    }

}
