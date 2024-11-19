package br.com.mavidenergy.gateways.repositories;

import br.com.mavidenergy.domains.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
