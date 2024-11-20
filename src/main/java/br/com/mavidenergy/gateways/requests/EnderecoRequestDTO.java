package br.com.mavidenergy.gateways.requests;

import br.com.mavidenergy.domains.Pessoa;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnderecoRequestDTO {
    private String cep;
    private String logradouro;
    private String numero;
    private String latitude;
    private String longitude;
    private String cidadeId;
    private String pessoaId;
}
