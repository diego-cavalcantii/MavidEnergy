package br.com.mavidenergy.gateways.responses;

import br.com.mavidenergy.domains.Endereco;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaResponseDTO {
    private String bandeira;
    private Double valorKwh;
    private EnderecoResponseDTO endereco;
    private String EconomiaPotencial;
    private String ValorComDesconto;
    private String ValorSemDesconto;

}
