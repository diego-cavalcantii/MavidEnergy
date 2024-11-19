package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.domains.Usuario;
import br.com.mavidenergy.gateways.repositories.PessoaRepository;
import br.com.mavidenergy.gateways.repositories.UsuarioRepository;
import br.com.mavidenergy.gateways.requests.PessoaUsuarioRequestDTO;
import br.com.mavidenergy.usecases.interfaces.AdicionarPessoaComUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdicionarPessoaComUsuarioImpl implements AdicionarPessoaComUsuario {

    private final PessoaRepository pessoaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Pessoa executa(PessoaUsuarioRequestDTO pessoaUsuarioRequestDTO) {
        Usuario usuario = Usuario.builder()
                .email(pessoaUsuarioRequestDTO.getEmail())
                .senha(pessoaUsuarioRequestDTO.getSenha())
                .build();

        usuarioRepository.save(usuario);

        Pessoa pessoa = Pessoa.builder()
                .nome(pessoaUsuarioRequestDTO.getNome())
                .usuario(usuario)
                .build();

        pessoaRepository.save(pessoa);

        return pessoa;
    }
}
