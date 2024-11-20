package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ConverteFornecedorPaginadoEmDTO {
    FornecedorPaginadoResponseDTO executa(Fornecedor fornecedor, Double latitude, Double longitude);
}


