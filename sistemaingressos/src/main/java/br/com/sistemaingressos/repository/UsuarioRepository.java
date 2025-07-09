package br.com.sistemaingressos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sistemaingressos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Usuario findByEmail(String email);
    Optional<Usuario> findByEmail(String email);   
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}