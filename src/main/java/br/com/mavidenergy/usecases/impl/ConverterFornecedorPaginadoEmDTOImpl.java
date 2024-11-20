package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import br.com.mavidenergy.usecases.interfaces.CalcularDistanciaLatELong;
import br.com.mavidenergy.usecases.interfaces.ConverteEnderecoEmDTO;
import br.com.mavidenergy.usecases.interfaces.ConverteFornecedorPaginadoEmDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConverterFornecedorPaginadoEmDTOImpl implements ConverteFornecedorPaginadoEmDTO {

    private final ConverteEnderecoEmDTO converteEnderecoEmDTO;
    private final CalcularDistanciaLatELong calcularDistanciaLatELong;

    @Override
    public FornecedorPaginadoResponseDTO executa(Fornecedor fornecedor, Double latitude, Double longitude) {
        EnderecoResponseDTO enderecoDTO = converteEnderecoEmDTO.executa(fornecedor.getEndereco());

        Double distancia = null;
        if (enderecoDTO != null) {
            distancia = calcularDistanciaLatELong.calcularDistancia(
                    latitude,
                    longitude,
                    enderecoDTO.getLatitude(),
                    enderecoDTO.getLongitude()
            );
        }

        return FornecedorPaginadoResponseDTO.builder()
                .nomeFornecedor(fornecedor.getNomeFornecedor())
                .cnpj(fornecedor.getCnpj())
                .email(fornecedor.getEmail())
                .telefone(fornecedor.getTelefone())
                .endereco(enderecoDTO)
                .distancia(distancia)
                .build();
    }
}

