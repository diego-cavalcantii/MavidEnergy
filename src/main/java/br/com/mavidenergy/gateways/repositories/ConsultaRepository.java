package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, String> {
}
