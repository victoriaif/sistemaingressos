package br.com.sistemaingressos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sistemaingressos.model.Usuario;
import br.com.sistemaingressos.repository.queries.usuario.UsuarioQueries;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, UsuarioQueries {
    // Usuario findByEmail(String email);
    Optional<Usuario> findByEmail(String email);   
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}