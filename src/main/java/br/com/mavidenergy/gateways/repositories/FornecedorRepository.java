package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FornecedorRepository extends JpaRepository<Fornecedor,String> {
    @Query("SELECT f FROM Fornecedor f LEFT JOIN FETCH f.endereco e LEFT JOIN FETCH e.cidade")
    List<Fornecedor> buscarTodosComEndereco();


}
