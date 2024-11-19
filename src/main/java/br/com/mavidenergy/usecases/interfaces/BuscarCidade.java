package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Cidade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuscarCidade {

    Cidade buscarPorId(String id);

    List<Cidade> buscarTodos();
}
