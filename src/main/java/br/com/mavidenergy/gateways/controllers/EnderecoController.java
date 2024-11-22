package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.repositories.ConsultaRepository;
import br.com.mavidenergy.gateways.repositories.EnderecoRepository;
import br.com.mavidenergy.gateways.requests.EnderecoRequestDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final AdicionarEndereco adicionarEndereco;
    private final BuscarPessoa buscarPessoa;
    private final BuscarEndereco buscarEndereco;
    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;
    private final EnderecoRepository enderecoRepository;
    private final BuscarCidade buscarCidade;
    private final ConsultaRepository consultaRepository;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> adicionarEndereco(@RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO enderecoResponseDTO = adicionarEndereco.executa(enderecoRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoResponseDTO);
    }

    @GetMapping("/{enderecoId}")
    public ResponseEntity<EnderecoResponseDTO> buscarEnderecoPorId(@PathVariable String enderecoId) {
        Endereco endereco = buscarEndereco.buscarPorId(enderecoId);

        EnderecoResponseDTO enderecoResponseDTO = converteEnderecoEmDTO.executa(endereco);

        return ResponseEntity.ok(enderecoResponseDTO);
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<EnderecoResponseDTO>> buscarEnderecoPorPessoa(@PathVariable String pessoaId) {
        Pessoa pessoa = buscarPessoa.buscarPorId(pessoaId);

        List<EnderecoResponseDTO> enderecos = pessoa.getEnderecos().stream().map(endereco ->
                EnderecoResponseDTO.builder()
                        .enderecoId(endereco.getEnderecoId())
                        .cep(endereco.getCep())
                        .logradouro(endereco.getLogradouro())
                        .numero(endereco.getNumero())
                        .nomeCidade(endereco.getCidade().getNomeCidade())
                        .nomeEstado(endereco.getCidade().getNomeEstado())
                        .siglaEstado(endereco.getCidade().getSiglaEstado())
                        .build())
                .toList();

        return ResponseEntity.ok(enderecos);

    }

    @PutMapping("/{enderecoId}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(@PathVariable String enderecoId, @RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        Endereco endereco = buscarEndereco.buscarPorId(enderecoId);
        Cidade cidade = buscarCidade.buscarPorId(enderecoRequestDTO.getCidadeId());
        endereco.setCep(enderecoRequestDTO.getCep());
        endereco.setLogradouro(enderecoRequestDTO.getLogradouro());
        endereco.setNumero(enderecoRequestDTO.getNumero());
        endereco.setLatitude(enderecoRequestDTO.getLatitude());
        endereco.setLongitude(enderecoRequestDTO.getLongitude());
        endereco.setCidade(cidade);
        enderecoRepository.save(endereco);
        EnderecoResponseDTO enderecoResponseDTO = converteEnderecoEmDTO.executa(endereco);
        return ResponseEntity.ok(enderecoResponseDTO);
    }


    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<String> deletarEndereco(@PathVariable String enderecoId) {
        Endereco endereco = buscarEndereco.buscarPorId(enderecoId);

        List<Consulta> consultas = consultaRepository.findConsultasByEndereco(endereco);

        if (!consultas.isEmpty()) {
            consultaRepository.deleteAll(consultas);
        }

        enderecoRepository.delete(endereco);

        return ResponseEntity.ok("Endereço deletado com sucesso");
    }


}
