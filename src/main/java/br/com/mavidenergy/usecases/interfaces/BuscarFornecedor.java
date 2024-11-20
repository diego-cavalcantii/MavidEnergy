package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BuscarFornecedor {

    List<Fornecedor> buscarFornecedores();

    Page<FornecedorPaginadoResponseDTO> buscarFornecedorMaisProximo(Double latitude, Double longitude, List<Fornecedor> fornecedores, Pageable pageable);
}
