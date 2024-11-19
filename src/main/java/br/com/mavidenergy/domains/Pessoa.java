package br.com.mavidenergy.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String pessoaId;

    private String nome;

    @OneToOne
    private Usuario usuario;
    
}
