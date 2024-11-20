package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Pessoa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuscarPessoa {

    List<Pessoa> buscarTodos();

    Pessoa buscarPorId(String id);
}
