package br.com.mavidenergy.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalhes de erro padrão para respostas da API")
@Builder
public class ErrorResponseDTO {
    @Schema(description = "Código de status HTTP da resposta de erro")
    private int status;

    @Schema(description = "Mensagem de erro descritiva")
    private String message;

    @Schema(description = "Qualquer detalhe adicional")
    private String details;
}

