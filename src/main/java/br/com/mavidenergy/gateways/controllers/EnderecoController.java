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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Adiciona um novo endereço")
    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDTO.class)))
    public ResponseEntity<EnderecoResponseDTO> adicionarEndereco(@RequestBody EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO enderecoResponseDTO = adicionarEndereco.executa(enderecoRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(enderecoResponseDTO);
    }

    @GetMapping("/{enderecoId}")
    @Operation(summary = "Busca um endereço por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content)
    })
    @Parameter(description = "ID do endereço que será recuperado", required = true)
        public ResponseEntity<EnderecoResponseDTO> buscarEnderecoPorId(@PathVariable String enderecoId) {
        Endereco endereco = buscarEndereco.buscarPorId(enderecoId);

        EnderecoResponseDTO enderecoResponseDTO = converteEnderecoEmDTO.executa(endereco);

        return ResponseEntity.ok(enderecoResponseDTO);
    }

    @GetMapping("/pessoa/{pessoaId}")
    @Operation(summary = "Lista todos os endereços de uma pessoa")
    @ApiResponse(responseCode = "200", description = "Endereços listados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDTO[].class)))
    @ApiResponse(responseCode = "404", description = "Endereços não encontrados", content = @Content)
    @Parameter(description = "ID da pessoa que será recuperada", required = true)
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
    @Operation(summary = "Atualiza um endereço existente")
    @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnderecoResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content)
    @Parameter(description = "ID do endereço que seja alterado", required = true)
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
    @Operation(summary = "Deleta um endereço")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Endereço deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content)
    })
    @Parameter(description = "ID do endereço que seja excluido", required = true)
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
