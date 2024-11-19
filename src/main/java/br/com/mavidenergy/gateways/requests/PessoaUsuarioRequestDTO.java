package br.com.mavidenergy.gateways.requests;

import lombok.Builder;
import lombok.Data;

@Data
public class PessoaUsuarioRequestDTO {
    private String nome;
    private String email;
    private String senha;
}
