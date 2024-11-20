package br.com.mavidenergy.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String fornecedorId;

    private String nomeFornecedor;
    private String cnpj;
    private String telefone;
    private String email;

    @OneToOne(mappedBy = "fornecedor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Endereco endereco;


}
