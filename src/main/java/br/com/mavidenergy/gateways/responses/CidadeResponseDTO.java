package br.com.mavidenergy.gateways.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CidadeResponseDTO {
    private String cidadeId;
    private String nomeCidade;
    private String nomeEstado;
    private String siglaEstado;
}
