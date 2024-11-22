package br.com.mavidenergy.gateways.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaRequestDTO {
    private String bandeira;

//    @NotNull(message = "O valor kWh deve ser fornecido.")
//    @Positive(message = "O valor kWh deve ser um número positivo.")
    private Double valorKwh;
    private String pessoaId;
    private String enderecoId;
}
