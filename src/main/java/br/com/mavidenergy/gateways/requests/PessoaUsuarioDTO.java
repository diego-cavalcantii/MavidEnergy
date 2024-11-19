package br.com.mavidenergy.gateways.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PessoaUsuarioDTO {
    private String nome;
    private String email;
    private String senha;
}
