package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.requests.PessoaUsuarioRequestDTO;
import br.com.mavidenergy.gateways.responses.PessoaResponseDTO;
import br.com.mavidenergy.usecases.impl.ConverterPessoaEmDTO;
import br.com.mavidenergy.usecases.interfaces.AdicionarPessoaComUsuario;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {

    private final AdicionarPessoaComUsuario adicionarPessoaComUsuario;
    private final BuscarPessoa buscarPessoa;
    private final ConverterPessoaEmDTO converterPessoaEmDTO;

    @GetMapping
    @Operation(summary = "Exibe todas as pessoas com usuário", description = "Retorna uma lista de todas as pessoas com seus detalhes de usuário.")
    @ApiResponse(responseCode = "200", description = "Lista de pessoas retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class)))
    public ResponseEntity<List<PessoaResponseDTO>> exibirTodasAsPessoasComUsuario() {
        List<Pessoa> pessoas = buscarPessoa.buscarTodos();

        List<PessoaResponseDTO> pessoasResponseDTO = pessoas.stream()
                .map(converterPessoaEmDTO::executa)
                .toList();

        return ResponseEntity.ok(pessoasResponseDTO);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Retorna uma pessoa com usuário", description = "Retorna detalhes de uma pessoa específica pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada", content = @Content)
    })
    @Parameter(description = "ID da pessoa que será recuperada", required = true)
    public ResponseEntity<PessoaResponseDTO> retornaPessoaComUsuario(@PathVariable String id) {
        Pessoa pessoa = buscarPessoa.buscarPorId(id);

        PessoaResponseDTO pessoaResponseDTO = converterPessoaEmDTO.executa(pessoa);

        return ResponseEntity.ok(pessoaResponseDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma pessoa com usuário", description = "Cria uma nova pessoa com detalhes de usuário.")
    @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PessoaResponseDTO.class)))
    public ResponseEntity<PessoaResponseDTO> criaPessoaComUsuario(@RequestBody PessoaUsuarioRequestDTO pessoa) {

        Pessoa novaPessoa = adicionarPessoaComUsuario.executa(pessoa);

        PessoaResponseDTO pessoaResponseDTO = converterPessoaEmDTO.executa(novaPessoa);

        Link link = linkTo(PessoaController.class).slash(pessoaResponseDTO.getPessoaId()).withSelfRel();

        pessoaResponseDTO   .add(link);

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaResponseDTO);
    }
}
