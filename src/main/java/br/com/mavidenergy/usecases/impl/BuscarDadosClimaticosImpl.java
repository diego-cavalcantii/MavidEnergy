package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.gateways.responses.DadosClimaticosResponseDTO;
import br.com.mavidenergy.usecases.interfaces.BuscarDadosClimaticos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuscarDadosClimaticosImpl implements BuscarDadosClimaticos {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> buscarPorCidade(Cidade cidade) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&lang=pt",
                cidade.getLatitude(),
                cidade.getLongitude(),
                "626134ecb5b009e6383d175d3fc17150" // Substitua pela sua chave real
        );


        // Faz a requisição para a API
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        return response;

    }
}
