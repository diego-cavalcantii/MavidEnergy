package br.com.mavidenergy.gateways.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaRequestDTO {
    private String bandeira;
    private Double valorKwh;
    private String pessoaId;
    private String enderecoId;
}
