package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.exceptions.PessoaNotFoundException;
import br.com.mavidenergy.gateways.repositories.PessoaRepository;
import br.com.mavidenergy.usecases.interfaces.BuscarPessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarPessoaImpl implements BuscarPessoa {

    private final PessoaRepository pessoaRepository;

    @Override
    public List<Pessoa> buscarTodos() {
        List<Pessoa> pessoas = pessoaRepository.findAll();

        if (pessoas.isEmpty()) {
            throw new PessoaNotFoundException("Nenhuma pessoa encontrada");
        }

        return pessoas;
    }

    @Override
    public Pessoa buscarPorId(String id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new PessoaNotFoundException("Pessoa com ID " + id + " não encontrada"));

        return pessoa;
    }
}
