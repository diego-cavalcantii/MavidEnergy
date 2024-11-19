package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Cidade;
import br.com.mavidenergy.gateways.repositories.CidadeRepository;
import br.com.mavidenergy.usecases.interfaces.BuscarCidade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BuscarCidadeImpl implements BuscarCidade {

    private final CidadeRepository cidadeRepository;

    @Override
    public Cidade buscarPorId(String id) {
        Cidade cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cidade não encontrado"));

        return cidade;
    }

    @Override
    public List<Cidade> buscarTodos() {
        return cidadeRepository.findAll();
    }
}
