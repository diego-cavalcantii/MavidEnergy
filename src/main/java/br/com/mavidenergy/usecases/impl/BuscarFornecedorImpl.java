package br.com.mavidenergy.usecases.impl;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.repositories.FornecedorRepository;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import br.com.mavidenergy.usecases.interfaces.BuscarFornecedor;
import br.com.mavidenergy.usecases.interfaces.CalcularDistanciaLatELong;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscarFornecedorImpl implements BuscarFornecedor {

    private final FornecedorRepository fornecedorRepository;
    private final CalcularDistanciaLatELong calcularDistanciaLatELong;
    private final PaginarResultados paginarResultados;
    private final ConverteFornecedorEmDTOImpl converteFornecedorEmDTO;


    @Override
    public List<Fornecedor> buscarFornecedores() {
        return fornecedorRepository.buscarTodosComEndereco();
    }

    @Override
    public Page<FornecedorResponseDTO> buscarFornecedorMaisProximo(Double latitude, Double longitude, List<Fornecedor> fornecedores, Pageable pageable) {
        // Calcular distância e mapear para DTO
        List<FornecedorResponseDTO> fornecedorResponseDTOS = fornecedores.stream()
                .map(fornecedor -> {
                    Endereco endereco = fornecedor.getEndereco();
                    EnderecoResponseDTO enderecoDTO = null;
                    Double distancia = null;

                    if (endereco != null) {
                        distancia = calcularDistanciaLatELong.calcularDistancia(
                                latitude,
                                longitude,
                                Double.valueOf(endereco.getLatitude()),
                                Double.valueOf(endereco.getLongitude()));

                        enderecoDTO = EnderecoResponseDTO.builder()
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

                    return FornecedorResponseDTO.builder()
                            .nomeFornecedor(fornecedor.getNomeFornecedor())
                            .cnpj(fornecedor.getCnpj())
                            .email(fornecedor.getEmail())
                            .telefone(fornecedor.getTelefone())
                            .endereco(enderecoDTO)
                            .distancia(distancia)
                            .build();
                })
                .filter(fornecedor -> fornecedor.getEndereco() != null) // Garante que o fornecedor tenha endereço
                .sorted(Comparator.comparingDouble(FornecedorResponseDTO::getDistancia)) // Ordena pelo mais próximo
                .collect(Collectors.toList());

        // Paginar os resultados manualmente
        return paginarResultados.paginarResultados(fornecedorResponseDTOS, pageable);
    }



}
