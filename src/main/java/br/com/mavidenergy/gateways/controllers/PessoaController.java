package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.requests.PessoaUsuarioRequestDTO;
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

    @GetMapping
    public ResponseEntity<String> testeApi() {
        return ResponseEntity.ok("API funcionando");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> retornaPessoaComUsuario(@PathVariable String id) {
        Pessoa pessoa = buscarPessoa.buscarPorId(id);
        return ResponseEntity.ok(pessoa);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Pessoa> criaPessoaComUsuario(@RequestBody PessoaUsuarioRequestDTO pessoa) {

        Pessoa novaPessoa = adicionarPessoaComUsuario.executa(pessoa);

        Link link = linkTo(PessoaController.class).slash(novaPessoa.getPessoaId()).withSelfRel();

        novaPessoa.add(link);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
    }
}
