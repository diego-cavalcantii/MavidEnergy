package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Endereco;
import org.springframework.stereotype.Service;

@Service
public interface BuscarEndereco {
    Endereco buscarPorId(String enderecoId);
}
