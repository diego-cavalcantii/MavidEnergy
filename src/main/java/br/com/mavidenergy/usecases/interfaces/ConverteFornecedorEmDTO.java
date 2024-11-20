package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Fornecedor;
import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConverteFornecedorEmDTO {
    List<FornecedorResponseDTO> executa(List<Fornecedor> fornecedores);

}
