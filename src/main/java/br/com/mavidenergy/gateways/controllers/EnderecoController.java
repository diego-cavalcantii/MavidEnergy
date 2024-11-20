package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.repositories.EnderecoRepository;
import br.com.mavidenergy.gateways.requests.EnderecoRequestDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.AdicionarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final AdicionarEndereco adicionarEndereco;
    private final BuscarPessoa buscarPessoa;
    private final BuscarEndereco buscarEndereco;
    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;
    private final EnderecoRepository enderecoRepository;


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

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<String> deletarEndereco(@PathVariable String enderecoId) {
        Endereco endereco = buscarEndereco.buscarPorId(enderecoId);

        enderecoRepository.delete(endereco);

        return ResponseEntity.ok("Endereço deletado com sucesso");
    }


}
