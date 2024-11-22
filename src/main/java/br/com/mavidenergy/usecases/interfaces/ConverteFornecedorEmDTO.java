package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.FornecedorPaginadoResponseDTO;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface ConverteFornecedorEmDTO {
    FornecedorResponseDTO executa(Fornecedor fornecedor,Double latitude, Double longitude);
}

