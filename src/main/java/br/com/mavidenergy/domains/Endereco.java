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
    private String latitude;
    private String longitude;

    @ManyToOne
    @JoinColumn(name = "cidadeId")
    private Cidade cidade;

    @ManyToOne
    @JoinColumn(name = "pessoaId")
    private Pessoa pessoa;

    @OneToOne
    @JoinColumn(name = "fornecedorId")
    private Fornecedor fornecedor;

    @OneToOne(mappedBy = "endereco", cascade = CascadeType.ALL)
    private Consulta consulta;

}
