package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Pessoa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuscarConsulta {

    Consulta buscarPorId(String id);

    List<Consulta> buscarPorPessoa(Pessoa pessoa);
}
