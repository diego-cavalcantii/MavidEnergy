package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.gateways.responses.DadosClimaticosResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface BuscarDadosClimaticos {
    DadosClimaticosResponseDTO buscarPorCidade(String cidade);
}
