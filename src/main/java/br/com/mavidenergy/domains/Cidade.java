package br.com.mavidenergy.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cidade {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cidadeId;
    private String nomeCidade;
    private String nomeEstado;
    private String siglaEstado;
    private String latitude;
    private String longitude;
}
