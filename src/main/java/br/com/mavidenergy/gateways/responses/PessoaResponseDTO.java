package br.com.mavidenergy.gateways.responses;

import br.com.mavidenergy.domains.Usuario;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PessoaResponseDTO {
    private String pessoaId;
    private String nome;
    private String email;
    private List<EnderecoResponseDTO> enderecos;
}
