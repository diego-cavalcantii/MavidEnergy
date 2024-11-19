package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.requests.EnderecoRequestDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.AdicionarEndereco;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import lombok.RequiredArgsConstructor;
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


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> adicionarEndereco(@RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO enderecoResponseDTO = adicionarEndereco.executa(enderecoRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoResponseDTO);
    }

    @GetMapping("/{pessoaId}")
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


}
