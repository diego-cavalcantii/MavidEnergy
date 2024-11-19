package br.com.mavidenergy.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    private String usuarioId;

    private String email;
    private String senha;

    @OneToOne
    @JoinColumn(name = "pessoaId")
    private Pessoa pessoaId;

}