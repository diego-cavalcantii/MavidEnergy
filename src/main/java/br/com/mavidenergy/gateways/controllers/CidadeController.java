package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.domains.ErrorResponseDTO;
import br.com.mavidenergy.gateways.responses.CidadeResponseDTO;
import br.com.mavidenergy.gateways.responses.ConsultaResponseDTO;
import br.com.mavidenergy.gateways.responses.DadosClimaticosResponseDTO;
import br.com.mavidenergy.usecases.interfaces.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Busca dados climáticos por cidade")
    @ApiResponse(responseCode = "200", description = "Cidades encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CidadeResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cidades não encontradas", content = @Content)
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    public ResponseEntity<List<CidadeResponseDTO>> exibiTodasAsCiDades() {
        List<Cidade> cidades = buscarCidade.buscarTodos();

        List<CidadeResponseDTO> cidadeResponseDTOS = cidades.stream().map(cidade ->
                CidadeResponseDTO.builder()
                        .cidadeId(cidade.getCidadeId())
                        .nomeCidade(cidade.getNomeCidade())
                        .nomeEstado(cidade.getNomeEstado())
                        .siglaEstado(cidade.getSiglaEstado())
                        .build()).toList();

        return ResponseEntity.ok(cidadeResponseDTOS);
    }

    @GetMapping("/{cidadeId}/dados-climaticos")
    @Operation(summary = "Busca dados climáticos por cidade")
    @ApiResponse(responseCode = "200", description = "Dados Climaticos Encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DadosClimaticosResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content)
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    @Parameter(name = "cidadeId", description = "ID da cidade para a qual os dados climáticos são buscados", required = true)
    public ResponseEntity<DadosClimaticosResponseDTO> buscarDadoClimaticoPorCidade(@PathVariable String cidadeId) {
        Cidade cidade = buscarCidade.buscarPorId(cidadeId);

        Map<String, Object> response = buscarDadosClimaticos.buscarPorCidade(cidade);
        DadosClimaticosResponseDTO dadosClimaticosResponseDTO = converterDadosClimaticos.converter(response, cidade);

        return ResponseEntity.ok(dadosClimaticosResponseDTO);
    }


    @GetMapping("/{cidadeId}")
    @Operation(summary = "Busca cidade por ID")
    @ApiResponse(responseCode = "200", description = "Cidade encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CidadeResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Cidade não encontrada", content = @Content)
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    @Parameter(name = "cidadeId", description = "ID da cidade que está sendo buscada", required = true)
    public ResponseEntity<CidadeResponseDTO> buscarCidadePorId(@PathVariable  String cidadeId) {
        Cidade cidade = buscarCidade.buscarPorId(cidadeId);

        CidadeResponseDTO cidadeResponseDTO = CidadeResponseDTO.builder()
                .cidadeId(cidade.getCidadeId())
                .nomeCidade(cidade.getNomeCidade())
                .nomeEstado(cidade.getNomeEstado())
                .siglaEstado(cidade.getSiglaEstado())
                .build();

        return ResponseEntity.ok(cidadeResponseDTO);
    }

}
