package br.com.mavidenergy.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String consultaId;

    private String bandeira;
    private Double valorKwh;

    @ManyToOne
    private Pessoa pessoa;

    @OneToOne
    private Endereco endereco;
}
