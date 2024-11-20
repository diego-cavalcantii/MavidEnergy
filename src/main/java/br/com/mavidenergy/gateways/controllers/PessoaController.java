package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.requests.PessoaUsuarioRequestDTO;
import br.com.mavidenergy.gateways.responses.PessoaResponseDTO;
import br.com.mavidenergy.usecases.impl.ConverterPessoaEmDTO;
import br.com.mavidenergy.usecases.interfaces.AdicionarPessoaComUsuario;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {

    private final AdicionarPessoaComUsuario adicionarPessoaComUsuario;
    private final BuscarPessoa buscarPessoa;
    private final ConverterPessoaEmDTO converterPessoaEmDTO;

    @GetMapping
    public ResponseEntity<String> testeApi() {
        return ResponseEntity.ok("API funcionando");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> retornaPessoaComUsuario(@PathVariable String id) {
        Pessoa pessoa = buscarPessoa.buscarPorId(id);

        PessoaResponseDTO pessoaResponseDTO = converterPessoaEmDTO.executa(pessoa);

        return ResponseEntity.ok(pessoaResponseDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<PessoaResponseDTO> criaPessoaComUsuario(@RequestBody PessoaUsuarioRequestDTO pessoa) {

        Pessoa novaPessoa = adicionarPessoaComUsuario.executa(pessoa);

        PessoaResponseDTO pessoaResponseDTO = converterPessoaEmDTO.executa(novaPessoa);

        Link link = linkTo(PessoaController.class).slash(novaPessoa.getPessoaId()).withSelfRel();

        novaPessoa.add(link);

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaResponseDTO);
    }
}
