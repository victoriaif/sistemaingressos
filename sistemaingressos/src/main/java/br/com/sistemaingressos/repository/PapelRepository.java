package br.com.sistemaingressos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sistemaingressos.model.Papel;
import java.util.Optional;

public interface PapelRepository extends JpaRepository<Papel, Long> {
    Optional<Papel> findByNome(String nome);
}
