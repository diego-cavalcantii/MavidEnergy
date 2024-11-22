package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.exceptions.ConsultaNotFoundException;
import br.com.mavidenergy.gateways.repositories.ConsultaRepository;
import br.com.mavidenergy.gateways.requests.ConsultaRequestDTO;
import br.com.mavidenergy.gateways.responses.ConsultaResponseDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.impl.TarifaService;
import br.com.mavidenergy.usecases.interfaces.BuscarConsulta;
import br.com.mavidenergy.usecases.interfaces.BuscarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
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


    @GetMapping("/{consultaId}")
    public ResponseEntity<ConsultaResponseDTO> exibiUmaConsulta(@PathVariable String consultaId){
        Consulta consulta = buscarConsulta.buscarPorId(consultaId);

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

            List<Consulta> consultas = consultaRepository.findAll();

            for (Consulta consulta : consultas) {
                if (consulta.getEndereco().getEnderecoId().equals(consultaRequestDTO.getEnderecoId())) {
                    throw new IllegalArgumentException("Endereço já possui uma consulta");
                }
            }

            Pessoa pessoa = buscarPessoa.buscarPorId(consultaRequestDTO.getPessoaId());
            if (pessoa == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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
