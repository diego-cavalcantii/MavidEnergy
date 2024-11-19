package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, String> {
}
