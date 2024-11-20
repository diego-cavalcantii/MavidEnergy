package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import br.com.mavidenergy.usecases.interfaces.CalcularDistanciaLatELong;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteFornecedorEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConverteFornecedorEmDTOImpl implements ConverteFornecedorEmDTO {

    private final ConverteEnderecoDTO converteEnderecoDTO;

    @Override
    public List<FornecedorResponseDTO> executa(List<Fornecedor> fornecedores) {

        List<FornecedorResponseDTO> fornecedorResponseDTOS = fornecedores.stream().map(fornecedor -> {
            EnderecoResponseDTO enderecoResponseDTO = converteEnderecoDTO.executa(fornecedor.getEndereco());


            // Criar o DTO do fornecedor
            return FornecedorResponseDTO.builder()
                    .nomeFornecedor(fornecedor.getNomeFornecedor())
                    .cnpj(fornecedor.getCnpj())
                    .email(fornecedor.getEmail())
                    .telefone(fornecedor.getTelefone())
                    .endereco(enderecoResponseDTO) // Pode ser nulo
                    .build();
        }).toList();

        return fornecedorResponseDTOS;
    }

}
