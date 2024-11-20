package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoDTO;
import org.springframework.stereotype.Service;

@Service
public class ConverterEnderecoImpl implements ConverteEnderecoDTO {
    @Override
    public EnderecoResponseDTO executa(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoResponseDTO.builder()
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .logradouro(endereco.getLogradouro() != null ? endereco.getLogradouro() : null)
                .siglaEstado(endereco.getCidade() != null ? endereco.getCidade().getSiglaEstado() : null)
                .nomeEstado(endereco.getCidade() != null ? endereco.getCidade().getNomeEstado() : null)
                .nomeCidade(endereco.getCidade() != null ? endereco.getCidade().getNomeCidade() : null)
                .latitude(endereco.getLatitude() != null ? Double.valueOf(endereco.getLatitude()) : null)
                .longitude(endereco.getLongitude() != null ? Double.valueOf(endereco.getLongitude()) : null)
                .build();
    }
}
