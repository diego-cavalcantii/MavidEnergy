package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import org.springframework.stereotype.Service;

@Service
public class ConverterEnderecoEmDTOImpl implements ConverteEnderecoEmDTO {
    @Override
    public EnderecoResponseDTO executa(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoResponseDTO.builder()
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .logradouro(endereco.getLogradouro())
                .siglaEstado(endereco.getCidade() != null ? endereco.getCidade().getSiglaEstado() : null)
                .nomeEstado(endereco.getCidade() != null ? endereco.getCidade().getNomeEstado() : null)
                .nomeCidade(endereco.getCidade() != null ? endereco.getCidade().getNomeCidade() : null)
                .latitude(Double.valueOf(endereco.getLatitude()))
                .longitude(Double.valueOf(endereco.getLongitude()))
                .build();
    }
}
