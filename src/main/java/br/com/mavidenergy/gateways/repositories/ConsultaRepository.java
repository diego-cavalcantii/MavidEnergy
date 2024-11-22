package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Consulta;
import br.com.mavidenergy.domains.Endereco;
import br.com.mavidenergy.domains.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, String> {

    List<Consulta> findConsultasByPessoaOrderByDataCriacaoDesc(Pessoa pessoa);

    List<Consulta> findConsultasByEndereco(Endereco endereco);
}
