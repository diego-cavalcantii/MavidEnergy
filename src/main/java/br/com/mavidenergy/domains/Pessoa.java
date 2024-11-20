package br.com.mavidenergy.domains;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"pessoaId", "nome", "usuario", "enderecos"})
public class Pessoa extends RepresentationModel<Pessoa> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String pessoaId;

    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;


    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;

}
