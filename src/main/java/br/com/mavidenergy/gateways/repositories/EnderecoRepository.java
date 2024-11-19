package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
