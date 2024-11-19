package br.com.mavidenergy.usecases.interfaces;

import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.gateways.requests.EnderecoRequestDTO;
import br.com.mavidenergy.gateways.responses.EnderecoResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdicionarEndereco {
    EnderecoResponseDTO executa(EnderecoRequestDTO enderecoRequestDTO);
}
