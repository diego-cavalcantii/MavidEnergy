package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.ErrorResponseDTO;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.exceptions.ConsultaNotFoundException;
import br.com.mavidenergy.gateways.repositories.ConsultaRepository;
import br.com.mavidenergy.gateways.repositories.PessoaRepository;
import br.com.mavidenergy.gateways.requests.ConsultaRequestDTO;
import br.com.mavidenergy.gateways.responses.ConsultaResponseDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.impl.TarifaService;
import br.com.mavidenergy.usecases.interfaces.BuscarConsulta;
import br.com.mavidenergy.usecases.interfaces.BuscarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/consulta")
@RequiredArgsConstructor
public class ConsultaController {

    private final BuscarEndereco buscarEndereco;
    private final BuscarConsulta buscarConsulta;
    private final BuscarPessoa buscarPessoa;
    private final TarifaService tarifaService;
    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;
    private final ConsultaRepository consultaRepository;
    private final PessoaRepository pessoaRepository;


    @GetMapping("/{consultaId}")
    @Operation(summary = "Exibe uma consulta específica", description = "Retorna os detalhes de uma consulta específica pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsultaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada", content = @Content)
    })
    @Parameter(description = "ID da consulta que sera encontrada", required = true)
    public ResponseEntity<Object> exibiUmaConsulta(@PathVariable String consultaId) {
        Consulta consulta = buscarConsulta.buscarPorId(consultaId);
        if (consulta == null) {
            throw new ConsultaNotFoundException("Consulta não encontrada");
        }

        ConsultaResponseDTO consultaResponse = ConsultaResponseDTO.builder()
                .bandeira(consulta.getBandeira())
                .valorKwh(consulta.getValorKwh())
                .endereco(converteEnderecoEmDTO.executa(consulta.getEndereco()))
                .EconomiaPotencial(String.valueOf(consulta.getEconomiaPotencial()))
                .ValorComDesconto(String.valueOf(consulta.getValorComDesconto()))
                .ValorSemDesconto(String.valueOf(consulta.getValorSemDesconto()))
                .dataCriacao(consulta.getDataCriacao())
                .build();
        return ResponseEntity.ok(consultaResponse);
    }



    @GetMapping("/pessoa/{pessoaId}")
    @Operation(summary = "Exibe todas as consultas de uma pessoa", description = "Retorna uma lista de todas as consultas associadas a uma pessoa específica, identificada pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsultaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consultas não encontradas", content = @Content)

    })
    @Parameter(description = "ID da pessoa que sera encontrada", required = true)
    public ResponseEntity<List<ConsultaResponseDTO>> exibiTodasAsConsultasDeUmaPessoa(@PathVariable String pessoaId) {

        Pessoa pessoa = buscarPessoa.buscarPorId(pessoaId);

        List<Consulta> consultas = buscarConsulta.buscarPorPessoa(pessoa);

        List<ConsultaResponseDTO> consultasResponse = consultas.stream()
                .map(consulta -> ConsultaResponseDTO.builder()
                        .bandeira(consulta.getBandeira())
                        .valorKwh(consulta.getValorKwh())
                        .endereco(converteEnderecoEmDTO.executa(consulta.getEndereco()))
                        .EconomiaPotencial(String.valueOf(consulta.getEconomiaPotencial()))
                        .ValorComDesconto(String.valueOf(consulta.getValorComDesconto()))
                        .ValorSemDesconto(String.valueOf(consulta.getValorSemDesconto()))
                        .dataCriacao(consulta.getDataCriacao())
                        .build())
                .toList();

        return ResponseEntity.ok(consultasResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Gera uma nova consulta", description = "Cria uma nova consulta com base nas informações fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta criad com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsultaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    public ResponseEntity<ConsultaResponseDTO> gerarConsulta(@Valid @RequestBody ConsultaRequestDTO consultaRequestDTO) {
        // Define fixed values for calculations, adjust as necessary
        double tarifa = 0.60; // R$/kWh
        double icms = 18; // percent
        double pis = 1.65; // percent
        double cofins = 7.6; // percent
        double distribuicao = 0.10; // per kWh
        double energia = 0.05; // per kWh

        try {
            Endereco endereco = buscarEndereco.buscarPorId(consultaRequestDTO.getEnderecoId());
            if (endereco == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Pessoa pessoa = buscarPessoa.buscarPorId(consultaRequestDTO.getPessoaId());
            if (pessoa == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            List<Consulta> consultas = consultaRepository.findAll();
            List<Pessoa> pessoas = buscarPessoa.buscarTodos();

            for (Consulta consulta : consultas) {
                if (consulta.getEndereco().getEnderecoId().equals(consultaRequestDTO.getEnderecoId())) {
                    throw new IllegalArgumentException("Endereço já possui uma consulta");
                }
            }

            for (Pessoa p : pessoas) {
                if (p.getPessoaId().equals(consultaRequestDTO.getPessoaId())) {
                    throw new IllegalArgumentException("Pessoa já possui uma consulta");
                }
            }

            Map<String, Object> tarifaResultados = tarifaService.calcularTarifa(
                    consultaRequestDTO.getValorKwh(),
                    tarifa,
                    consultaRequestDTO.getBandeira(),
                    icms,
                    pis,
                    cofins,
                    distribuicao,
                    energia
            );

            Map<String, Double> valoresEconomia = tarifaService.calcularEconomia(
                    (Double) tarifaResultados.get("valorSemDesconto"),
                    50
            );

            Consulta consulta = Consulta.builder()
                    .bandeira((String) tarifaResultados.get("corBandeira"))
                    .valorKwh(consultaRequestDTO.getValorKwh())
                    .pessoa(pessoa)
                    .endereco(endereco)
                    .ValorComDesconto(valoresEconomia.get("valorComDesconto"))
                    .ValorSemDesconto((Double) tarifaResultados.get("valorSemDesconto"))
                    .EconomiaPotencial(valoresEconomia.get("economia"))
                    .build();
            consultaRepository.save(consulta);

            EnderecoResponseDTO enderecoResponseDTO = converteEnderecoEmDTO.executa(endereco);

            ConsultaResponseDTO consultaResponse = ConsultaResponseDTO.builder()
                    .bandeira((String) tarifaResultados.get("corBandeira"))
                    .valorKwh(consultaRequestDTO.getValorKwh())
                    .endereco(enderecoResponseDTO)
                    .EconomiaPotencial(String.format("%.2f", valoresEconomia.get("economia")))
                    .ValorComDesconto(String.format("%.2f", valoresEconomia.get("valorComDesconto")))
                    .ValorSemDesconto(String.format("%.2f", (Double) tarifaResultados.get("valorSemDesconto")))
                    .dataCriacao(consulta.getDataCriacao())
                    .build();

            Link link = linkTo(ConsultaController.class).slash(consulta.getConsultaId()).withSelfRel();

            consultaResponse.add(link);

            return ResponseEntity.status(HttpStatus.CREATED).body(consultaResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
