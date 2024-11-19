package br.com.mavidenergy.domains;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String enderecoId;

    private String cep;
    private String logradouro;
    private String numero;

    @ManyToOne
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "pessoaId")
    private Pessoa pessoa;
}
