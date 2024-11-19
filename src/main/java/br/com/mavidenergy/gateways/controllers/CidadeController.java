package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.gateways.responses.CidadeResponseDTO;
import br.com.mavidenergy.gateways.responses.DadosClimaticosResponseDTO;
import br.com.mavidenergy.usecases.interfaces.*;
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
    private final ConverterTimesTampParaHora converterTimesTampParaHora;
    private final ConverteDirecaoVento converteDirecaoVento;
    private final BuscarDadosClimaticos buscarDadosClimaticos;
    private final ConverterDadosClimaticos converterDadosClimaticos;

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
        Cidade cidade = buscarCidade.buscarPorId(cidadeId);

        Map<String, Object> response = buscarDadosClimaticos.buscarPorCidade(cidade);
        DadosClimaticosResponseDTO dadosClimaticosResponseDTO = converterDadosClimaticos.converter(response, cidade);

        return ResponseEntity.ok(dadosClimaticosResponseDTO);
    }


    @GetMapping("/{cidadeId}")
    public String buscarCidadePorId(@PathVariable  String cidadeId) {
        Cidade cidade = buscarCidade.buscarPorId(cidadeId);

        String mensagem = cidade.getLongitude() + " " + cidade.getLatitude();
        return mensagem;
    }

}
