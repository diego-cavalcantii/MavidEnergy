package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteFornecedorEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConverterFornecedorEmDTOImpl implements ConverteFornecedorEmDTO {

    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;

    @Override
    public FornecedorResponseDTO executa(Fornecedor fornecedor) {
        EnderecoResponseDTO enderecoDTO = converteEnderecoEmDTO.executa(fornecedor.getEndereco());

        return FornecedorResponseDTO.builder()
                .nomeFornecedor(fornecedor.getNomeFornecedor())
                .cnpj(fornecedor.getCnpj())
                .email(fornecedor.getEmail())
                .telefone(fornecedor.getTelefone())
                .endereco(enderecoDTO)
                .build();
    }
}

