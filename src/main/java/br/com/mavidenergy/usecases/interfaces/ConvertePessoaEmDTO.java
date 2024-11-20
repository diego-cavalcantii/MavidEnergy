package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.responses.PessoaResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ConvertePessoaEmDTO {
    PessoaResponseDTO executa(Pessoa pessoa);
}
