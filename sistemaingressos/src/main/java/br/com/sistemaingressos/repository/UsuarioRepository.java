package br.com.sistemaingressos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sistemaingressos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   
}