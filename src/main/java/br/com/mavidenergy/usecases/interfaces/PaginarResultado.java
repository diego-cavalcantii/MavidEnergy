package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.gateways.responses.FornecedorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaginarResultado {
    Page<FornecedorResponseDTO> paginarResultados(List<FornecedorResponseDTO> fornecedores, Pageable pageable);
}
