package br.com.mavidenergy.gateways.controllers;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.domains.Usuario;
import br.com.mavidenergy.gateways.repositories.PessoaRepository;
import br.com.mavidenergy.gateways.repositories.UsuarioRepository;
import br.com.mavidenergy.gateways.requests.PessoaUsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<String> testeApi() {
        return ResponseEntity.ok("API funcionando");
    }

    @PostMapping
    public ResponseEntity<Pessoa> criaPessoaComUsuario(@RequestBody PessoaUsuarioDTO pessoa) {
        Usuario novoUsuario = Usuario.builder()
                .email(pessoa.getEmail())
                .senha(pessoa.getSenha())
                .build();

        usuarioRepository.save(novoUsuario);

        Pessoa novaPessoa = Pessoa.builder()
                .nome(pessoa.getNome())
                .usuario(novoUsuario)
                .build();

        pessoaRepository.save(novaPessoa);

        return ResponseEntity.ok(novaPessoa);
    }
}
