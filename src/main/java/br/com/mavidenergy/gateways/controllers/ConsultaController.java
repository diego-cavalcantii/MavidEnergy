package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.repositories.ConsultaRepository;
import br.com.mavidenergy.gateways.requests.ConsultaRequestDTO;
import br.com.mavidenergy.gateways.responses.ConsultaResponseDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.impl.TarifaService;
import br.com.mavidenergy.usecases.interfaces.BuscarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/consulta")
@RequiredArgsConstructor
public class ConsultaController {

    private final BuscarEndereco buscarEndereco;
    private final ConsultaRepository consultaRepository;
    private final BuscarPessoa buscarPessoa;
    private final TarifaService tarifaService;
    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;

    @PostMapping("/teste")
    public ResponseEntity<Endereco> testeApi(@RequestBody ConsultaRequestDTO consultaRequestDTO) {
        Endereco endereco = buscarEndereco.buscarPorId(consultaRequestDTO.getEnderecoId());

        return ResponseEntity.ok(endereco);
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> gerarConsulta(@RequestBody ConsultaRequestDTO consultaRequestDTO) {
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
                    .build();

            return ResponseEntity.ok(consultaResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
