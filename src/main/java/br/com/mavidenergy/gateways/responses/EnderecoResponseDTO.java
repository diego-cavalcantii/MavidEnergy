package br.com.mavidenergy.gateways.responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
public class EnderecoResponseDTO extends RepresentationModel<EnderecoResponseDTO> {
    private String cep;
    private String logradouro;
    private String numero;
    private String nomeCidade;
    private String nomeEstado;
    private String siglaEstado;
    private Double latitude;
    private Double longitude;
}
