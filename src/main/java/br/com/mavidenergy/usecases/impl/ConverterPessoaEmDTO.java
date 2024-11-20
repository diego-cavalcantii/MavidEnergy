package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Pessoa;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.PessoaResponseDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import br.com.mavidenergy.usecases.interfaces.ConvertePessoaEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConverterPessoaEmDTO implements ConvertePessoaEmDTO {

    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;

    @Override
    public PessoaResponseDTO executa(Pessoa pessoa) {

        List<EnderecoResponseDTO> enderecoResponseDTOS = Collections.emptyList();
                if (pessoa.getEnderecos() != null) {
                    enderecoResponseDTOS = pessoa.getEnderecos().stream()
                            .map(converteEnderecoEmDTO::executa)
                            .toList();
                }

        return PessoaResponseDTO.builder()
                .pessoaId(pessoa.getPessoaId())
                .nome(pessoa.getNome())
                .email(pessoa.getUsuario().getEmail())
                .enderecos(enderecoResponseDTOS)
                .build();
    }
}
