package br.com.mavidenergy.gateways.responses;

import br.com.mavidenergy.domains.Endereco;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class ConsultaResponseDTO extends RepresentationModel<ConsultaResponseDTO> {
    private String bandeira;
    private Double valorKwh;
    private EnderecoResponseDTO endereco;
    private String EconomiaPotencial;
    private String ValorComDesconto;
    private String ValorSemDesconto;
    private LocalDateTime dataCriacao;


}
