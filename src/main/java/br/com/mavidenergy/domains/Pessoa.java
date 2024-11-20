package br.com.mavidenergy.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String pessoaId;

    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuario;


    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Endereco> enderecos;

    // As consultas associadas a esta pessoa
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

}
