package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.gateways.exceptions.EnderecoNotFoundException;
import br.com.mavidenergy.gateways.repositories.EnderecoRepository;
import br.com.mavidenergy.usecases.interfaces.BuscarEndereco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarEnderecoImpl implements BuscarEndereco {

    private final EnderecoRepository enderecoRepository;

    @Override
    public Endereco buscarPorId(String enderecoId) {
        Endereco endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new EnderecoNotFoundException("Endereço com ID " + enderecoId + " não encontrado"));

        return endereco;
    }
}
