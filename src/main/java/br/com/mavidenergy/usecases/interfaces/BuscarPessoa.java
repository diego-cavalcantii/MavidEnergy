package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Pessoa;
import org.springframework.stereotype.Service;

@Service
public interface BuscarPessoa {

    Pessoa buscarPorId(String id);
}
