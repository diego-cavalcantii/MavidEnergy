package br.com.mavidenergy.gateways.responses;

import br.com.mavidenergy.domains.Usuario;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
public class PessoaResponseDTO extends RepresentationModel<PessoaResponseDTO> {
    private String pessoaId;
    private String nome;
    private String email;
    private List<EnderecoResponseDTO> enderecos;
}
