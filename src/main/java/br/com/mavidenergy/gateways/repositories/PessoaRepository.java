package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface PessoaRepository extends JpaRepository<Pessoa, String> {

}
