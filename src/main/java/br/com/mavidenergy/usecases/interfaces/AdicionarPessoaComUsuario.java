package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.requests.PessoaUsuarioRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdicionarPessoaComUsuario {
    Pessoa executa(PessoaUsuarioRequestDTO pessoaUsuarioRequestDTO);
}
